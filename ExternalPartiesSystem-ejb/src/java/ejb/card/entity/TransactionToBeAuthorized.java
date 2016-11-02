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
 * @author Jingyuan
 */
@Entity
public class TransactionToBeAuthorized implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionToBeAuthorizedId;
    private String transactionTime;
    private String cardType;
    private String cardNum;
    private String cardPwd;
    private String customerSignature;
    private String transactionStatus;
    private double transactionAmt;
    private String merchantName;
    
    public Long getTransactionToBeAuthorizedId() {
        return transactionToBeAuthorizedId;
    }

    public void setTransactionToBeAuthorizedId(Long transactionToBeAuthorizedId) {
        this.transactionToBeAuthorizedId = transactionToBeAuthorizedId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public double getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(double transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionToBeAuthorizedId != null ? transactionToBeAuthorizedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionToBeAuthorized)) {
            return false;
        }
        TransactionToBeAuthorized other = (TransactionToBeAuthorized) object;
        if ((this.transactionToBeAuthorizedId == null && other.transactionToBeAuthorizedId != null) || (this.transactionToBeAuthorizedId != null && !this.transactionToBeAuthorizedId.equals(other.transactionToBeAuthorizedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.simulate.entity.TransactionToBeAuthorized[ id=" + transactionToBeAuthorizedId + " ]";
    }

}
