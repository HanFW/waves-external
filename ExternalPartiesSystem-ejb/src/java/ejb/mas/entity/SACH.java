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
    private String updateDate;
    private String bankNames;
    private String paymentMethod;

    public Long getSachId() {
        return sachId;
    }

    public void setSachId(Long sachId) {
        this.sachId = sachId;
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
