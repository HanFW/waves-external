package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankAccount;
import java.util.ArrayList;
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
    public List<OtherBankAccount> getAllBankOfKoreaBankAccount() {
        Query query = entityManager.createQuery("SELECT o FROM OtherBankAccount o Where o.bankName=:bankName");
        query.setParameter("bankName", "Bank of Korea");

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

    @Override
    public void updateAvailableAccountBalance(String otherBankAccountNum, String availableBankAccountBalance) {
        OtherBankAccount otherBankAccount = retrieveBankAccountByNum(otherBankAccountNum);
        otherBankAccount.setAvailableBankAccountBalance(availableBankAccountBalance);
    }

    @Override
    public List<OtherBankAccount> getAllCitiBankMerchantAccount() {
        Query query = entityManager.createQuery("SELECT o FROM OtherBankAccount o Where o.bankName=:bankName");
        query.setParameter("bankName", "citi");

        return query.getResultList();
    }

    @Override
    public List<Long> getAllCitiBankMerchantAccountId() {
        List<OtherBankAccount> merchantAccounts = getAllCitiBankMerchantAccount();
        List<Long> merchantIds = new ArrayList<>();

        for (int i = 0; i < merchantAccounts.size(); i++) {
            merchantIds.add(merchantAccounts.get(i).getOtherBankAccountId());
        }
        return merchantIds;
    }

    @Override
    public void updateOtherBankAccountBalanceById(Long otherBankAccountId, double transactionAmt) {
        OtherBankAccount otherBankAccount = entityManager.find(OtherBankAccount.class, otherBankAccountId);

        Double currentAvailableBankAccountBalance = Double.valueOf(otherBankAccount.getAvailableBankAccountBalance()) + transactionAmt;
        otherBankAccount.setAvailableBankAccountBalance(String.valueOf(currentAvailableBankAccountBalance));

        Double totalBankAccountBalance = Double.valueOf(otherBankAccount.getTotalBankAccountBalance()) + transactionAmt;
        otherBankAccount.setTotalBankAccountBalance(String.valueOf(totalBankAccountBalance));
    }
}
