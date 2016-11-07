/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.VisaClearingNetwork;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author qinglai
 */
@Stateless
public class VisaNetworkClearingSessionBean implements VisaNetworkClearingSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext
    EntityManager em;
    
    private List<String> referenceNoList=new ArrayList<String>();
    private double totalCreditAmt=0.0;
    
    @Override
    public void createNewVisaClearingRecord(double transactionAmt, String reference, String transactionTime, String status,
            String merchantName){
         VisaClearingNetwork visaRecord= new VisaClearingNetwork();
         
         String referenceNo=generateReferenceNo();
         
         visaRecord.setReferenceNo(referenceNo);
         visaRecord.setReference(reference);
         visaRecord.setTransactionAmt(transactionAmt);
         visaRecord.setTransactionTime(transactionTime);
         visaRecord.setMerchantName(merchantName);
         visaRecord.setStatus("new");
         visaRecord.setPayMerchantStatus("no");
         
         em.persist(visaRecord);
         totalCreditAmt+=transactionAmt;
         System.out.println("****** visa network clearing session bean: createNewVisaClearingRecord "+visaRecord);
         System.out.println("****** visa network clearing session bean: total credit/debit amount "+totalCreditAmt);
         
    }
    
    private String generateReferenceNo(){
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
    
    @Override
    public double getTotalCreditAmt(){
        return totalCreditAmt;
    }
    
    @Override
    public List<VisaClearingNetwork> getAllVisaRecords(){
        List<VisaClearingNetwork> visaRecords=new ArrayList<VisaClearingNetwork>();
        Query q=em.createQuery("select v from VisaClearingNetwork v where v.payMerchantStatus=:status");
        q.setParameter("status", "yes");
        
        if(!q.getResultList().isEmpty())
            visaRecords=q.getResultList();
        
        return visaRecords;
    }
}
