package managedbean.mas;

import ejb.mas.entity.MEPSMasterAccountTransaction;
import ejb.mas.session.MEPSMasterAccountTransactionSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "dBSMasterAccountTransactionManagedBean")
@RequestScoped

public class DBSMasterAccountTransactionManagedBean implements Serializable {

    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    private ExternalContext ec;

    public DBSMasterAccountTransactionManagedBean() {
    }

    public List<MEPSMasterAccountTransaction> getDbsMasterAccountTransactions() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<MEPSMasterAccountTransaction> dbsMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("44332211");

        return dbsMasterAccountTransactions;
    }
}
