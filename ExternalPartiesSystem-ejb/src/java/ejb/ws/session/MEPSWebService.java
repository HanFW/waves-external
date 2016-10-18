package ejb.ws.session;

import ejb.mas.entity.MEPSMasterBankAccount;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@WebService(serviceName = "MEPSWebService")
@Stateless()

public class MEPSWebService {
    @PersistenceContext(unitName = "ExternalPartiesSystem-ejbPU")
    private EntityManager entityManager;

    @WebMethod(operationName = "maintainDailyBalance")
    @Oneway
    public void maintainDailyBalance() {
        
        Query query = entityManager.createQuery("SELECT m FROM MEPSMasterBankAccount m");
        List<MEPSMasterBankAccount> mepsMasterBankAccounts = query.getResultList();

        for (MEPSMasterBankAccount mepsMasterBankAccount : mepsMasterBankAccounts) {

            String currentBalance = mepsMasterBankAccount.getMasterBankAccountBalance();
            Double currentBalanceDouble = Double.valueOf(currentBalance);

            if (currentBalanceDouble < 100000) {
                Double diffBalance = 100000 - Double.valueOf(currentBalance);
                Double totalBalance = Double.valueOf(currentBalance) + diffBalance;

                mepsMasterBankAccount.setMasterBankAccountBalance(totalBalance.toString());
            }
        }
    }
}
