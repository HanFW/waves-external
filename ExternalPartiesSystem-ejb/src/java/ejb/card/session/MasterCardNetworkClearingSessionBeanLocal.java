/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.MasterCardClearingNetwork;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface MasterCardNetworkClearingSessionBeanLocal {

    public void createNewMasterCardClearingRecord(double transactionAmt, String reference, String transactionTime,
            String status,String merchantName);

    public double getTotalCreditAmt();

    public List<MasterCardClearingNetwork> getAllMasterCardRecords();
}
