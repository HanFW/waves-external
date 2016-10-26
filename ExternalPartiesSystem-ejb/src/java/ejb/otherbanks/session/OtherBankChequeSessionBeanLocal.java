package ejb.otherbanks.session;

import ejb.otherbanks.entity.OtherBankCheque;
import javax.ejb.Local;

@Local
public interface OtherBankChequeSessionBeanLocal {
    public Long addNewReceivedCheque(String transactionDate, String transactionAmt,
            String receivedBankAccountNum, String receivedCustomerName, String receivedCustomerMobile,
            String issuedBankAccountNum, String otherBankAccountNum);
    public OtherBankCheque retrieveReceivedChequeById(Long chequeId);
}
