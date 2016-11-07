/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankAccountTransaction;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface MerchantTransactionRecordsSessionBeanLocal {
    public List<OtherBankAccountTransaction> getWatsonsTransactionRecords();
    
    public List<OtherBankAccountTransaction> getNtucTransactionRecords();
    
    public List<OtherBankAccountTransaction> getSephoraTransactionRecords();
}
