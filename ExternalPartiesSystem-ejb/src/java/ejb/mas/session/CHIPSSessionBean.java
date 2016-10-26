package ejb.mas.session;

import ejb.mas.entity.CHIPS;
import ejb.mas.entity.SWIFT;
import ejb.mas.entity.SWIFTCode;
import ejb.otherbanks.session.OnHoldSessionBeanLocal;
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
import ws.client.merlionBank.MerlionBankWebService_Service;

@Stateless
public class CHIPSSessionBean implements CHIPSSessionBeanLocal {
    
    @EJB
    private SettlementSessionBeanLocal settlementSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private OnHoldSessionBeanLocal onHoldSessionBeanLocal;

    @EJB
    private SWIFTCodeSessionBeanLocal sWIFTCodeSessionBeanLocal;

    @EJB
    private SWIFTSessionBeanLocal sWIFTSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewCHIPS(Double otherTotalCredit, Double merlionTotalCredit,
            String currentTime, String bankNames, String paymentMethod, String creditAccountNum,
            String creditBank, String debitAccountNum, String debitBank, Long currentTimeMilis,
            Double creditAmt) {

        CHIPS chips = new CHIPS();

        chips.setBankBTotalCredit(otherTotalCredit);
        chips.setBankATotalCredit(merlionTotalCredit);
        chips.setCurrentTime(currentTime);
        chips.setBankNames(bankNames);
        chips.setPaymentMethod(paymentMethod);
        chips.setCreditAccountNum(creditAccountNum);
        chips.setCreditBank(creditBank);
        chips.setDebitAccountNum(debitAccountNum);
        chips.setDebitBank(debitBank);
        chips.setCurrentTimeMilis(currentTimeMilis);
        chips.setCreditAmt(creditAmt);

        entityManager.persist(chips);
        entityManager.flush();

        return chips.getChipsId();
    }

    @Override
    public CHIPS retrieveCHIPSById(Long chipsId) {
        CHIPS chips = new CHIPS();

        try {
            Query query = entityManager.createQuery("Select c From CHIPS c Where c.chipsId=:chipsId");
            query.setParameter("chipsId", chipsId);

            if (query.getResultList().isEmpty()) {
                return new CHIPS();
            } else {
                chips = (CHIPS) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new CHIPS();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return chips;
    }

    @Override
    public void clearSWIFTTransferMTK(Long swiftId) {
        SWIFT swift = sWIFTSessionBeanLocal.retrieveSWIFTById(swiftId);

        Double transactionAmt = Double.valueOf(swift.getPaymentAmt());
        String receivedSWIFTCode = swift.getOrganizationB();
        SWIFTCode receivedSwiftCode = sWIFTCodeSessionBeanLocal.retrieveSWIFTBySWIFTCode(receivedSWIFTCode);
        String receivedBankAccountNum = receivedSwiftCode.getBankAccountNum();

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "Merlion&BankofKorea";
        String paymentMethod = "SWIFT";

        Long chipsId = addNewCHIPS(0.0, 0.0, currentTime, bankNames, paymentMethod, "22222222",
                "Bank of Korea", receivedBankAccountNum, "Merlion", cal.getTimeInMillis(), transactionAmt);

        CHIPS chips = retrieveCHIPSById(chipsId);

        Double koreaTotalCredit = 0 + transactionAmt;
        Double merlionTotalCredit = 0 - transactionAmt;

        chips.setBankBTotalCredit(koreaTotalCredit);
        chips.setBankATotalCredit(merlionTotalCredit);

        onHoldSessionBeanLocal.addNewRecord("Bank of Korea", "22222222",
                "Credit", transactionAmt.toString(), "New", "Merlion",
                receivedBankAccountNum, "SWIFT");
        addNewRecord("Merlion", receivedBankAccountNum,
                "Debit", transactionAmt.toString(), "New", "Bank of Korea",
                "22222222", "SWIFT");
    }
    
    @Override
    public void ForwardPaymentInstructionToMEPS() {

        Calendar cal = Calendar.getInstance();
        Long startTime = cal.getTimeInMillis() - 10000;
        Long endTime = cal.getTimeInMillis();

        Query query = entityManager.createQuery("SELECT c FROM CHIPS c WHERE c.currentTimeMilis >= :startTime And c.currentTimeMilis<=:endTime And c.paymentMethod<>:paymentMethod");
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.setParameter("paymentMethod", "FAST");
        List<CHIPS> chips = query.getResultList();

        if (!chips.isEmpty()) {
            settlementSessionBeanLocal.recordSettlementInformationCHIPS(chips);
        }
    }
    
    @Override
    public List<CHIPS> getAllCHIPS(String bankNames) {

        Query query = entityManager.createQuery("SELECT c FROM CHIPS c Where c.bankNames=:bankNames");
        query.setParameter("bankNames", bankNames);

        return query.getResultList();
    }

    private Long addNewRecord(java.lang.String bankName, java.lang.String bankAccountNum, java.lang.String debitOrCredit, java.lang.String paymentAmt, java.lang.String onHoldStatus, java.lang.String debitOrCreditBankName, java.lang.String debitOrCreditBankAccountNum, java.lang.String paymentMethod) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        return port.addNewRecord(bankName, bankAccountNum, debitOrCredit, paymentAmt, onHoldStatus, debitOrCreditBankName, debitOrCreditBankAccountNum, paymentMethod);
    }
}
