package ejb.otherbanks.session;

import ejb.billingprocessor.entity.Bill;
import ejb.billingprocessor.session.BillSessionBeanLocal;
import ejb.card.entity.MasterCardClearingNetwork;
import ejb.card.entity.VisaClearingNetwork;
import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.entity.OtherBankOnHoldRecord;
import ejb.otherbanks.entity.OtherBankAccount;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

    @EJB
    private BillSessionBeanLocal billSessionBeanLocal;

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
    public String askForCreditOtherBankAccount(Long billId) {

        String result = sACHSessionBeanLocal.ntucInitiateGIRO(billId);

        if (result.equals("Exceed Payment Limit")) {
            return "Exceed Payment Limit";
        }

        return "Payment Approved";
    }

    @Override
    public void askForRejectBillingPayment(Long billId) {

        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);
        String debilBankAccountNum = bill.getDebitBankAccountNum();
        String creditBankAccountNum = bill.getCreditBankAccountNum();
        String paymentAmt = bill.getPaymentAmt();
        String billReference = bill.getBillReference();

        sACHSessionBeanLocal.rejectStandingGIRO(billReference, creditBankAccountNum, debilBankAccountNum);
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
            String paymentMethod = onHoldRecord.getPaymentMethod();

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
                String transactionCode = "";

                if (paymentMethod.equals("Non Standing GIRO") || paymentMethod.equals("Standing GIRO")) {
                    transactionCode = "BILL";
                } else if (paymentMethod.equals("Cheque")) {
                    transactionCode = "CHQ";
                } else if (paymentMethod.equals("Regular GIRO")) {
                    transactionCode = "GIRO";
                }

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
                String transactionCode = "";

                if (paymentMethod.equals("Non Standing GIRO") || paymentMethod.equals("Standing GIRO")) {
                    transactionCode = "BILL";
                } else if (paymentMethod.equals("Cheque")) {
                    transactionCode = "CHQ";
                } else if (paymentMethod.equals("Regular GIRO")) {
                    transactionCode = "GIRO";
                }

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

    @Override
    public void merchantVisaNetworkSettlement() {
        System.out.println("enter citi pays merchant by via");
        List<VisaClearingNetwork> visaRecords = new ArrayList<>();

        List<Long> merchantAccountIds = otherBankAccountSessionBeanLocal.getAllCitiBankMerchantAccountId();
        double creditAmt = 0.0;

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();

        //suppose accout num of three merchants is pre-set 
        //watsons
        Query q = entityManager.createQuery("select v from VisaClearingNetwork v where v.merchantName=:merchant AND v.payMerchantStatus=:status");
        q.setParameter("merchant", "watsons");
        q.setParameter("status", "no");

        if (!q.getResultList().isEmpty()) {
            visaRecords = q.getResultList();
            for (int i = 0; i < visaRecords.size(); i++) {
                creditAmt += visaRecords.get(i).getTransactionAmt();
                visaRecords.get(i).setPayMerchantStatus("yes");
                entityManager.flush();
            }
            System.out.println("test q - credit amt " + creditAmt);
            otherBankAccountSessionBeanLocal.updateOtherBankAccountBalanceById(merchantAccountIds.get(0), creditAmt);
            Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, "CTW", "Transfer from citiBank to watsons", "", String.valueOf(creditAmt), merchantAccountIds.get(0));
        }

        //ntuc
        Query q2 = entityManager.createQuery("select v from VisaClearingNetwork v where v.merchantName=:merchant AND v.payMerchantStatus=:status");
        q2.setParameter("merchant", "ntuc");
        q2.setParameter("status", "no");

        if (!q2.getResultList().isEmpty()) {
            visaRecords = q2.getResultList();
            for (int i = 0; i < visaRecords.size(); i++) {
                creditAmt += visaRecords.get(i).getTransactionAmt();
                visaRecords.get(i).setPayMerchantStatus("yes");
                entityManager.flush();
            }
            System.out.println("test q2 - credit amt " + creditAmt);
            otherBankAccountSessionBeanLocal.updateOtherBankAccountBalanceById(merchantAccountIds.get(1), creditAmt);
            Long otherTransactionId2 = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, "CTN", "Transfer from citiBank to ntuc", "", String.valueOf(creditAmt), merchantAccountIds.get(1));
        }

        //sephora
        Query q3 = entityManager.createQuery("select v from VisaClearingNetwork v where v.merchantName=:merchant AND v.payMerchantStatus=:status");
        q3.setParameter("merchant", "sephora");
        q3.setParameter("status", "no");

        if (!q3.getResultList().isEmpty()) {
            visaRecords = q3.getResultList();
            for (int i = 0; i < visaRecords.size(); i++) {
                creditAmt += visaRecords.get(i).getTransactionAmt();
                visaRecords.get(i).setPayMerchantStatus("yes");
                entityManager.flush();
            }
            System.out.println("test q3 - credit amt " + creditAmt);
            otherBankAccountSessionBeanLocal.updateOtherBankAccountBalanceById(merchantAccountIds.get(2), creditAmt);
            Long otherTransactionId3 = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, "CTS", "Transfer from citiBank to sephora", "", String.valueOf(creditAmt), merchantAccountIds.get(2));
        }

    }

    @Override
    public void merchantMasterCardNetworkSettlement() {
        List<MasterCardClearingNetwork> masterCardRecords = new ArrayList<>();

        List<Long> merchantAccountIds = otherBankAccountSessionBeanLocal.getAllCitiBankMerchantAccountId();
        double creditAmt = 0.0;

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();

        //suppose accout num of three merchants is pre-set 
        //watsons
        Query q = entityManager.createQuery("select m from MasterCardClearingNetwork m where m.merchantName=:merchant AND m.payMerchantStatus=:status");
        q.setParameter("merchant", "watsons");
        q.setParameter("status", "no");

        if (!q.getResultList().isEmpty()) {
            masterCardRecords = q.getResultList();
            for (int i = 0; i < masterCardRecords.size(); i++) {
                creditAmt += masterCardRecords.get(i).getTransactionAmt();
                masterCardRecords.get(i).setPayMerchantStatus("yes");
                entityManager.flush();
            }
            otherBankAccountSessionBeanLocal.updateOtherBankAccountBalanceById(merchantAccountIds.get(0), creditAmt);
            Long otherTransactionId = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, "CTW", "Transfer from citiBank to watsons", "", String.valueOf(creditAmt), merchantAccountIds.get(0));
        }

        //ntuc
        Query q2 = entityManager.createQuery("select m from MasterCardClearingNetwork m where m.merchantName=:merchant AND m.payMerchantStatus=:status");
        q2.setParameter("merchant", "ntuc");
        q2.setParameter("status", "no");

        if (!q2.getResultList().isEmpty()) {
            masterCardRecords = q2.getResultList();
            for (int i = 0; i < masterCardRecords.size(); i++) {
                creditAmt += masterCardRecords.get(i).getTransactionAmt();
                masterCardRecords.get(i).setPayMerchantStatus("yes");
                entityManager.flush();
            }
            otherBankAccountSessionBeanLocal.updateOtherBankAccountBalanceById(merchantAccountIds.get(1), creditAmt);
            Long otherTransactionId2 = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, "CTN", "Transfer from citiBank to ntuc", "", String.valueOf(creditAmt), merchantAccountIds.get(1));
        }

        //sephora
        Query q3 = entityManager.createQuery("select m from MasterCardClearingNetwork m where m.merchantName=:merchant AND m.payMerchantStatus=:status");
        q3.setParameter("merchant", "sephora");
        q3.setParameter("status", "no");

        if (!q3.getResultList().isEmpty()) {
            masterCardRecords = q3.getResultList();
            for (int i = 0; i < masterCardRecords.size(); i++) {
                creditAmt += masterCardRecords.get(i).getTransactionAmt();
                masterCardRecords.get(i).setPayMerchantStatus("yes");
                entityManager.flush();
            }
            otherBankAccountSessionBeanLocal.updateOtherBankAccountBalanceById(merchantAccountIds.get(2), creditAmt);
            Long otherTransactionId3 = otherTransactionSessionBeanLocal.addNewOtherTransaction(transactionDate, "CTS", "Transfer from citiBank to sephora", "", String.valueOf(creditAmt), merchantAccountIds.get(2));
        }
    }

    private BankAccount retrieveBankAccountByNum(java.lang.String bankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveBankAccountByNum(bankAccountNum);
    }
}
