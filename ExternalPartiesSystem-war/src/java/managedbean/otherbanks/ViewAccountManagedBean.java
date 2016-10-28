package managedbean.otherbanks;

import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.session.OtherBankAccountSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "viewAccountManagedBean")
@RequestScoped

public class ViewAccountManagedBean {

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    public ViewAccountManagedBean() {
    }

    public List<OtherBankAccount> getAllKoreaBankAccounts() throws IOException {

        List<OtherBankAccount> koreaBankAccounts = otherBankAccountSessionBeanLocal.getAllBankOfKoreaBankAccount();

        return koreaBankAccounts;
    }
}
