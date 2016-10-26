package ejb.ws.session;

import ejb.mas.entity.SWIFT;
import ejb.mas.session.SWIFTSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@WebService(serviceName = "SWIFTWebService")
@Stateless()

public class SWIFTWebService {

    @EJB
    private SWIFTSessionBeanLocal sWIFTSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @WebMethod(operationName = "addNewSWIFT")
    public Long addNewSWIFT(@WebParam(name = "swiftMessage") String swiftMessage,
            @WebParam(name = "transactionDate") String transactionDate,
            @WebParam(name = "swiftCodeA") String swiftCodeA,
            @WebParam(name = "swiftCodeB") String swiftCodeB,
            @WebParam(name = "organizationA") String organizationA,
            @WebParam(name = "organizationB") String organizationB,
            @WebParam(name = "countryA") String countryA,
            @WebParam(name = "countryB") String countryB,
            @WebParam(name = "paymentAmt") String paymentAmt,
            @WebParam(name = "transferedBankAccountNum") String transferedBankAccountNum) {

        SWIFT swift = new SWIFT();

        swift.setOrganizationA(organizationA);
        swift.setOrganizationB(organizationB);
        swift.setSwiftCodeA(swiftCodeA);
        swift.setSwiftCodeB(swiftCodeB);
        swift.setSwiftMessage(swiftMessage);
        swift.setTransactionDate(transactionDate);
        swift.setCountryA(countryA);
        swift.setCountryB(countryB);
        swift.setPaymentAmt(paymentAmt);
        swift.setTransferedBankAccountNum(transferedBankAccountNum);

        entityManager.persist(swift);
        entityManager.flush();

        sWIFTSessionBeanLocal.sendMessageToReceivedInstitution(swift.getSwiftId());

        return swift.getSwiftId();
    }

}
