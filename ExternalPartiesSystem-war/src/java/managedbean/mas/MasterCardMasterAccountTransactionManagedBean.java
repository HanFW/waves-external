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
@Named(value = "masterCardMasterAccountTransactionManagedBean")
@RequestScoped
public class MasterCardMasterAccountTransactionManagedBean {

    /**
     * Creates a new instance of MasterCardMasterAccountTransactionManagedBean
     */
    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    public MasterCardMasterAccountTransactionManagedBean() {
    }

    public List<MEPSMasterAccountTransaction> getMasterCardMasterAccountTransactions() {

        List<MEPSMasterAccountTransaction> masterCardMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("20160308");

        return masterCardMasterAccountTransactions;
    }

}
