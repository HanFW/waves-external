/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.mas;

import ejb.mas.entity.MEPSMasterAccountTransaction;
import ejb.mas.session.MEPSMasterAccountTransactionSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "merlionBankMasterAccountTransactionManagedBean")
@RequestScoped
public class MerlionBankMasterAccountTransactionManagedBean {

    /**
     * Creates a new instance of MerlionBankMasterAccountTransactionManagedBean
     */
    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    public MerlionBankMasterAccountTransactionManagedBean() {
    }

    public List<MEPSMasterAccountTransaction> getMerlionBankMasterAccountTransactions(){

        List<MEPSMasterAccountTransaction> merlionBankMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("20161109");

        return merlionBankMasterAccountTransactions;
    }

}
