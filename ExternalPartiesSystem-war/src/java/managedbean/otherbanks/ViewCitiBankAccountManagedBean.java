/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.otherbanks;

import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.session.OtherBankAccountSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Jingyuan
 */
@Named(value = "viewCitiBankAccountManagedBean")
@RequestScoped
public class ViewCitiBankAccountManagedBean {

    /**
     * Creates a new instance of ViewCitiBankAccountManagedBean
     */
    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    public ViewCitiBankAccountManagedBean() {
    }

    public List<OtherBankAccount> getAllCitiBankAccounts() throws IOException {

        List<OtherBankAccount> citiBankAccounts = otherBankAccountSessionBeanLocal.getAllCitiBankMerchantAccount();

        return citiBankAccounts;
    }

}
