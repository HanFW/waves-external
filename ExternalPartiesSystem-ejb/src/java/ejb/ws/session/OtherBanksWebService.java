package ejb.ws.session;

import ejb.otherbanks.entity.OtherBankAccount;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@WebService(serviceName = "OtherBanksWebService")
@Stateless()

public class OtherBanksWebService {
    @PersistenceContext(unitName = "ExternalPartiesSystem-ejbPU")
    private EntityManager entityManager;

    @WebMethod(operationName = "retrieveBankAccountByNum")
    public OtherBankAccount retrieveBankAccountByNum(@WebParam(name = "otherBankAccountNum") String otherBankAccountNum) {
        
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
        
        entityManager.detach(otherBankAccount);
        otherBankAccount.setOtherBankAccountTransaction(null);

        return otherBankAccount;
    }
}
