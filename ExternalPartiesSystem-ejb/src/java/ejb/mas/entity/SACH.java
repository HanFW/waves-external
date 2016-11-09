package ejb.mas.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SACH implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sachId;
    private Double bankATotalCredit;
    private Double bankBTotalCredit;
    private String currentTime;
    private String bankNames;
    private String paymentMethod;
    private String creditAccountNum;
    private String creditBank;
    private String debitAccountNum;
    private String debitBank;
    private Long currentTimeMilis;
    private Double creditAmt;
    private String sachStatus;
    private String failedReason;

    public Long getSachId() {
        return sachId;
    }

    public void setSachId(Long sachId) {
        this.sachId = sachId;
    }

    public String getBankNames() {
        return bankNames;
    }

    public void setBankNames(String bankNames) {
        this.bankNames = bankNames;
    }

    public Double getBankATotalCredit() {
        return bankATotalCredit;
    }

    public void setBankATotalCredit(Double bankATotalCredit) {
        this.bankATotalCredit = bankATotalCredit;
    }

    public Double getBankBTotalCredit() {
        return bankBTotalCredit;
    }

    public void setBankBTotalCredit(Double bankBTotalCredit) {
        this.bankBTotalCredit = bankBTotalCredit;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreditAccountNum() {
        return creditAccountNum;
    }

    public void setCreditAccountNum(String creditAccountNum) {
        this.creditAccountNum = creditAccountNum;
    }

    public String getCreditBank() {
        return creditBank;
    }

    public void setCreditBank(String creditBank) {
        this.creditBank = creditBank;
    }

    public String getDebitAccountNum() {
        return debitAccountNum;
    }

    public void setDebitAccountNum(String debitAccountNum) {
        this.debitAccountNum = debitAccountNum;
    }

    public String getDebitBank() {
        return debitBank;
    }

    public void setDebitBank(String debitBank) {
        this.debitBank = debitBank;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Long getCurrentTimeMilis() {
        return currentTimeMilis;
    }

    public void setCurrentTimeMilis(Long currentTimeMilis) {
        this.currentTimeMilis = currentTimeMilis;
    }

    public Double getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(Double creditAmt) {
        this.creditAmt = creditAmt;
    }

    public String getSachStatus() {
        return sachStatus;
    }

    public void setSachStatus(String sachStatus) {
        this.sachStatus = sachStatus;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sachId != null ? sachId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SACH)) {
            return false;
        }
        SACH other = (SACH) object;
        if ((this.sachId == null && other.sachId != null) || (this.sachId != null && !this.sachId.equals(other.sachId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.SACH[ id=" + sachId + " ]";
    }
    
}
