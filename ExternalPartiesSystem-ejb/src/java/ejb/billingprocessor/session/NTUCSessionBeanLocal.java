package ejb.billingprocessor.session;

import javax.ejb.Local;

@Local
public interface NTUCSessionBeanLocal {

    public void receiveStandingGIROFromSACH(String customerName, String customerMobile,
            String billReference, String billingOrganizationName, String creditBank,
            String creditBankAccountNum, String debitBank, String debitBankAccountNum,
            String paymemtLimit, String billStatus, boolean buttonRender);

    public void receiveNonStandingGIROFromSACH(String customerName, String customerMobile,
            String billReference, String billingOrganizationName, String creditBank,
            String creditBankAccountNum, String debitBank, String debitBankAccountNum,
            String paymemtLimit, String billStatus, String paymentFrequency);
}
