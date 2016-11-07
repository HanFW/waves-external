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
@Named(value = "citiBankMasterAccountTransactionManagedBean")
@RequestScoped
public class CitiBankMasterAccountTransactionManagedBean {

    /**
     * Creates a new instance of CitiBankMasterAccountTransactionManagedBean
     */
    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    public CitiBankMasterAccountTransactionManagedBean() {
    }

    public List<MEPSMasterAccountTransaction> getCitiBankMasterAccountTransactions() {

        List<MEPSMasterAccountTransaction> citiBankMasterAccountTransactions = mEPSMasterAccountTransactionSessionBeanLocal.retrieveMEPSMasterAccountTransactionByAccNum("20160913");

        return citiBankMasterAccountTransactions;
    }

}
