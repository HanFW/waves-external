package ejb.ws.session;

import ejb.mas.entity.SACH;
import ejb.mas.session.MEPSSessionBeanLocal;
import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.session.OnHoldSessionBeanLocal;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.MerlionBankWebService_Service;
import ws.client.merlionBank.OnHoldRecord;
import ws.client.merlionBank.ReceivedCheque;

@WebService(serviceName = "SACHWebService")
@Stateless()

public class SACHWebService {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @EJB
    private OnHoldSessionBeanLocal onHoldSessionBeanLocal;

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @EJB
    private MEPSSessionBeanLocal mEPSSessionBeanLocal;

    @WebMethod(operationName = "SACHTransferMTD")
//    @Oneway
    public void SACHTransferMTD(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum, @WebParam(name = "toBankAccountNum") String toBankAccountNum, @WebParam(name = "transferAmt") Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime,
                "DBS&Merlion", "FAST", toBankAccountNum, "DBS", fromBankAccountNum, "Merlion",
                currentTimeMilis, transferAmt);
        SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transferAmt;
        Double merlionTotalCredit = 0 - transferAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        otherBankSessionBeanLocal.actualMTOFastTransfer(fromBankAccountNum, toBankAccountNum, transferAmt);
        mEPSSessionBeanLocal.MEPSSettlementMTD("88776655", "44332211", transferAmt);

    }

    @WebMethod(operationName = "SACHNonStandingGIROTransferMTD")
//    @Oneway
    public void SACHNonStandingGIROTransferMTD(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum,
            @WebParam(name = "toBankAccountNum") String toBankAccountNum,
            @WebParam(name = "transferAmt") Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime,
                "DBS&Merlion", "Non Standing GIRO", toBankAccountNum, "DBS", fromBankAccountNum,
                "Merlion", currentTimeMilis, transferAmt);
        SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transferAmt;
        Double merlionTotalCredit = 0 - transferAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        addNewRecord("Merlion", fromBankAccountNum, "Debit",
                transferAmt.toString(), "New", "DBS", toBankAccountNum,
                "Non Standing GIRO");
        onHoldSessionBeanLocal.addNewRecord("DBS", toBankAccountNum,
                "Credit", transferAmt.toString(), "New", "Merlion",
                fromBankAccountNum, "Non Standing GIRO");
    }

    @WebMethod(operationName = "clearMerlionReceivedCheque")
    public void clearMerlionReceivedCheque(@WebParam(name = "chequeId") Long chequeId) {

        ReceivedCheque receivedCheque = retrieveReceivedChequeById(chequeId);

        Double transactionAmt = Double.valueOf(receivedCheque.getTransactionAmt());
        String bankAccountNum = receivedCheque.getReceivedBankAccountNum();
        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";
        String paymentMethod = "Cheque";

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod, bankAccountNum, "Merlion",
                "11111111", "DBS", cal.getTimeInMillis(), transactionAmt);
        SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 - transactionAmt;
        Double merlionTotalCredit = 0 + transactionAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        Long otherAccountOnHoldId = onHoldSessionBeanLocal.addNewRecord("DBS", "11111111",
                "Debit", transactionAmt.toString(), "New", "Merlion",
                bankAccountNum, "Cheque");
        Long bankAccountOnHoldId = addNewRecord("Merlion", bankAccountNum,
                "Credit", transactionAmt.toString(), "New", "DBS",
                "11111111", "Cheque");
        
        updateOnHoldChequeId(bankAccountOnHoldId, chequeId);
    }

    private ReceivedCheque retrieveReceivedChequeById(java.lang.Long chequeId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveReceivedChequeById(chequeId);
    }

    private Long addNewRecord(java.lang.String bankName, java.lang.String bankAccountNum, java.lang.String debitOrCredit, java.lang.String paymentAmt, java.lang.String onHoldStatus, java.lang.String debitOrCreditBankName, java.lang.String debitOrCreditBankAccountNum, java.lang.String paymentMethod) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.addNewRecord(bankName, bankAccountNum, debitOrCredit, paymentAmt, onHoldStatus, debitOrCreditBankName, debitOrCreditBankAccountNum, paymentMethod);
    }

    private OnHoldRecord retrieveOnHoldRecordById(java.lang.Long onHoldRecordId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveOnHoldRecordById(onHoldRecordId);
    }

    private void updateOnHoldChequeId(java.lang.Long onHoldRecordId, java.lang.Long chequeId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.updateOnHoldChequeId(onHoldRecordId, chequeId);
    }
}
