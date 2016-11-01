/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.mas;

import ejb.mas.entity.MEPS;
import ejb.mas.session.MEPSCardSettlementHistorySessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "mEPSCitiAndMasterCardManagedBean")
@RequestScoped
public class MEPSCitiAndMasterCardHistoryManagedBean {

    /**
     * Creates a new instance of MEPSCitiAndMasterCardManagedBean
     */
    @EJB
    private MEPSCardSettlementHistorySessionBeanLocal mEPSCardSettlementHistorySessionBeanLocal;

    private List<MEPS> settlements = new ArrayList<>();

    public MEPSCitiAndMasterCardHistoryManagedBean() {
    }

    public List<MEPS> getAllMEPSCitiAndMasterCardHistory() {
        settlements = mEPSCardSettlementHistorySessionBeanLocal.getAllCitiAndMasterCardSettlements();
        return settlements;
    }
}
