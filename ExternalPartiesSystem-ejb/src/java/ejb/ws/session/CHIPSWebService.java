package ejb.ws.session;

import ejb.mas.entity.CHIPS;
import ejb.mas.entity.SWIFT;
import ejb.mas.entity.SWIFTCode;
import ejb.mas.session.CHIPSSessionBeanLocal;
import ejb.mas.session.SWIFTCodeSessionBeanLocal;
import ejb.mas.session.SWIFTSessionBeanLocal;
import ejb.otherbanks.session.OnHoldSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.MerlionBankWebService_Service;

@WebService(serviceName = "CHIPSWebService")
@Stateless()

public class CHIPSWebService {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private CHIPSSessionBeanLocal cHIPSSessionBeanlocal;

    @EJB
    private SWIFTCodeSessionBeanLocal sWIFTCodeSessionBeanLocal;

    @EJB
    private SWIFTSessionBeanLocal sWIFTSessionBeanLocal;

    @EJB
    private OnHoldSessionBeanLocal onHoldSessionBeanLocal;

    @WebMethod(operationName = "clearSWIFTTransferMTK")
//    @Oneway
    public void clearSWIFTTransferMTK(@WebParam(name = "swiftId") Long swiftId) {

        SWIFT swift = sWIFTSessionBeanLocal.retrieveSWIFTById(swiftId);

        Double transactionAmt = Double.valueOf(swift.getPaymentAmt());
        String receivedSWIFTCode = swift.getSwiftCodeB();
        SWIFTCode receivedSwiftCode = sWIFTCodeSessionBeanLocal.retrieveSWIFTBySWIFTCode(receivedSWIFTCode);
        String receivedBankAccountNum = receivedSwiftCode.getBankAccountNum();
        String transferedBankAccountNum = swift.getTransferedBankAccountNum();

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "Merlion&BankofKorea";
        String paymentMethod = "SWIFT";

        Long chipsId = cHIPSSessionBeanlocal.addNewCHIPS(0.0, 0.0, currentTime, bankNames, paymentMethod, "22222222",
                "Bank of Korea", receivedBankAccountNum, "Merlion", cal.getTimeInMillis(), transactionAmt);

        CHIPS chips = cHIPSSessionBeanlocal.retrieveCHIPSById(chipsId);

        Double koreaTotalCredit = 0 + transactionAmt;
        Double merlionTotalCredit = 0 - transactionAmt;

        chips.setBankBTotalCredit(koreaTotalCredit);
        chips.setBankATotalCredit(merlionTotalCredit);

        onHoldSessionBeanLocal.addNewRecord("Bank of Korea", receivedBankAccountNum,
                "Credit", transactionAmt.toString(), "New", "Bank of Korea",
                transferedBankAccountNum, "SWIFT");
        addNewRecord("Merlion", transferedBankAccountNum,
                "Debit", transactionAmt.toString(), "New", "Bank of Korea",
                receivedBankAccountNum, "SWIFT");
    }

    private Long addNewRecord(java.lang.String bankName, java.lang.String bankAccountNum, java.lang.String debitOrCredit, java.lang.String paymentAmt, java.lang.String onHoldStatus, java.lang.String debitOrCreditBankName, java.lang.String debitOrCreditBankAccountNum, java.lang.String paymentMethod) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.addNewRecord(bankName, bankAccountNum, debitOrCredit, paymentAmt, onHoldStatus, debitOrCreditBankName, debitOrCreditBankAccountNum, paymentMethod);
    }

}
