/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.TransactionToBeAuthorized;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.transactionAuthorization.MerlionTransactionAuthorizationWebService_Service;

/**
 *
 * @author jingyuan
 */
@Stateless
public class TransactionAutorizationSessionBean implements TransactionAutorizationSessionBeanLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionTransactionAuthorizationWebService/MerlionTransactionAuthorizationWebService.wsdl")
    private MerlionTransactionAuthorizationWebService_Service service_merlionTransactionAuthorization;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    @EJB
    VisaNetworkClearingSessionBeanLocal visaNetworkClearingSessionBeanLocal;

    @Override
    public TransactionToBeAuthorized createTransactionToBeAuthorized(String cardType, String cardNum, String customerSignature, String debitCardPwd, double transactionAmt, String transactionTime, String merchantName) {
        TransactionToBeAuthorized transaction = new TransactionToBeAuthorized();
        transaction.setCardNum(cardNum);
        transaction.setCardType(cardType);
        transaction.setCustomerSignature(customerSignature);
        transaction.setCardPwd(debitCardPwd);
        transaction.setTransactionAmt(transactionAmt);
        transaction.setTransactionTime(transactionTime);
        transaction.setMerchantName(merchantName);
        transaction.setTransactionStatus("pending");
        transaction.setDebitBankAccount("no");

        em.persist(transaction);
        em.flush();
        System.out.println("transaction to be authorized created: " + transaction);
        return transaction;
    }

    @Override
    public List<TransactionToBeAuthorized> getAllPendingTransactionsToBeAuthorized() {
        List<TransactionToBeAuthorized> transactions = new ArrayList<>();

        Query q = em.createQuery("select t from TransactionToBeAuthorized t where t.transactionStatus=:status");
        q.setParameter("status", "pending");

        if (!q.getResultList().isEmpty()) {
            transactions = q.getResultList();
        }
        return transactions;

    }

    @Override
    public List<TransactionToBeAuthorized> getAllAuthorizedTransactions() {
        List<TransactionToBeAuthorized> transactions = new ArrayList<>();

        Query q = em.createQuery("select t from TransactionToBeAuthorized t where t.transactionStatus=:status");
        q.setParameter("status", "authorized");

        if (!q.getResultList().isEmpty()) {
            transactions = q.getResultList();
        }
        return transactions;
    }

    @Override
    public TransactionToBeAuthorized getTransactionToBeAuthorizedById(Long id) {
        TransactionToBeAuthorized transaction = em.find(TransactionToBeAuthorized.class, id);
        return transaction;
    }

    @Override
    public String checkTransactionAuthorization(Long id) {
        String result = checkTransactionAuthorizationById(id);
        return result;
    }
    
    @Override
    public void merlionCreditCustomerForTransactionMade(){
      String result = merlionCreditCustomerAccountForTransaction();
      List<TransactionToBeAuthorized> transactions = getAllAuthorizedDebitCardTransaction();
      for(int i=0;i<transactions.size();i++){
          transactions.get(i).setDebitBankAccount("yes");
          em.flush();
      }
      
    }
    
    @Override
    public void updateTransactionStatus(Long id){
        TransactionToBeAuthorized transaction = em.find(TransactionToBeAuthorized.class, id);
        transaction.setTransactionStatus("authorized");
        em.flush();
    }


    private String checkTransactionAuthorizationById(java.lang.Long id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.transactionAuthorization.MerlionTransactionAuthorizationWebService port = service_merlionTransactionAuthorization.getMerlionTransactionAuthorizationWebServicePort();
        return port.checkTransactionAuthorizationById(id);
    }

    private String merlionCreditCustomerAccountForTransaction() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.transactionAuthorization.MerlionTransactionAuthorizationWebService port = service_merlionTransactionAuthorization.getMerlionTransactionAuthorizationWebServicePort();
        return port.merlionCreditCustomerAccountForTransaction();
    }
    
    @Override
    public List<TransactionToBeAuthorized> getAllAuthorizedDebitCardTransaction(){
                List<TransactionToBeAuthorized> transactions = new ArrayList<>();

        Query q = em.createQuery("select t from TransactionToBeAuthorized t where t.transactionStatus=:statusand t.cardType=:cardType and t.debitBankAccount=:debitAccount");
        q.setParameter("status", "authorized");
        q.setParameter("cardType", "debit");
        q.setParameter("debitAccount", "no");

        if (!q.getResultList().isEmpty()) {
            transactions = q.getResultList();
        }
        return transactions;
    }


    
    

}
