package ejb.mas.entity;

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
    private String organizationName;
    private String country;

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
