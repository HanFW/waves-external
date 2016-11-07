/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.clearing;

import ejb.card.entity.VisaClearingNetwork;
import ejb.card.session.VisaNetworkClearingSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author qinglai
 */
@Named(value = "simulateVisaClearingNetworkManagedBean")
@RequestScoped
public class SimulateVisaClearingNetworkManagedBean implements Serializable{

    /**
     * Creates a new instance of SimulateVisaClearingNetworkManagedBean
     */
    @EJB
    private VisaNetworkClearingSessionBeanLocal visaNetworkClearingSessionBeanLocal;
    
    private List<VisaClearingNetwork> visaClearingRecords = new ArrayList<VisaClearingNetwork>();
    private double totalCreditAmt;
    
    public SimulateVisaClearingNetworkManagedBean() {
    }

    public List<VisaClearingNetwork> getVisaClearingRecords() {
        visaClearingRecords=visaNetworkClearingSessionBeanLocal.getAllVisaRecords();
        return visaClearingRecords;
    }

    public void setVisaClearingRecords(List<VisaClearingNetwork> visaClearingRecords) {
        this.visaClearingRecords = visaClearingRecords;
    }
    
    public double getTotalCreditAmt(){
        totalCreditAmt=visaNetworkClearingSessionBeanLocal.getTotalCreditAmt();
        return totalCreditAmt;
    }
    
    
}
