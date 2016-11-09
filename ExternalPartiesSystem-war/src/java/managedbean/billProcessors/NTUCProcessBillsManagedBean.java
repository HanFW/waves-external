package managedbean.billProcessors;

import ejb.billingprocessor.entity.Bill;
import ejb.billingprocessor.session.BillSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "nTUCProcessBillsManagedBean")
@RequestScoped

public class NTUCProcessBillsManagedBean {

    @EJB
    private BillSessionBeanLocal billSessionBeanLocal;

    public NTUCProcessBillsManagedBean() {
    }

    public List<Bill> getAllStandingNTUCBills() {

        List<Bill> ntucBills = billSessionBeanLocal.getAllStandingGIROBill("NTUC");

        return ntucBills;
    }

    public List<Bill> getAllNonStandingNTUCBills() {

        List<Bill> ntucBills = billSessionBeanLocal.getAllNonStandingGIROBill("NTUC");

        return ntucBills;
    }
}
