package ejb.ws.session;

import ejb.billingprocessor.entity.Bill;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@WebService(serviceName = "BillWebService")
@Stateless()

public class BillWebService {

    @PersistenceContext
    private EntityManager entityManager;

    @WebMethod(operationName = "addNewBill")
    public Long addNewBill(@WebParam(name = "customerName") String customerName,
            @WebParam(name = "customerMobile") String customerMobile,
            @WebParam(name = "billReference") String billReference,
            @WebParam(name = "billingOrganizationName") String billingOrganizationName,
            @WebParam(name = "debitBank") String debitBank,
            @WebParam(name = "debitBankAccountNum") String debitBankAccountNum) {
        Bill bill = new Bill();

        bill.setCustomerName(customerName);
        bill.setCustomerMobile(customerMobile);
        bill.setBillReference(billReference);
        bill.setBillingOrganizationName(billingOrganizationName);
        bill.setDebitBank(debitBank);
        bill.setDebitBankAccountNum(debitBankAccountNum);

        entityManager.persist(bill);
        entityManager.flush();

        return bill.getBillId();
    }

}
