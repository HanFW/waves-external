package ejb.mas.session;

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
    private SACHSessionBeanLocal sACHSessionBean;

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
            String updateDate, String bankNames) {
        Settlement settlement = new Settlement();

        settlement.setBankNames(bankNames);
        settlement.setDailySettlementAmt(dailySettlementAmt);
        settlement.setDailySettlementRef(dailySettlementRef);
        settlement.setUpdateDate(updateDate);

        entityManager.persist(settlement);
        entityManager.flush();

        return settlement.getSettlementId();
    }

    @Override
    public void recordSettlementInformation(List<SACH> sachs, String creditBank, String debitBank) {

        Double dailySettlementAmt = 0.0;
        String dailySettlementRef = "";

        for (int i = 0; i < sachs.size(); i++) {
            dailySettlementAmt = dailySettlementAmt + Double.valueOf(sachs.get(i).getBankATotalCredit());
        }

        if (creditBank.equals("DBS") && debitBank.equals("Merlion")) {
            if (dailySettlementAmt < 0) {
                dailySettlementRef = "Merlion Bank has to pay DBS S$" + dailySettlementAmt * (-1);
            } else if (dailySettlementAmt > 0) {
                dailySettlementRef = "DBS has to pay Merlion Bank S$" + dailySettlementAmt;
            } else {
                dailySettlementRef = "Not Applicable";
            }
        }

        Calendar cal = Calendar.getInstance();
        String updateDate = cal.getTime().toString();
        Double dailySettlementAmtPos = dailySettlementAmt * (-1);

        Long settlementId = addNewSettlement(dailySettlementAmtPos.toString(), dailySettlementRef,
                updateDate, "DBS&Merlion");

    }

    @Override
    public List<Settlement> getAllSettlement() {

        Query query = entityManager.createQuery("SELECT s FROM Settlement s");

        return query.getResultList();
    }
}
