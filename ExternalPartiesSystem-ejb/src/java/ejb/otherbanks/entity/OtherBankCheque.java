package ejb.otherbanks.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OtherBankCheque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chequeId;
    private String transactionDate;
    private String transactionAmt;
    private String receivedBankAccountNum;
    private String receivedCustomerName;
    private String receivedCustomerMobile;
    private String issuedBankAccountNum;

    @ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private OtherBankAccount otherBankAccount;
    
    public Long getChequeId() {
        return chequeId;
    }

    public void setChequeId(Long chequeId) {
        this.chequeId = chequeId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(String transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getReceivedBankAccountNum() {
        return receivedBankAccountNum;
    }

    public void setReceivedBankAccountNum(String receivedBankAccountNum) {
        this.receivedBankAccountNum = receivedBankAccountNum;
    }

    public String getReceivedCustomerName() {
        return receivedCustomerName;
    }

    public void setReceivedCustomerName(String receivedCustomerName) {
        this.receivedCustomerName = receivedCustomerName;
    }

    public String getReceivedCustomerMobile() {
        return receivedCustomerMobile;
    }

    public void setReceivedCustomerMobile(String receivedCustomerMobile) {
        this.receivedCustomerMobile = receivedCustomerMobile;
    }

    public OtherBankAccount getOtherBankAccount() {
        return otherBankAccount;
    }

    public void setOtherBankAccount(OtherBankAccount otherBankAccount) {
        this.otherBankAccount = otherBankAccount;
    }

    public String getIssuedBankAccountNum() {
        return issuedBankAccountNum;
    }

    public void setIssuedBankAccountNum(String issuedBankAccountNum) {
        this.issuedBankAccountNum = issuedBankAccountNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chequeId != null ? chequeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtherBankCheque)) {
            return false;
        }
        OtherBankCheque other = (OtherBankCheque) object;
        if ((this.chequeId == null && other.chequeId != null) || (this.chequeId != null && !this.chequeId.equals(other.chequeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.otherbanks.entity.OtherBankCheque[ id=" + chequeId + " ]";
    }
    
}
