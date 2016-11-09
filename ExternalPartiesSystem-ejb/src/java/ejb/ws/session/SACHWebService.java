package ejb.ws.session;

import ejb.billingprocessor.session.NTUCSessionBeanLocal;
import ejb.mas.entity.SACH;
import ejb.mas.session.MEPSSessionBeanLocal;
import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.session.OnHoldSessionBeanLocal;
import ejb.otherbanks.session.OtherBankAccountSessionBeanLocal;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.MerlionBankWebService_Service;
import ws.client.merlionBank.ReceivedCheque;

@WebService(serviceName = "SACHWebService")
@Stateless()
public class SACHWebService {

    @EJB
    private NTUCSessionBeanLocal nTUCSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

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

    @PersistenceContext
    private EntityManager entityManager;

    @WebMethod(operationName = "SACHTransferMTD")
//    @Oneway
    public void SACHTransferMTD(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum, @WebParam(name = "toBankAccountNum") String toBankAccountNum, @WebParam(name = "transferAmt") Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime,
                "DBS&Merlion", "FAST", toBankAccountNum, "DBS", fromBankAccountNum, "Merlion",
                currentTimeMilis, transferAmt, "Successful");
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
                "Merlion", currentTimeMilis, transferAmt, "Successful");
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
    public void clearMerlionReceivedCheque(@WebParam(name = "chequeNum") String chequeNum) {

        ReceivedCheque receivedCheque = retrieveReceivedChequeByNum(chequeNum);

        Double transactionAmt = Double.valueOf(receivedCheque.getTransactionAmt());
        String receivedBankAccountNum = receivedCheque.getReceivedBankAccountNum();
        String issuedBankAccountNum = receivedCheque.getOtherBankAccountNum();

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";
        String paymentMethod = "Cheque";

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod, receivedBankAccountNum,
                "Merlion", issuedBankAccountNum, "DBS", cal.getTimeInMillis(), transactionAmt, "Successful");
        SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 - transactionAmt;
        Double merlionTotalCredit = 0 + transactionAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        Long otherAccountOnHoldId = onHoldSessionBeanLocal.addNewRecord("DBS", issuedBankAccountNum,
                "Debit", transactionAmt.toString(), "New", "Merlion",
                receivedBankAccountNum, "Cheque");
        Long bankAccountOnHoldId = addNewRecord("Merlion", receivedBankAccountNum,
                "Credit", transactionAmt.toString(), "New", "DBS",
                issuedBankAccountNum, "Cheque");

        updateOnHoldChequeNum(bankAccountOnHoldId, chequeNum);
    }

    @WebMethod(operationName = "SACHRegularGIROTransferMTD")
//    @Oneway
    public void SACHRegularGIROTransferMTD(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum,
            @WebParam(name = "toBankAccountNum") String toBankAccountNum,
            @WebParam(name = "transferAmt") Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        OtherBankAccount dbsBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccountNum);
        if (dbsBankAccount.getOtherBankAccountId() == null) {
            Double otherTotalCredit = 0 - transferAmt;
            Double merlionTotalCredit = 0 + transferAmt;
            Long sachId = sACHSessionBeanLocal.addNewSACH(otherTotalCredit, merlionTotalCredit,
                    currentTime, "DBS&Merlion", "Regular GIRO", toBankAccountNum, "DBS",
                    fromBankAccountNum, "Merlion", currentTimeMilis, transferAmt, "Failed");
            SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);
            String failedReason = "Invalid Bank Account Number";
            sach.setFailedReason(failedReason);

            addNewRecord("Merlion", fromBankAccountNum, "Credit",
                    transferAmt.toString(), "New", "DBS", toBankAccountNum,
                    "Regular GIRO");
            onHoldSessionBeanLocal.addNewRecord("DBS", toBankAccountNum,
                    "Debit", transferAmt.toString(), "New", "Merlion",
                    fromBankAccountNum, "Regular GIRO");

            rejectRegularGIROTransaction(fromBankAccountNum, transferAmt, toBankAccountNum);
        } else {
            Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime,
                    "DBS&Merlion", "Regular GIRO", toBankAccountNum, "DBS", fromBankAccountNum,
                    "Merlion", currentTimeMilis, transferAmt, "Successful");
            SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

            Double dbsTotalCredit = 0 + transferAmt;
            Double merlionTotalCredit = 0 - transferAmt;

            sach.setBankBTotalCredit(dbsTotalCredit);
            sach.setBankATotalCredit(merlionTotalCredit);

            addNewRecord("Merlion", fromBankAccountNum, "Debit",
                    transferAmt.toString(), "New", "DBS", toBankAccountNum,
                    "Regular GIRO");
            onHoldSessionBeanLocal.addNewRecord("DBS", toBankAccountNum,
                    "Credit", transferAmt.toString(), "New", "Merlion",
                    fromBankAccountNum, "Regular GIRO");
        }
    }

    @WebMethod(operationName = "addNewSACH")
    public Long addNewSACH(@WebParam(name = "otherTotalCredit") Double otherTotalCredit,
            @WebParam(name = "merlionTotalCredit") Double merlionTotalCredit,
            @WebParam(name = "currentTime") String currentTime,
            @WebParam(name = "bankNames") String bankNames,
            @WebParam(name = "paymentMethod") String paymentMethod,
            @WebParam(name = "creditAccountNum") String creditAccountNum,
            @WebParam(name = "creditBank") String creditBank,
            @WebParam(name = "debitAccountNum") String debitAccountNum,
            @WebParam(name = "debitBank") String debitBank,
            @WebParam(name = "currentTimeMilis") Long currentTimeMilis,
            @WebParam(name = "creditAmt") Double creditAmt,
            @WebParam(name = "sachStatus") String sachStatus) {
        SACH sach = new SACH();

        sach.setBankBTotalCredit(otherTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);
        sach.setCurrentTime(currentTime);
        sach.setBankNames(bankNames);
        sach.setPaymentMethod(paymentMethod);
        sach.setCreditAccountNum(creditAccountNum);
        sach.setCreditBank(creditBank);
        sach.setDebitAccountNum(debitAccountNum);
        sach.setDebitBank(debitBank);
        sach.setCurrentTimeMilis(currentTimeMilis);
        sach.setCreditAmt(creditAmt);
        sach.setSachStatus(sachStatus);

        entityManager.persist(sach);
        entityManager.flush();

        return sach.getSachId();
    }

    @WebMethod(operationName = "retrieveSACHById")
    public SACH retrieveSACHById(@WebParam(name = "sachId") Long sachId) {
        SACH sach = new SACH();

        try {
            Query query = entityManager.createQuery("Select s From SACH s Where s.sachId=:sachId");
            query.setParameter("sachId", sachId);

            if (query.getResultList().isEmpty()) {
                return new SACH();
            } else {
                sach = (SACH) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SACH();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return sach;
    }

    @WebMethod(operationName = "passStandingGIROToSACH")
//    @Oneway
    public void passStandingGIROToSACH(@WebParam(name = "customerName") String customerName,
            @WebParam(name = "customerMobile") String customerMobile,
            @WebParam(name = "billReference") String billReference,
            @WebParam(name = "billingOrganizationName") String billingOrganizationName,
            @WebParam(name = "creditBank") String creditBank,
            @WebParam(name = "creditBankAccountNum") String creditBankAccountNum,
            @WebParam(name = "debitBank") String debitBank,
            @WebParam(name = "debitBankAccountNum") String debitBankAccountNum,
            @WebParam(name = "paymemtLimit") String paymemtLimit,
            @WebParam(name = "billStatus") String billStatus,
            @WebParam(name = "buttonRender") boolean buttonRender) {

        nTUCSessionBeanLocal.receiveStandingGIROFromSACH(customerName, customerMobile,
                billReference, billingOrganizationName, creditBank, creditBankAccountNum,
                debitBank, debitBankAccountNum, paymemtLimit, billStatus, buttonRender);
    }

    @WebMethod(operationName = "passNonStandingGIROToSACH")
//    @Oneway
    public void passNonStandingGIROToSACH(@WebParam(name = "customerName") String customerName,
            @WebParam(name = "customerMobile") String customerMobile,
            @WebParam(name = "billReference") String billReference,
            @WebParam(name = "billingOrganizationName") String billingOrganizationName,
            @WebParam(name = "creditBank") String creditBank,
            @WebParam(name = "creditBankAccountNum") String creditBankAccountNum,
            @WebParam(name = "debitBank") String debitBank,
            @WebParam(name = "debitBankAccountNum") String debitBankAccountNum,
            @WebParam(name = "paymemtLimit") String paymemtLimit,
            @WebParam(name = "billStatus") String billStatus,
            @WebParam(name = "paymentFrequency") String paymentFrequency) {

        nTUCSessionBeanLocal.receiveNonStandingGIROFromSACH(customerName, customerMobile,
                billReference, billingOrganizationName, creditBank, creditBankAccountNum,
                debitBank, debitBankAccountNum, paymemtLimit, billStatus, paymentFrequency);
    }

    private Long addNewRecord(java.lang.String bankName, java.lang.String bankAccountNum, java.lang.String debitOrCredit, java.lang.String paymentAmt, java.lang.String onHoldStatus, java.lang.String debitOrCreditBankName, java.lang.String debitOrCreditBankAccountNum, java.lang.String paymentMethod) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.addNewRecord(bankName, bankAccountNum, debitOrCredit, paymentAmt, onHoldStatus, debitOrCreditBankName, debitOrCreditBankAccountNum, paymentMethod);
    }

    private void updateOnHoldChequeNum(java.lang.Long onHoldRecordId, java.lang.String chequeNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.updateOnHoldChequeNum(onHoldRecordId, chequeNum);
    }

    private ReceivedCheque retrieveReceivedChequeByNum(java.lang.String chequeNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveReceivedChequeByNum(chequeNum);
    }

    private void rejectRegularGIROTransaction(java.lang.String bankAccountNum, java.lang.Double transferAmt, java.lang.String toBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.rejectRegularGIROTransaction(bankAccountNum, transferAmt, toBankAccountNum);
    }
}
