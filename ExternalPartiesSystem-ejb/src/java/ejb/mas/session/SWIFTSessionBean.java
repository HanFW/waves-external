package ejb.mas.session;

import ejb.mas.entity.SWIFT;
import ejb.mas.entity.SWIFTCode;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SWIFTSessionBean implements SWIFTSessionBeanLocal {

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @EJB
    private SWIFTCodeSessionBeanLocal sWIFTCodeSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SWIFT> getAllSWIFT() {
        Query query = entityManager.createQuery("SELECT s FROM SWIFT s");
        return query.getResultList();
    }

    @Override
    public SWIFT retrieveSWIFTById(Long swiftId) {
        SWIFT swift = new SWIFT();

        try {
            Query query = entityManager.createQuery("Select s From SWIFT s Where s.swiftId=:swiftId");
            query.setParameter("swiftId", swiftId);

            if (query.getResultList().isEmpty()) {
                return new SWIFT();
            } else {
                swift = (SWIFT) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SWIFT();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return swift;
    }

    @Override
    public void sendMessageToReceivedInstitution(Long swiftId) {

        SWIFT swift = retrieveSWIFTById(swiftId);

        String swiftCodeB = swift.getSwiftCodeB();
        String swiftCodeA = swift.getSwiftCodeA();
        SWIFTCode receivedSWIFTCode = sWIFTCodeSessionBeanLocal.retrieveSWIFTBySWIFTCode(swiftCodeB);
        String receivedInstitution = receivedSWIFTCode.getRecipient();
        String paymentAmt = swift.getPaymentAmt();
        SWIFTCode transferedSWIFTCode = sWIFTCodeSessionBeanLocal.retrieveSWIFTBySWIFTCode(swiftCodeA);
        String receivedBankAccountNum = receivedSWIFTCode.getBankAccountNum();
        String transferedBankAccountNum = transferedSWIFTCode.getBankAccountNum();

        if (receivedInstitution.equals("Bank of Korea")) {
            otherBankSessionBeanLocal.creditPayementToAccountMTK(transferedBankAccountNum,
                    receivedBankAccountNum, Double.valueOf(paymentAmt));
        }
    }
}
