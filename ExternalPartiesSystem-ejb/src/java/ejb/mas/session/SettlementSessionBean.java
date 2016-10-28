package ejb.mas.session;

import ejb.chips.entity.CHIPS;
import ejb.mas.entity.MEPSMasterBankAccount;
import ejb.mas.entity.SACH;
import ejb.mas.entity.Settlement;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SettlementSessionBean implements SettlementSessionBeanLocal {

    @EJB
    private MEPSMasterBankAccountSessionBeanLocal mEPSMasterBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Settlement retrieveSettlementById(Long settlementId) {
        Settlement settlement = new Settlement();

        try {
            Query query = entityManager.createQuery("Select s From Settlement s Where s.settlementId=:settlementId");
            query.setParameter("settlementId", settlementId);

            if (query.getResultList().isEmpty()) {
                return new Settlement();
            } else {
                settlement = (Settlement) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Settlement();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return settlement;
    }

    @Override
    public Long addNewSettlement(String dailySettlementAmt, String dailySettlementRef,
            String updateDate, String bankNames, String settlementStatus, String creditMEPSBank,
            String creditMEPSBankAccountNum, String debitMEPSBank, String debitMEPSBankAccountNum,
            String clearanceSystem) {
        Settlement settlement = new Settlement();

        settlement.setBankNames(bankNames);
        settlement.setDailySettlementAmt(dailySettlementAmt);
        settlement.setDailySettlementRef(dailySettlementRef);
        settlement.setUpdateDate(updateDate);
        settlement.setSettlementStatus(settlementStatus);
        settlement.setCreditMEPSBankAccountNum(creditMEPSBankAccountNum);
        settlement.setDebitMEPSBankAccountNum(debitMEPSBankAccountNum);
        settlement.setClearanceSystem(clearanceSystem);

        entityManager.persist(settlement);
        entityManager.flush();

        return settlement.getSettlementId();
    }

    @Override
    public void recordSettlementInformation(List<SACH> sachs) {

        Double dailySettlementAmt = 0.0;
        String dailySettlementRef = "";
        String creditMEPSBank = "";
        String creditMEPSBankAccountNum = "";
        String debitMEPSBank = "";
        String debitMEPSBankAccountNum = "";

        for (int i = 0; i < sachs.size(); i++) {
            dailySettlementAmt = dailySettlementAmt + Double.valueOf(sachs.get(i).getBankATotalCredit());
        }

        if (dailySettlementAmt < 0) {

            dailySettlementRef = "Merlion Bank has to pay DBS S$" + dailySettlementAmt * (-1);
            creditMEPSBank = "DBS";
            debitMEPSBank = "Merlion";

            MEPSMasterBankAccount dbsMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("DBS");
            MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("Merlion");

            creditMEPSBankAccountNum = dbsMasterBankAccount.getMasterBankAccountNum();
            debitMEPSBankAccountNum = merlionMasterBankAccount.getMasterBankAccountNum();

            Calendar cal = Calendar.getInstance();
            String updateDate = cal.getTime().toString();
            Double dailySettlementAmtPos = dailySettlementAmt * (-1);

            Long settlementId = addNewSettlement(dailySettlementAmtPos.toString(), dailySettlementRef,
                    updateDate, "DBS&Merlion", "New", creditMEPSBank, creditMEPSBankAccountNum,
                    debitMEPSBank, debitMEPSBankAccountNum, "SACH");

        } else if (dailySettlementAmt > 0) {

            dailySettlementRef = "DBS has to pay Merlion Bank S$" + dailySettlementAmt * (-1);
            creditMEPSBank = "Merlion";
            debitMEPSBank = "DBS";

            MEPSMasterBankAccount dbsMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("DBS");
            MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("Merlion");

            debitMEPSBankAccountNum = dbsMasterBankAccount.getMasterBankAccountNum();
            creditMEPSBankAccountNum = merlionMasterBankAccount.getMasterBankAccountNum();

            Calendar cal = Calendar.getInstance();
            String updateDate = cal.getTime().toString();
            Double dailySettlementAmtPos = dailySettlementAmt * (-1);

            Long settlementId = addNewSettlement(dailySettlementAmtPos.toString(), dailySettlementRef,
                    updateDate, "DBS&Merlion", "New", creditMEPSBank, creditMEPSBankAccountNum,
                    debitMEPSBank, debitMEPSBankAccountNum, "SACH");

        } else {
            dailySettlementRef = "Not Applicable";
        }
    }

    @Override
    public void recordSettlementInformationCHIPS(List<CHIPS> chips) {

        Double dailySettlementAmt = 0.0;
        String dailySettlementRef = "";
        String creditMEPSBank = "";
        String creditMEPSBankAccountNum = "";
        String debitMEPSBank = "";
        String debitMEPSBankAccountNum = "";

        for (int i = 0; i < chips.size(); i++) {
            dailySettlementAmt = dailySettlementAmt + Double.valueOf(chips.get(i).getBankATotalCredit());
        }

        if (dailySettlementAmt < 0) {
            dailySettlementRef = "Merlion Bank has to pay Bank of Korea S$" + dailySettlementAmt * (-1);
            creditMEPSBank = "Bank of Korea";
            debitMEPSBank = "Merlion";

            MEPSMasterBankAccount koreaMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("Bank of Korea");
            MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("Merlion");

            creditMEPSBankAccountNum = koreaMasterBankAccount.getMasterBankAccountNum();
            debitMEPSBankAccountNum = merlionMasterBankAccount.getMasterBankAccountNum();

            Calendar cal = Calendar.getInstance();
            String updateDate = cal.getTime().toString();
            Double dailySettlementAmtPos = dailySettlementAmt * (-1);

            Long settlementId = addNewSettlement(dailySettlementAmtPos.toString(), dailySettlementRef,
                    updateDate, "Merlion&BankofKorea", "New", creditMEPSBank, creditMEPSBankAccountNum,
                    debitMEPSBank, debitMEPSBankAccountNum, "CHIPS");

        } else if (dailySettlementAmt > 0) {
            dailySettlementRef = "Bank of Korea has to pay Merlion Bank S$" + dailySettlementAmt * (-1);
            creditMEPSBank = "Merlion";
            debitMEPSBank = "Bank of Korea";

            MEPSMasterBankAccount koreaMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("Bank of Korea");
            MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("Merlion");

            debitMEPSBankAccountNum = koreaMasterBankAccount.getMasterBankAccountNum();
            creditMEPSBankAccountNum = merlionMasterBankAccount.getMasterBankAccountNum();

            Calendar cal = Calendar.getInstance();
            String updateDate = cal.getTime().toString();
            Double dailySettlementAmtPos = dailySettlementAmt * (-1);

            Long settlementId = addNewSettlement(dailySettlementAmtPos.toString(), dailySettlementRef,
                    updateDate, "Merlion&BankofKorea", "New", creditMEPSBank, creditMEPSBankAccountNum,
                    debitMEPSBank, debitMEPSBankAccountNum, "CHIPS");

        } else {
            dailySettlementRef = "Not Applicable";
        }
    }

    @Override
    public List<Settlement> getAllSACHSettlement() {

        Query query = entityManager.createQuery("SELECT s FROM Settlement s Where s.clearanceSystem=:clearanceSystem");
        query.setParameter("clearanceSystem", "SACH");
        
        return query.getResultList();
    }
    
    @Override
    public List<Settlement> getAllCHIPSSettlement() {

        Query query = entityManager.createQuery("SELECT s FROM Settlement s Where s.clearanceSystem=:clearanceSystem");
        query.setParameter("clearanceSystem", "CHIPS");
        
        return query.getResultList();
    }
}
