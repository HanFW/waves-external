package ejb.billingprocessor.session;

import ejb.billingprocessor.entity.Bill;
import java.util.List;
import javax.ejb.Local;

@Local
public interface BillSessionBeanLocal {

    public Long addNewBill(String customerName, String customerMobile, String billReference,
            String billingOrganizationName, String creditBank, String creditBankAccountNum,
            String debitBank, String debitBankAccountNum, String paymentLimit, boolean buttonRender,
            String paymentFrequency, String billType);

    public Bill retrieveBillByBillOrgName(String billingOrganizationName);

    public List<Bill> getAllStandingGIROBill(String billingOrganizationName);

    public List<Bill> getAllNonStandingGIROBill(String billingOrganizationName);

    public Bill retrieveBillByBillId(Long billId);

    public void updateBillingPayment(Long billId, Double paymentAmt);

    public String deleteBill(Long billId);

    public void updateButtonRender(Long billId);
}
