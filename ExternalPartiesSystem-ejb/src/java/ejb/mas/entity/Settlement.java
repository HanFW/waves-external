package ejb.mas.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Settlement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;
    private String dailySettlementAmt;
    private String dailySettlementRef;
    private String updateDate;
    private String bankNames;

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public String getDailySettlementAmt() {
        return dailySettlementAmt;
    }

    public void setDailySettlementAmt(String dailySettlementAmt) {
        this.dailySettlementAmt = dailySettlementAmt;
    }

    public String getDailySettlementRef() {
        return dailySettlementRef;
    }

    public void setDailySettlementRef(String dailySettlementRef) {
        this.dailySettlementRef = dailySettlementRef;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (settlementId != null ? settlementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Settlement)) {
            return false;
        }
        Settlement other = (Settlement) object;
        if ((this.settlementId == null && other.settlementId != null) || (this.settlementId != null && !this.settlementId.equals(other.settlementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.mas.entity.SettlementInfo[ id=" + settlementId + " ]";
    }

}
