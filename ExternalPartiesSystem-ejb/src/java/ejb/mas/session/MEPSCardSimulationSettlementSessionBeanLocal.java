/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.mas.session;

import ejb.card.entity.MasterCardClearingNetwork;
import ejb.card.entity.VisaClearingNetwork;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface MEPSCardSimulationSettlementSessionBeanLocal {
    public void recordSettlementInformationOfVisa();
    
    public void recordSettlementInformationOfMasterCard();
    
    public void MEPSVisaSettlementVTC();
    
    public void MEPSVisaSettlementMTV();
    
    public void MEPSMasterCardSettlementMTM();
    
    public void MEPSMasterCardSettlementMTC();
    
    
}
