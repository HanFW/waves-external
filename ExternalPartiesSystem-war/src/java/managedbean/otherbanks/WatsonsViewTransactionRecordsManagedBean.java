/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.otherbanks;

import ejb.otherbanks.entity.OtherBankAccountTransaction;
import ejb.otherbanks.session.MerchantTransactionRecordsSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "watsonsViewTransactionRecordsManagedBean")
@RequestScoped
public class WatsonsViewTransactionRecordsManagedBean {

    /**
     * Creates a new instance of WatsonsViewTransactionRecordsManagedBean
     */
    
    @EJB
    private MerchantTransactionRecordsSessionBeanLocal merchantTransactionRecordsSessionBeanLocal;
    
    private List<OtherBankAccountTransaction> otherBankTransactions;
    
    public WatsonsViewTransactionRecordsManagedBean() {
    }
    
        public List<OtherBankAccountTransaction> getAllWatsonsTransactionRecords() {

        otherBankTransactions = merchantTransactionRecordsSessionBeanLocal.getWatsonsTransactionRecords();
        return otherBankTransactions;
    }     

}
