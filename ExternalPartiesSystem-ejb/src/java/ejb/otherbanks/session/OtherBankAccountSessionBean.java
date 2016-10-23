package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class OtherBankAccountSessionBean implements OtherBankAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OtherBankAccount> getAllDBSBankAccount() {
        Query query = entityManager.createQuery("SELECT o FROM OtherBankAccount o Where o.bankName=:bankName");
        query.setParameter("bankName", "DBS");

        return query.getResultList();
    }

    @Override
    public OtherBankAccount retrieveBankAccountByNum(String otherBankAccountNum) {
        OtherBankAccount otherBankAccount = new OtherBankAccount();

        try {
            Query query = entityManager.createQuery("Select o From OtherBankAccount o Where o.otherBankAccountNum=:otherBankAccountNum");
            query.setParameter("otherBankAccountNum", otherBankAccountNum);

            if (query.getResultList().isEmpty()) {
                return new OtherBankAccount();
            } else {
                otherBankAccount = (OtherBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OtherBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return otherBankAccount;
    }

    @Override
    public OtherBankAccount retrieveBankAccountById(Long otherBankAccountId) {
        OtherBankAccount otherBankAccount = new OtherBankAccount();

        try {
            Query query = entityManager.createQuery("Select o From OtherBankAccount o Where o.otherBankAccountId=:otherBankAccountId");
            query.setParameter("otherBankAccountId", otherBankAccountId);

            if (query.getResultList().isEmpty()) {
                return new OtherBankAccount();
            } else {
                otherBankAccount = (OtherBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OtherBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return otherBankAccount;
    }

    @Override
    public void updateBankAccountBalance(String otherBankAccountNum, String availableBankAccountBalance,
            String totalBankAccountBalance) {
        OtherBankAccount otherBankAccount = retrieveBankAccountByNum(otherBankAccountNum);

        otherBankAccount.setAvailableBankAccountBalance(availableBankAccountBalance);
        otherBankAccount.setTotalBankAccountBalance(totalBankAccountBalance);
    }
}
