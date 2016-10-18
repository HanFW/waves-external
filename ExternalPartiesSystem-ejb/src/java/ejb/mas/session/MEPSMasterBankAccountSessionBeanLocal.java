package ejb.mas.session;

import ejb.mas.entity.MEPSMasterBankAccount;
import javax.ejb.Local;

@Local
public interface MEPSMasterBankAccountSessionBeanLocal {
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountByBankName(String bankName);
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountByAccNum(String masterBankAccountNum);
    public MEPSMasterBankAccount retrieveMEPSMasterBankAccountById(Long masterBankAccountId);
    public void maintainDailyBalance();
}
