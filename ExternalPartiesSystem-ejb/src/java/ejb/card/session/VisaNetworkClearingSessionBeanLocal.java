/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.VisaClearingNetwork;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qinglai
 */
@Local
public interface VisaNetworkClearingSessionBeanLocal {
   public void createNewVisaClearingRecord(double transactionAmt, String reference, String transactionTime, String status, 
    String merchantName);
   
   public double getTotalCreditAmt();
   
   public List<VisaClearingNetwork> getAllVisaRecords();

   
   
}
