/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.otherbanks;

import ejb.otherbanks.entity.OtherBankAccountTransaction;
import ejb.otherbanks.session.OtherTransactionSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "viewCitiBankAccountTransactionManagedBean")
@SessionScoped
public class ViewCitiBankAccountTransactionManagedBean implements Serializable{

    /**
     * Creates a new instance of ViewCitiBankAccountTransactionManagedBean
     */
    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    private String bankAccountNum;
    
    public ViewCitiBankAccountTransactionManagedBean() {
    }
    
    
    public List<OtherBankAccountTransaction> getTransaction() throws IOException {

        List<OtherBankAccountTransaction> transaction = otherTransactionSessionBeanLocal.retrieveAccTransactionByBankNum(bankAccountNum);

        return transaction;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
    
    

}
