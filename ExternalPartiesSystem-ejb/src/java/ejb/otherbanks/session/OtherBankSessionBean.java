package ejb.otherbanks.session;

import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.entity.OtherBankAccount;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.BankAccount;
import ws.client.merlionBank.MerlionBankWebService_Service;

@Stateless
public class OtherBankSessionBean implements OtherBankSessionBeanLocal {

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @Override
    public void actualMTOFastTransfer(String fromAccountNum, String toAccountNum, Double transferAmt) {

        DecimalFormat df = new DecimalFormat("#.00");

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

        otherBankAccount.setAvailableBankAccountBalance(df.format(availableBankAcocuntBalance));
        otherBankAccount.setTotalBankAccountBalance(df.format(totalBankAcocuntBalance));
    }

    @Override
    public void creditPaymentToAccountMTD(String fromBankAccountNum, String toBankAccountNum, Double paymentAmt) {

        DecimalFormat df = new DecimalFormat("#.00");
        Double currentAvailableBalance = 0.0;

        OtherBankAccount dbsBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccountNum);
        if (dbsBankAccount.getAvailableBankAccountBalance() == null) {
            currentAvailableBalance = 0 + paymentAmt;
        } else {
            currentAvailableBalance = Double.valueOf(dbsBankAccount.getAvailableBankAccountBalance()) + paymentAmt;
        }

        dbsBankAccount.setAvailableBankAccountBalance(df.format(currentAvailableBalance));
        dbsBankAccount.setTotalBankAccountBalance(df.format(currentAvailableBalance));

//        BankAccount bankAccount = retrieveBankAccountByNum(fromBankAccountNum);

//        Calendar cal = Calendar.getInstance();
//        String transactionDate = cal.getTime().toString();
//        String transactionCode = "BILL";
//        String accountCredit = paymentAmt.toString();
//        String transactionRef = bankAccount.getBankAccountType() + bankAccount.getBankAccountNum();
//
//        Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, transactionCode,
//                transactionRef, " ", accountCredit, dbsBankAccount.getOtherBankAccountId());

    }

    @Override
    public void askForCreditOtherBankAccount(Long billId) {
        sACHSessionBeanLocal.ntucInitiateGIRO(billId);
    }

    private BankAccount retrieveBankAccountByNum(java.lang.String bankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveBankAccountByNum(bankAccountNum);
    }
}
