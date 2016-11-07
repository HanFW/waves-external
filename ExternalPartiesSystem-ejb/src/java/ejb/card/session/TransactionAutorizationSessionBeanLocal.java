/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.TransactionToBeAuthorized;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author qinglai
 */
@Local
public interface TransactionAutorizationSessionBeanLocal {
    public TransactionToBeAuthorized createTransactionToBeAuthorized(String cardType, String cardNum, String customerSignature, String debitCardPwd, double transactionAmt, String transactionTime,
            String merchantName);
    
    public List<TransactionToBeAuthorized> getAllPendingTransactionsToBeAuthorized();
    
    public List<TransactionToBeAuthorized> getAllAuthorizedTransactions();
    
    public List<TransactionToBeAuthorized> getAllAuthorizedDebitCardTransaction();
    
    public TransactionToBeAuthorized getTransactionToBeAuthorizedById(Long id);
  
    public String checkTransactionAuthorization(Long id);
    
    public void merlionCreditCustomerForTransactionMade();
    
    public void updateTransactionStatus(Long id);
}
