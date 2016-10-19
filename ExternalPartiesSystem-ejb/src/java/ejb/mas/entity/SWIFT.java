package ejb.mas.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SWIFT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swiftId;
    private String swiftMessage;
    private String transactionDate;

    public Long getSwiftId() {
        return swiftId;
    }

    public void setSwiftId(Long swiftId) {
        this.swiftId = swiftId;
    }

    public String getSwiftMessage() {
        return swiftMessage;
    }

    public void setSwiftMessage(String swiftMessage) {
        this.swiftMessage = swiftMessage;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (swiftId != null ? swiftId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SWIFT)) {
            return false;
        }
        SWIFT other = (SWIFT) object;
        if ((this.swiftId == null && other.swiftId != null) || (this.swiftId != null && !this.swiftId.equals(other.swiftId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.mas.entity.SWIFT[ id=" + swiftId + " ]";
    }
    
}
