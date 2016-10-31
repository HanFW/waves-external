package managedbean.billProcessors;

import ejb.billingprocessor.entity.Bill;
import ejb.billingprocessor.session.BillSessionBeanLocal;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "nTUCProcessBillsDoneManagedBean")
@SessionScoped

public class NTUCProcessBillsDoneManagedBean implements Serializable {

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @EJB
    private BillSessionBeanLocal billSessionBeanLocal;

    private Long billId;
    private String customerName;
    private String customerMobile;
    private String billReference;
    private String debitBank;
    private String debitBankAccountNum;
    private Double paymentAmt;

    private ExternalContext ec;

    public NTUCProcessBillsDoneManagedBean() {
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getCustomerName() {
        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);
        customerName = bill.getCustomerName();

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);
        customerMobile = bill.getCustomerMobile();

        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getBillReference() {
        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);
        billReference = bill.getBillReference();

        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getDebitBank() {
        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);
        debitBank = bill.getDebitBank();

        return debitBank;
    }

    public void setDebitBank(String debitBank) {
        this.debitBank = debitBank;
    }

    public String getDebitBankAccountNum() {
        Bill bill = billSessionBeanLocal.retrieveBillByBillId(billId);
        debitBankAccountNum = bill.getDebitBankAccountNum();

        return debitBankAccountNum;
    }

    public void setDebitBankAccountNum(String debitBankAccountNum) {
        this.debitBankAccountNum = debitBankAccountNum;
    }

    public Double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(Double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public void askForPayment() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        billSessionBeanLocal.updateBillingPayment(billId, paymentAmt);

        String result = otherBankSessionBeanLocal.askForCreditOtherBankAccount(billId);

        if (result.equals("Payment Approved")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have successfully updated payment amount.", ""));
        } else if (result.equals("Exceed Payment Limit")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have exceeded customer's payment limit.", ""));
        }
    }
}
