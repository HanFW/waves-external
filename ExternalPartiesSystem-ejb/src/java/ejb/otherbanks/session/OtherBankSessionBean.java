package ejb.otherbanks.session;

import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.entity.OtherBankOnHoldRecord;
import ejb.otherbanks.entity.OtherBankAccount;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.BankAccount;
import ws.client.merlionBank.MerlionBankWebService_Service;

@Stateless
public class OtherBankSessionBean implements OtherBankSessionBeanLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

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

        BankAccount bankAccount = retrieveBankAccountByNum(fromBankAccountNum);

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();
        String transactionCode = "BILL";
        String accountCredit = paymentAmt.toString();
        String transactionRef = bankAccount.getBankAccountType() + bankAccount.getBankAccountNum();

        Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, transactionCode,
                transactionRef, " ", accountCredit, dbsBankAccount.getOtherBankAccountId());

    }

    @Override
    public void creditPayementToAccountMTK(String fromBankAccountNum, String toBankAccountNum, Double paymentAmt) {

        DecimalFormat df = new DecimalFormat("#.00");
        Double currentAvailableBalance = 0.0;

        OtherBankAccount koreaBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccountNum);
        if (koreaBankAccount.getAvailableBankAccountBalance() == null) {
            currentAvailableBalance = 0 + paymentAmt;
        } else {
            currentAvailableBalance = Double.valueOf(koreaBankAccount.getAvailableBankAccountBalance()) + paymentAmt;
        }

        koreaBankAccount.setAvailableBankAccountBalance(df.format(currentAvailableBalance));
    }

    @Override
    public void askForCreditOtherBankAccount(Long billId) {
        sACHSessionBeanLocal.ntucInitiateGIRO(billId);
    }

    @Override
    public void settleEachOtherBankAccount() {

        DecimalFormat df = new DecimalFormat("#.00");

        Query query = entityManager.createQuery("SELECT o FROM OtherBankOnHoldRecord o WHERE o.onHoldStatus = :onHoldStatus");
        query.setParameter("onHoldStatus", "New");
        List<OtherBankOnHoldRecord> onHoldRecords = query.getResultList();

        for (OtherBankOnHoldRecord onHoldRecord : onHoldRecords) {

            String bankAccountNum = onHoldRecord.getBankAccountNum();
            String paymentAmt = onHoldRecord.getPaymentAmt();
            String debitOrCredit = onHoldRecord.getDebitOrCredit();
            String debitOrCreditBankAccountNum = onHoldRecord.getDebitOrCreditBankAccountNum();
            String debitOrCreditBankName = onHoldRecord.getDebitOrCreditBankName();

            OtherBankAccount dbsBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
            String currentAvailableBalance = dbsBankAccount.getAvailableBankAccountBalance();
            String currentTotalBalance = dbsBankAccount.getTotalBankAccountBalance();

            if (debitOrCredit.equals("Credit") && debitOrCreditBankName.equals("Merlion")) {

                Double totalAvailableBalance = Double.valueOf(currentAvailableBalance) + Double.valueOf(paymentAmt);
                Double totalBalance = Double.valueOf(currentTotalBalance) + Double.valueOf(paymentAmt);

                dbsBankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
                dbsBankAccount.setTotalBankAccountBalance(totalBalance.toString());

                onHoldRecord.setOnHoldStatus("Done");

                BankAccount bankAccount = retrieveBankAccountByNum(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "BILL";
                String accountCredit = paymentAmt;
                String transactionRef = bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();

                Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, transactionCode,
                        transactionRef, " ", accountCredit, dbsBankAccount.getOtherBankAccountId());

            } else if (debitOrCredit.equals("Debit") && debitOrCreditBankName.equals("Merlion")) {

                Double totalAvailableBalance = Double.valueOf(currentAvailableBalance) - Double.valueOf(paymentAmt);
                Double totalBalance = Double.valueOf(currentTotalBalance) - Double.valueOf(paymentAmt);

                dbsBankAccount.setAvailableBankAccountBalance(totalAvailableBalance.toString());
                dbsBankAccount.setTotalBankAccountBalance(totalBalance.toString());

                onHoldRecord.setOnHoldStatus("Done");

                BankAccount bankAccount = retrieveBankAccountByNum(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "BILL";
                String accountdebit = paymentAmt;
                String transactionRef = bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();

                Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, transactionCode,
                        transactionRef, accountdebit, " ", dbsBankAccount.getOtherBankAccountId());

            } else if (debitOrCredit.equals("Credit") && debitOrCreditBankName.equals("Bank of Korea")) {

                Double totalBalance = Double.valueOf(currentTotalBalance) + Double.valueOf(paymentAmt);
                dbsBankAccount.setTotalBankAccountBalance(df.format(totalBalance));

                onHoldRecord.setOnHoldStatus("Done");

                BankAccount bankAccount = retrieveBankAccountByNum(debitOrCreditBankAccountNum);
                Calendar cal = Calendar.getInstance();
                String transactionDate = cal.getTime().toString();
                String transactionCode = "SWIFT";
                String transactionRef = bankAccount.getBankAccountType() + "-" + bankAccount.getBankAccountNum();

                Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, transactionCode,
                        transactionRef, " ", paymentAmt, dbsBankAccount.getOtherBankAccountId());

            }
        }
    }

    private BankAccount retrieveBankAccountByNum(java.lang.String bankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveBankAccountByNum(bankAccountNum);
    }
}
