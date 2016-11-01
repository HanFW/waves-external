/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author jingyuan
 */
@Entity
public class VisaClearingNetwork implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visaClearingNetworkId;

    private double transactionAmt;
    private String reference;
    private String referenceNo;
    private String transactionTime;
    private String merchantName;
    private String status;
    private String payMerchantStatus;

    public Long getVisaClearingNetworkId() {
        return visaClearingNetworkId;
    }

    public void setVisaClearingNetworkId(Long visaClearingNetworkId) {
        this.visaClearingNetworkId = visaClearingNetworkId;
    }

    public double getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(double transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayMerchantStatus() {
        return payMerchantStatus;
    }

    public void setPayMerchantStatus(String payMerchantStatus) {
        this.payMerchantStatus = payMerchantStatus;
    }
    
}
