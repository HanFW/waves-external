package managedbean.mas;

import ejb.mas.entity.MEPSMasterAccountTransaction;
import ejb.mas.session.MEPSMasterAccountTransactionSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "koreaMasterAccountTransactionManagedBean")
@RequestScoped

public class KoreaMasterAccountTransactionManagedBean {

    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;
    
    private ExternalContext ec;
    
    public KoreaMasterAccountTransactionManagedBean() {
    }
    
    public List<MEPSMasterAccountTransaction> getKoreaMasterAccountTransactions() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<MEPSMasterAccountTransaction> koreaMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("11335577");

        return koreaMasterAccountTransactions;
    }
}
