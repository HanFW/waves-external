package ejb.mas.entity;

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
    private Double otherBankTotalCredit;
    private Double merlionTotalCredit;
    private String updateDate;
    private String bankNames;
    private String paymentMethod;

    public Long getChipsId() {
        return chipsId;
    }

    public void setChipsId(Long chipsId) {
        this.chipsId = chipsId;
    }

    public Double getOtherBankTotalCredit() {
        return otherBankTotalCredit;
    }

    public void setOtherBankTotalCredit(Double otherBankTotalCredit) {
        this.otherBankTotalCredit = otherBankTotalCredit;
    }

    public Double getMerlionTotalCredit() {
        return merlionTotalCredit;
    }

    public void setMerlionTotalCredit(Double merlionTotalCredit) {
        this.merlionTotalCredit = merlionTotalCredit;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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
