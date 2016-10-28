package ejb.chips.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CHIPS implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chipsId;
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

    public Long getChipsId() {
        return chipsId;
    }

    public void setChipsId(Long chipsId) {
        this.chipsId = chipsId;
    }

    public String getBankNames() {
        return bankNames;
    }

    public void setBankNames(String bankNames) {
        this.bankNames = bankNames;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chipsId != null ? chipsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CHIPS)) {
            return false;
        }
        CHIPS other = (CHIPS) object;
        if ((this.chipsId == null && other.chipsId != null) || (this.chipsId != null && !this.chipsId.equals(other.chipsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.mas.entity.CHIPS[ id=" + chipsId + " ]";
    }
    
}
