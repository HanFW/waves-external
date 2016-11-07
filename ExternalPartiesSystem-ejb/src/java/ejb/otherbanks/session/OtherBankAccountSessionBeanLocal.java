package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankAccount;
import java.util.List;
import javax.ejb.Local;

@Local
public interface OtherBankAccountSessionBeanLocal {
    public List<OtherBankAccount> getAllDBSBankAccount();
    public OtherBankAccount retrieveBankAccountByNum(String otherBankAccountNum);
    public OtherBankAccount retrieveBankAccountById(Long otherBankAccountId);
    public void updateBankAccountBalance(String otherBankAccountNum,String availableBankAccountBalance,
            String totalBankAccountBalance);
    public void updateAvailableAccountBalance(String otherBankAccountNum, String availableBankAccountBalance);
    public List<OtherBankAccount> getAllBankOfKoreaBankAccount();
    
    public List<OtherBankAccount> getAllCitiBankMerchantAccount();
    public List<Long> getAllCitiBankMerchantAccountId();
    public void updateOtherBankAccountBalanceById(Long otherbankAccountId, double transactionAmt);
    
}
