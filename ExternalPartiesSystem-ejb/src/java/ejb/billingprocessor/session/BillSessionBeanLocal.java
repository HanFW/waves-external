package ejb.billingprocessor.session;

import ejb.billingprocessor.entity.Bill;
import java.util.List;
import javax.ejb.Local;

@Local
public interface BillSessionBeanLocal {
    public Long addNewBill(String customerName, String customerMobile, String billReference, 
            String billingOrganizationName, String creditBank, String creditBankAccountNum,
            String debitBank, String debitBankAccountNum);
    public Bill retrieveBillByBillOrgName(String billingOrganizationName);
    public List<Bill> getAllBill(String billingOrganizationName);
    public Bill retrieveBillByBillId(Long billId);
    public void updateBillingPayment(Long billId, Double paymentAmt);
}
