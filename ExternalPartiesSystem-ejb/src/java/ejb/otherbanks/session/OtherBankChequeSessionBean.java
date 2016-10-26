package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankCheque;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class OtherBankChequeSessionBean implements OtherBankChequeSessionBeanLocal {

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewReceivedCheque(String transactionDate, String transactionAmt,
            String receivedBankAccountNum, String receivedCustomerName, String receivedCustomerMobile,
            String issuedBankAccountNum, String otherBankAccountNum) {

        OtherBankCheque otherBankCheque = new OtherBankCheque();

        otherBankCheque.setReceivedBankAccountNum(receivedBankAccountNum);
        otherBankCheque.setReceivedCustomerMobile(receivedCustomerMobile);
        otherBankCheque.setReceivedCustomerName(receivedCustomerName);
        otherBankCheque.setTransactionAmt(transactionAmt);
        otherBankCheque.setTransactionDate(transactionDate);
        otherBankCheque.setIssuedBankAccountNum(issuedBankAccountNum);
        otherBankCheque.setOtherBankAccount(otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(otherBankAccountNum));

        entityManager.persist(otherBankCheque);
        entityManager.flush();

        return otherBankCheque.getChequeId();
    }

    @Override
    public OtherBankCheque retrieveReceivedChequeById(Long chequeId) {
        OtherBankCheque otherBankCheque = new OtherBankCheque();

        try {
            Query query = entityManager.createQuery("Select o From OtherBankCheque o Where o.chequeId=:chequeId");
            query.setParameter("chequeId", chequeId);

            if (query.getResultList().isEmpty()) {
                return new OtherBankCheque();
            } else {
                otherBankCheque = (OtherBankCheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OtherBankCheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return otherBankCheque;
    }
}
