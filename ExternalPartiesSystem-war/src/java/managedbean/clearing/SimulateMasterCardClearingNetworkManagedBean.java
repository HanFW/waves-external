/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.clearing;

import ejb.card.entity.MasterCardClearingNetwork;
import ejb.card.session.MasterCardNetworkClearingSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "simulateMasterCardClearingNetworkManagedBean")
@RequestScoped
public class SimulateMasterCardClearingNetworkManagedBean {

    /**
     * Creates a new instance of SimulateMasterCardClearingNetworkManagedBean
     */
    
    @EJB
    private MasterCardNetworkClearingSessionBeanLocal masterCardNetworkClearingSessionBeanLocal;
    
    private List<MasterCardClearingNetwork> masterCardClearingRecords = new ArrayList<>();
    private double totalCreditAmt;
    
    public SimulateMasterCardClearingNetworkManagedBean() {
    }
    
    public List<MasterCardClearingNetwork> getMasterCardClearingRecords() {
        masterCardClearingRecords=masterCardNetworkClearingSessionBeanLocal.getAllMasterCardRecords();
        return masterCardClearingRecords;
    }

    public void setMasterCardClearingRecords(List<MasterCardClearingNetwork> masterCardClearingRecords) {
        this.masterCardClearingRecords = masterCardClearingRecords;
    }
    
    public double getTotalCreditAmt(){
        totalCreditAmt=masterCardNetworkClearingSessionBeanLocal.getTotalCreditAmt();
        return totalCreditAmt;
    }
    
}
