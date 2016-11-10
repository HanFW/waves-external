/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.MasterCardClearingNetwork;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class MasterCardNetworkClearingSessionBean implements MasterCardNetworkClearingSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    private List<String> referenceNoList = new ArrayList<String>();
    private double totalCreditAmt = 0.0;

    @Override
    public void createNewMasterCardClearingRecord(double transactionAmt, String reference, String transactionTime,
            String status, String merchantName) {
        MasterCardClearingNetwork masterCardRecord = new MasterCardClearingNetwork();

        String referenceNo = generateReferenceNo();

        masterCardRecord.setReferenceNo(referenceNo);
        masterCardRecord.setReference(reference);
        masterCardRecord.setTransactionAmt(transactionAmt);
        masterCardRecord.setTransactionTime(transactionTime);
        masterCardRecord.setMerchantname(merchantName);
        masterCardRecord.setStatus("new");
        masterCardRecord.setPayMerchantStatus("no");

        em.persist(masterCardRecord);
        totalCreditAmt += transactionAmt;
        System.out.println("****** MasterCard network clearing session bean: createNewMasterCardClearingRecord " + masterCardRecord);
        System.out.println("****** MasterCard network clearing session bean: total credit/debit amount " + totalCreditAmt);

    }

    private String generateReferenceNo() {
        Random rnd = new Random();

        final char[] ch = new char[16];
        for (int i = 0; i < 16; i++) {
            ch[i] = (char) ('0' + (i == 0 ? rnd.nextInt(9) + 1 : rnd.nextInt(10)));
        }

        String referenceNo = String.valueOf(ch);

        System.out.println("before: " + referenceNoList);
        while (referenceNoList.contains(referenceNo)) {
            referenceNo = generateReferenceNo();
        }

        System.out.println("****** visa network clearing session bean: reference no generated" + referenceNo);
        referenceNoList.add(referenceNo);
        System.out.println("after: " + referenceNoList);
        return referenceNo;
    }

    @Override
    public double getTotalCreditAmt() {
        return totalCreditAmt;
    }

    @Override
    public List<MasterCardClearingNetwork> getAllMasterCardRecords() {
        List<MasterCardClearingNetwork> masterCardRecords = new ArrayList<MasterCardClearingNetwork>();
        Query q = em.createQuery("select m from MasterCardClearingNetwork m where m.status=:status");
        q.setParameter("status", "new");

        if (!q.getResultList().isEmpty()) {
            masterCardRecords = q.getResultList();
        }
        return masterCardRecords;
    }
}
