package managedbean.otherbanks;

import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.session.OtherBankAccountSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "dBSViewAccountManagedBean")
@RequestScoped

public class DBSViewAccountManagedBean implements Serializable {

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    public DBSViewAccountManagedBean() {
    }

    public List<OtherBankAccount> getAllDBSBankAccounts() throws IOException {

        List<OtherBankAccount> dbsBankAccounts = otherBankAccountSessionBeanLocal.getAllDBSBankAccount();

        return dbsBankAccounts;
    }

}
