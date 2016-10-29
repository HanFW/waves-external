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
    private String settlementStatus;
    private String creditMEPSBankAccountNum;
    private String debitMEPSBankAccountNum;
    private String clearanceSystem;

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

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getCreditMEPSBankAccountNum() {
        return creditMEPSBankAccountNum;
    }

    public void setCreditMEPSBankAccountNum(String creditMEPSBankAccountNum) {
        this.creditMEPSBankAccountNum = creditMEPSBankAccountNum;
    }

    public String getDebitMEPSBankAccountNum() {
        return debitMEPSBankAccountNum;
    }

    public void setDebitMEPSBankAccountNum(String debitMEPSBankAccountNum) {
        this.debitMEPSBankAccountNum = debitMEPSBankAccountNum;
    }

    public String getClearanceSystem() {
        return clearanceSystem;
    }

    public void setClearanceSystem(String clearanceSystem) {
        this.clearanceSystem = clearanceSystem;
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
        return "ejb.mas.entity.Settlement[ id=" + settlementId + " ]";
    }

}
