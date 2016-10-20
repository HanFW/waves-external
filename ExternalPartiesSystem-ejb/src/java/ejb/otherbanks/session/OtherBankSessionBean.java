package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankAccount;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.BankAccount;
import ws.client.merlionBank.MerlionBankWebService_Service;

@Stateless
public class OtherBankSessionBean implements OtherBankSessionBeanLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service;

    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @Override
    public void actualTransfer(String fromAccountNum, String toAccountNum, Double transferAmt) {

        OtherBankAccount otherBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccountNum);
        BankAccount bankAccount = retrieveBankAccountByNum(fromAccountNum);
        Double availableBankAcocuntBalance = Double.valueOf(otherBankAccount.getAvailableBankAccountBalance()) + transferAmt;
        Double totalBankAcocuntBalance = Double.valueOf(otherBankAccount.getTotalBankAccountBalance()) + transferAmt;

        Calendar cal = Calendar.getInstance();
        String otherTransactionCode = "ICT";
        String otherTransactionRef = "Transfer from " + bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();
        String otherAccountDebit = " ";
        String otherAccountCredit = transferAmt.toString();

        Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(cal.getTime().toString(),
                otherTransactionCode, otherTransactionRef, otherAccountDebit, otherAccountCredit, otherBankAccount.getOtherBankAccountId());

        otherBankAccount.setAvailableBankAccountBalance(availableBankAcocuntBalance.toString());
        otherBankAccount.setTotalBankAccountBalance(totalBankAcocuntBalance.toString());
    }

    private BankAccount retrieveBankAccountByNum(java.lang.String bankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service.getMerlionBankWebServicePort();
        return port.retrieveBankAccountByNum(bankAccountNum);
    }
}
