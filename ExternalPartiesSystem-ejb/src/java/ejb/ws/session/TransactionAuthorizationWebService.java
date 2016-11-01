/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.ws.session;

import ejb.card.entity.TransactionToBeAuthorized;
import ejb.card.entity.VisaClearingNetwork;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@WebService(serviceName = "TransactionAuthorizationWebService")
@Stateless()
public class TransactionAuthorizationWebService {

@PersistenceContext
    EntityManager em;

    @WebMethod(operationName = "getAllPendingTransactionsToBeAuthorized")
    public List<TransactionToBeAuthorized> getAllPendingTransactionsToBeAuthorized() {

        List<TransactionToBeAuthorized> transactions = new ArrayList<TransactionToBeAuthorized>();

        Query q = em.createQuery("select t from TransactionToBeAuthorized t where t.transactionStatus=:status");
        q.setParameter("status", "pending");

        if (!q.getResultList().isEmpty()) {
            transactions = q.getResultList();
        }
        return transactions;
    }

    @WebMethod(operationName = "getTransactionToBeAuthorizedById")
    public TransactionToBeAuthorized getTransactionToBeAuthorizedById(@WebParam(name = "id") Long id) {
        TransactionToBeAuthorized transaction = em.find(TransactionToBeAuthorized.class, id);
        return transaction;
    }

    @WebMethod(operationName = "createTransactionToBeAuthorized")
    public TransactionToBeAuthorized createTransactionToBeAuthorized(@WebParam(name = "cardType") String cardType,
            @WebParam(name = "cardNum") String cardNum, @WebParam(name = "customerSignature") String customerSignature,
            @WebParam(name = "debitCardPwd") String debitCardPwd, @WebParam(name = "transactionAmt") double transactionAmt,
            @WebParam(name = "transactionTime") String transactionTime) {
        //TODO write your implementation code here:
        TransactionToBeAuthorized transaction = new TransactionToBeAuthorized();
        transaction.setCardNum(cardNum);
        transaction.setCardType(cardType);
        transaction.setCustomerSignature(customerSignature);
        transaction.setCardPwd(debitCardPwd);
        transaction.setTransactionAmt(transactionAmt);
        transaction.setTransactionTime(transactionTime);
        transaction.setTransactionStatus("pending");

        em.persist(transaction);
        em.flush();
        System.out.println("transaction to be athorized created: " + transaction);
        return transaction;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "createNewVisaClearingRecord")
    public void createNewVisaClearingRecord(@WebParam(name = "transactionAmt") Double transactionAmt, 
            @WebParam(name = "reference") String reference, 
            @WebParam(name = "transactionTime") String transactionTime) {
    
     VisaClearingNetwork visaRecord= new VisaClearingNetwork();
         
         String referenceNo=generateReferenceNo();
         
         visaRecord.setReferenceNo(referenceNo);
         visaRecord.setReference(reference);
         visaRecord.setTransactionAmt(transactionAmt);
         visaRecord.setTransactionTime(transactionTime);
         
         em.persist(visaRecord);
//         totalCreditAmt+=transactionAmt;
         System.out.println("****** visa network clearing session bean: createNewVisaClearingRecord "+visaRecord);
//         System.out.println("****** visa network clearing session bean: total credit/debit amount "+totalCreditAmt);
         
    }
    
    private String generateReferenceNo(){
        List<String> referenceNoList=new ArrayList<String>();
        Random rnd = new Random();

        final char[] ch = new char[16];
        for (int i = 0; i < 16; i++) {
            ch[i] = (char) ('0' + (i == 0 ? rnd.nextInt(9) + 1 : rnd.nextInt(10)));
        }
        
        String referenceNo = String.valueOf(ch);
        
        System.out.println("before: "+referenceNoList);
        while(referenceNoList.contains(referenceNo)){
            referenceNo=generateReferenceNo();
        }

        System.out.println("****** visa network clearing session bean: reference no generated" + referenceNo);
        referenceNoList.add(referenceNo);
         System.out.println("after: "+referenceNoList);
        return referenceNo;
    }

}
