package ejb.billingprocessor.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class NTUCSessionBean implements NTUCSessionBeanLocal {

    @EJB
    private BillSessionBeanLocal billSessionBeanLocal;

    @Override
    public void receiveStandingGIROFromSACH(String customerName, String customerMobile,
            String billReference, String billingOrganizationName, String creditBank,
            String creditBankAccountNum, String debitBank, String debitBankAccountNum,
            String paymemtLimit, String billStatus, boolean buttonRender) {

        billSessionBeanLocal.addNewBill(customerName, customerMobile,
                billReference, billingOrganizationName, creditBank,
                creditBankAccountNum, debitBank, debitBankAccountNum,
                paymemtLimit, buttonRender, "Standing GIRO", "Standing GIRO");
    }

    @Override
    public void receiveNonStandingGIROFromSACH(String customerName, String customerMobile,
            String billReference, String billingOrganizationName, String creditBank,
            String creditBankAccountNum, String debitBank, String debitBankAccountNum,
            String paymemtLimit, String billStatus, String paymentFrequency) {

        billSessionBeanLocal.addNewBill(customerName, customerMobile,
                billReference, billingOrganizationName, creditBank,
                creditBankAccountNum, debitBank, debitBankAccountNum,
                paymemtLimit, false, paymentFrequency, "Non Standing GIRO");
    }
}
