package ejb.billingprocessor.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;
    private String customerName;
    private String billReference;
    private String customerMobile;
    private String paymentAmt;
    private String billingOrganizationName;
    private String debitBank;
    private String debitBankAccountNum;
    private String paymentLimit;
    private String billStatus;
    private String creditBank;
    private String creditBankAccountNum;
    private boolean buttonRender;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getDebitBank() {
        return debitBank;
    }

    public void setDebitBank(String debitBank) {
        this.debitBank = debitBank;
    }

    public String getDebitBankAccountNum() {
        return debitBankAccountNum;
    }

    public void setDebitBankAccountNum(String debitBankAccountNum) {
        this.debitBankAccountNum = debitBankAccountNum;
    }

    public String getPaymentLimit() {
        return paymentLimit;
    }

    public void setPaymentLimit(String paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getCreditBank() {
        return creditBank;
    }

    public void setCreditBank(String creditBank) {
        this.creditBank = creditBank;
    }

    public String getCreditBankAccountNum() {
        return creditBankAccountNum;
    }

    public void setCreditBankAccountNum(String creditBankAccountNum) {
        this.creditBankAccountNum = creditBankAccountNum;
    }

    public boolean isButtonRender() {
        return buttonRender;
    }

    public void setButtonRender(boolean buttonRender) {
        this.buttonRender = buttonRender;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billId != null ? billId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bill)) {
            return false;
        }
        Bill other = (Bill) object;
        if ((this.billId == null && other.billId != null) || (this.billId != null && !this.billId.equals(other.billId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.mas.entity.Bill[ id=" + billId + " ]";
    }

}
