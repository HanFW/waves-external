package ejb.mas.session;

import ejb.billingprocessor.entity.Bill;
import ejb.billingprocessor.session.BillSessionBeanLocal;
import ejb.mas.entity.SACH;
import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.entity.OtherBankCheque;
import ejb.otherbanks.session.OnHoldSessionBeanLocal;
import ejb.otherbanks.session.OtherBankAccountSessionBeanLocal;
import ejb.otherbanks.session.OtherBankChequeSessionBeanLocal;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.fastTransfer.FastTransferWebService_Service;
import ws.client.merlionBank.BankAccount;
import ws.client.merlionBank.MerlionBankWebService_Service;
import ws.client.merlionBank.ReceivedCheque;

@Stateless
public class SACHSessionBean implements SACHSessionBeanLocal {

    @EJB
    private OtherBankChequeSessionBeanLocal otherBankChequeSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private OnHoldSessionBeanLocal onHoldSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/FastTransferWebService/FastTransferWebService.wsdl")
    private FastTransferWebService_Service service_fastTransfer;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @EJB
    private SettlementSessionBeanLocal settlementSessionBeanLocal;

    @EJB
    private MEPSSessionBeanLocal mEPSSessionBeanLocal;

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @EJB
    private BillSessionBeanLocal billSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void SACHTransferMTD(String fromBankAccount, String toBankAccount, Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = addNewSACH(0.0, 0.0, currentTime, "DBS&Merlion", "FAST",
                toBankAccount, "DBS", fromBankAccount, "Merlion", currentTimeMilis, transferAmt,
                "Successful");
        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transferAmt;
        Double merlionTotalCredit = 0 - transferAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        otherBankSessionBeanLocal.actualMTOFastTransfer(fromBankAccount, toBankAccount, transferAmt);
        mEPSSessionBeanLocal.MEPSSettlementMTD("88776655", "44332211", transferAmt);
    }

    @Override
    public SACH retrieveSACHById(Long sachId) {
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

    @Override
    public Long addNewSACH(Double otherTotalCredit, Double merlionTotalCredit,
            String currentTime, String bankNames, String paymentMethod, String creditAccountNum,
            String creditBank, String debitAccountNum, String debitBank, Long currentTimeMilis,
            Double creditAmt, String sachStatus) {

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

    @Override
    public List<SACH> getAllSACH(String bankNames) {

        Query query = entityManager.createQuery("SELECT s FROM SACH s Where s.bankNames=:bankNames");
        query.setParameter("bankNames", bankNames);

        return query.getResultList();
    }

    @Override
    public void SACHTransferDTM(String fromBankAccount, String toBankAccount, Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = addNewSACH(0.0, 0.0, currentTime, "DBS&Merlion", "FAST", toBankAccount,
                "Merlion", fromBankAccount, "DBS", currentTimeMilis, transferAmt, "Successful");
        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 - transferAmt;
        Double merlionTotalCredit = 0 + transferAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        actualOTMFastTransfer(fromBankAccount, toBankAccount, transferAmt);
        mEPSSessionBeanLocal.MEPSSettlementDTM("44332211", "88776655", transferAmt);
    }

    @Override
    public void ForwardPaymentInstructionToMEPS() {

        Calendar cal = Calendar.getInstance();
        Long startTime = cal.getTimeInMillis() - 10000;
        Long endTime = cal.getTimeInMillis();

        Query query = entityManager.createQuery("SELECT s FROM SACH s WHERE s.currentTimeMilis >= :startTime And s.currentTimeMilis<=:endTime And s.paymentMethod<>:paymentMethod And s.sachStatus=:sachStatus");
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.setParameter("paymentMethod", "FAST");
        query.setParameter("sachStatus", "Successful");
        List<SACH> sachs = query.getResultList();

        if (!sachs.isEmpty()) {
            settlementSessionBeanLocal.recordSettlementInformation(sachs);
        }
    }

    @Override
    public String ntucInitiateGIRO(Long billId) {

        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);

        String debitBank = bill.getDebitBank();
        String debitBankAccountNum = bill.getDebitBankAccountNum();
        Double paymentAmt = Double.valueOf(bill.getPaymentAmt());
        String bankNames = "";
        String paymentMethod = "";
        String paymentLimit = bill.getPaymentLimit();
        Double paymentLimitDouble = Double.valueOf(paymentLimit);

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        if (paymentLimitDouble < paymentAmt) {
            return "Exceed Payment Limit";
        } else {

            if (debitBank.equals("Merlion")) {

                BankAccount bankAccount = retrieveBankAccountByNum(debitBankAccountNum);
                OtherBankAccount dbsBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum("12345678");
                bankNames = "DBS&Merlion";
                paymentMethod = "Standing GIRO";

                updateAvailableBalance(debitBankAccountNum, paymentAmt);

                Long sachId = addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod, dbsBankAccount.getOtherBankAccountNum(), "DBS",
                        bankAccount.getBankAccountNum(), "Merlion", currentTimeMilis, paymentAmt, "Successful");

                SACH sach = retrieveSACHById(sachId);

                Double dbsTotalCredit = 0 + paymentAmt;
                Double merlionTotalCredit = 0 - paymentAmt;

                sach.setBankBTotalCredit(dbsTotalCredit);
                sach.setBankATotalCredit(merlionTotalCredit);

                onHoldSessionBeanLocal.addNewRecord("DBS", "12345678",
                        "Credit", paymentAmt.toString(), "New", "Merlion",
                        debitBankAccountNum, "Non Standing GIRO");
                addNewRecord("Merlion", debitBankAccountNum,
                        "Debit", paymentAmt.toString(), "New", "DBS",
                        "12345678", "Non Standing GIRO");
            }
        }

        return "Payment Approved";
    }

    @Override
    public void clearMerlionReceivedCheque(String chequeNum) {

        ReceivedCheque receivedCheque = retrieveReceivedChequeByNum(chequeNum);

        Double transactionAmt = Double.valueOf(receivedCheque.getTransactionAmt());
        String receivedBankAccountNum = receivedCheque.getReceivedBankAccountNum();
        String issuedBankAccountNum = receivedCheque.getOtherBankAccountNum();

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";
        String paymentMethod = "Cheque";

        Long sachId = addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod, receivedBankAccountNum,
                "Merlion", issuedBankAccountNum, "DBS", cal.getTimeInMillis(), transactionAmt, "Successful");
        SACH sach = retrieveSACHById(sachId);

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

    @Override
    public void clearDBSReceivedCheque(String chequeNum) {

        OtherBankCheque otherBankCheque = otherBankChequeSessionBeanLocal.retrieveReceivedChequeByNum(chequeNum);

        Double transactionAmt = Double.valueOf(otherBankCheque.getTransactionAmt());
        String receivedBankAccountNum = otherBankCheque.getReceivedBankAccountNum();
        String issuedBankAccountNum = otherBankCheque.getIssuedBankAccountNum();

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";
        String paymentMethod = "Cheque";

        Long sachId = addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod, receivedBankAccountNum,
                "DBS", issuedBankAccountNum, "Merlion", cal.getTimeInMillis(), transactionAmt, "Successful");
        SACH sach = retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transactionAmt;
        Double merlionTotalCredit = 0 - transactionAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        Long otherAccountOnHoldId = onHoldSessionBeanLocal.addNewRecord("DBS", receivedBankAccountNum,
                "Credit", transactionAmt.toString(), "New", "Merlion",
                issuedBankAccountNum, "Cheque");
        Long bankAccountOnHoldId = addNewRecord("Merlion", issuedBankAccountNum,
                "Debit", transactionAmt.toString(), "New", "DBS",
                receivedBankAccountNum, "Cheque");

        updateOnHoldChequeNum(bankAccountOnHoldId, chequeNum);
    }

    @Override
    public void receiveChequeInformationFromOtherBank(String chequeNum,
            String transactionAmt, String bankAccountNum) {
        passChequeInformationToMerlion(chequeNum, transactionAmt, bankAccountNum);
    }

    @Override
    public void rejectStandingGIRO(String billReference, String creditBankAccountNum,
            String debitBankAccountNum) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";
        String paymentMethod = "Standing GIRO";
        String failedReason = "Invalid Bill Reference Number";

        Long sachId = addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod, creditBankAccountNum, "DBS",
                debitBankAccountNum, "Merlion", cal.getTimeInMillis(), 0.0, "Failed");

        SACH sach = retrieveSACHById(sachId);
        sach.setFailedReason(failedReason);

        rejectStandingGIROTransaction(billReference, creditBankAccountNum, debitBankAccountNum);
    }

    @Override
    public void rejectNonStandingGIRO(String billReference, String creditBankAccountNum,
            String debitBankAccountNum) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";
        String paymentMethod = "Non Standing GIRO";
        String failedReason = "Invalid Bill Reference Number";

        Long sachId = addNewSACH(0.0, 0.0, currentTime, bankNames, paymentMethod,
                creditBankAccountNum, "DBS", debitBankAccountNum, "Merlion",
                cal.getTimeInMillis(), 0.0, "Failed");

        SACH sach = retrieveSACHById(sachId);
        sach.setFailedReason(failedReason);

        rejectNonStandingGIROTransaction(billReference, creditBankAccountNum, debitBankAccountNum);
    }

    @Override
    public void approveNonStandingGIRO(String billReference) {
        approveNonStandingGIROTransaction(billReference);
    }

    private void actualOTMFastTransfer(java.lang.String fromBankAccountNum, java.lang.String toBankAccountNum, java.lang.Double transferAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.fastTransfer.FastTransferWebService port = service_fastTransfer.getFastTransferWebServicePort();
        port.actualOTMFastTransfer(fromBankAccountNum, toBankAccountNum, transferAmt);
    }

    private BankAccount retrieveBankAccountByNum(java.lang.String bankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.retrieveBankAccountByNum(bankAccountNum);
    }

    private void updateAvailableBalance(java.lang.String bankAccountNum, java.lang.Double paymentAmt) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.updateAvailableBalance(bankAccountNum, paymentAmt);
    }

    private Long addNewRecord(java.lang.String bankName, java.lang.String bankAccountNum, java.lang.String debitOrCredit, java.lang.String paymentAmt, java.lang.String onHoldStatus, java.lang.String debitOrCreditBankName, java.lang.String debitOrCreditBankAccountNum, java.lang.String paymentMethod) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.addNewRecord(bankName, bankAccountNum, debitOrCredit, paymentAmt, onHoldStatus, debitOrCreditBankName, debitOrCreditBankAccountNum, paymentMethod);
    }

    private void passChequeInformationToMerlion(java.lang.String chequeNum, java.lang.String transactionAmt, java.lang.String bankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.receiveChequeInformationFromSACH(chequeNum, transactionAmt, bankAccountNum);
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

    private void rejectStandingGIROTransaction(java.lang.String billReference, java.lang.String creditBankAccountNum, java.lang.String debitBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.rejectStandingGIROTransaction(billReference, creditBankAccountNum, debitBankAccountNum);
    }

    private void rejectNonStandingGIROTransaction(java.lang.String billReference, java.lang.String creditBankAccountNum, java.lang.String debitBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.rejectNonStandingGIROTransaction(billReference, creditBankAccountNum, debitBankAccountNum);
    }

    private void approveNonStandingGIROTransaction(java.lang.String billReference) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.approveNonStandingGIROTransaction(billReference);
    }
}
