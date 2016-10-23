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

    public List<Bill> getAllNTUCBills() {

        List<Bill> ntucBills = billSessionBeanLocal.getAllBill("NTUC");

        return ntucBills;
    }
}
