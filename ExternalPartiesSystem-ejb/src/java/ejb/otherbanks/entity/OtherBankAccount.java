package ejb.otherbanks.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OtherBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherBankAccountId;
    private String otherBankAccountNum;
    private String otherBankAccountType;
    private String totalBankAccountBalance;
    private String availableBankAccountBalance;
    private String bankName;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "otherBankAccount")
    private List<OtherBankAccountTransaction> otherBankAccountTransaction;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "otherBankAccount")
    private List<OtherBankCheque> otherBankCheque;

    public Long getOtherBankAccountId() {
        return otherBankAccountId;
    }

    public void setOtherBankAccountId(Long otherBankAccountId) {
        this.otherBankAccountId = otherBankAccountId;
    }

    public String getOtherBankAccountNum() {
        return otherBankAccountNum;
    }

    public void setOtherBankAccountNum(String otherBankAccountNum) {
        this.otherBankAccountNum = otherBankAccountNum;
    }

    public String getOtherBankAccountType() {
        return otherBankAccountType;
    }

    public void setOtherBankAccountType(String otherBankAccountType) {
        this.otherBankAccountType = otherBankAccountType;
    }

    public List<OtherBankAccountTransaction> getOtherBankAccountTransaction() {
        return otherBankAccountTransaction;
    }

    public void setOtherBankAccountTransaction(List<OtherBankAccountTransaction> otherBankAccountTransaction) {
        this.otherBankAccountTransaction = otherBankAccountTransaction;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<OtherBankCheque> getOtherBankCheque() {
        return otherBankCheque;
    }

    public void setOtherBankCheque(List<OtherBankCheque> otherBankCheque) {
        this.otherBankCheque = otherBankCheque;
    }

    public String getTotalBankAccountBalance() {
        return totalBankAccountBalance;
    }

    public void setTotalBankAccountBalance(String totalBankAccountBalance) {
        this.totalBankAccountBalance = totalBankAccountBalance;
    }

    public String getAvailableBankAccountBalance() {
        return availableBankAccountBalance;
    }

    public void setAvailableBankAccountBalance(String availableBankAccountBalance) {
        this.availableBankAccountBalance = availableBankAccountBalance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otherBankAccountId != null ? otherBankAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtherBankAccount)) {
            return false;
        }
        OtherBankAccount other = (OtherBankAccount) object;
        if ((this.otherBankAccountId == null && other.otherBankAccountId != null) || (this.otherBankAccountId != null && !this.otherBankAccountId.equals(other.otherBankAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.DBSBankAccount[ id=" + otherBankAccountId + " ]";
    }
    
}
