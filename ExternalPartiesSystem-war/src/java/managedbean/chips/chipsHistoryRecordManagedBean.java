package managedbean.chips;

import ejb.mas.entity.CHIPS;
import ejb.mas.entity.Settlement;
import ejb.mas.session.CHIPSSessionBeanLocal;
import ejb.mas.session.SettlementSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "chipsHistoryRecordManagedBean")
@RequestScoped

public class chipsHistoryRecordManagedBean {

    @EJB
    private SettlementSessionBeanLocal settlementSessionBeanLocal;

    @EJB
    private CHIPSSessionBeanLocal cHIPSSessionBeanLocal;

    private ExternalContext ec;

    public chipsHistoryRecordManagedBean() {
    }

    public List<CHIPS> getDbsAndMerlionCHIPS() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<CHIPS> chips = cHIPSSessionBeanLocal.getAllCHIPS("Merlion&BankofKorea");

        return chips;
    }

    public List<Settlement> getAllSettlement() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        List<Settlement> settlements = settlementSessionBeanLocal.getAllSettlement();

        return settlements;
    }
}
