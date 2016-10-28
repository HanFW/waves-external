package ejb.swift.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SWIFTCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swiftCodeId;
    private String swiftCode;
    private String recipient;
    private String country;
    private String bankAccountNum;

    public Long getSwiftCodeId() {
        return swiftCodeId;
    }

    public void setSwiftCodeId(Long swiftCodeId) {
        this.swiftCodeId = swiftCodeId;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (swiftCodeId != null ? swiftCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SWIFTCode)) {
            return false;
        }
        SWIFTCode other = (SWIFTCode) object;
        if ((this.swiftCodeId == null && other.swiftCodeId != null) || (this.swiftCodeId != null && !this.swiftCodeId.equals(other.swiftCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.mas.entity.SWIFTCode[ id=" + swiftCodeId + " ]";
    }

}
