/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.entity.OtherBankAccountTransaction;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class MerchantTransactionRecordsSessionBean implements MerchantTransactionRecordsSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @Override
    public List<OtherBankAccountTransaction> getWatsonsTransactionRecords() {
        List<OtherBankAccount> bankAccounts = otherBankAccountSessionBeanLocal.getAllCitiBankMerchantAccount();
        OtherBankAccount watsonsAccount = bankAccounts.get(0);

        return watsonsAccount.getOtherBankAccountTransaction();
    }

    @Override
    public List<OtherBankAccountTransaction> getNtucTransactionRecords() {
        List<OtherBankAccount> bankAccounts = otherBankAccountSessionBeanLocal.getAllCitiBankMerchantAccount();
        OtherBankAccount watsonsAccount = bankAccounts.get(1);

        return watsonsAccount.getOtherBankAccountTransaction();
    }

    @Override
    public List<OtherBankAccountTransaction> getSephoraTransactionRecords() {
        List<OtherBankAccount> bankAccounts = otherBankAccountSessionBeanLocal.getAllCitiBankMerchantAccount();
        OtherBankAccount watsonsAccount = bankAccounts.get(2);

        return watsonsAccount.getOtherBankAccountTransaction();
    }
}
