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
            @WebParam(name = "creditBank") String creditBank,
            @WebParam(name = "debitBank") String debitBank,
            @WebParam(name = "creditBankAccountNum") String creditBankAccountNum,
            @WebParam(name = "debitBankAccountNum") String debitBankAccountNum,
            @WebParam(name = "paymentLimit") String paymentLimit,
            @WebParam(name = "buttonRender") boolean buttonRender) {
        Bill bill = new Bill();

        bill.setCustomerName(customerName);
        bill.setCustomerMobile(customerMobile);
        bill.setBillReference(billReference);
        bill.setBillingOrganizationName(billingOrganizationName);
        bill.setCreditBank(creditBank);
        bill.setCreditBankAccountNum(creditBankAccountNum);
        bill.setDebitBank(debitBank);
        bill.setDebitBankAccountNum(debitBankAccountNum);
        bill.setPaymentLimit(paymentLimit);
        bill.setButtonRender(buttonRender);

        entityManager.persist(bill);
        entityManager.flush();

        return bill.getBillId();
    }

}
