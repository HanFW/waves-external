package ejb.mas.session;

import ejb.mas.entity.SWIFT;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SWIFTSessionBean implements SWIFTSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewSwift(String swiftMessage, String transactionDate, String swiftCodeA,
            String swiftCodeB, String organizationA, String organizationB,
            String countryA, String countryB) {

        SWIFT swift = new SWIFT();

        swift.setOrganizationA(organizationA);
        swift.setOrganizationB(organizationB);
        swift.setSwiftCodeA(swiftCodeA);
        swift.setSwiftCodeB(swiftCodeB);
        swift.setSwiftMessage(swiftMessage);
        swift.setTransactionDate(transactionDate);
        swift.setCountryA(countryA);
        swift.setCountryB(countryB);

        entityManager.persist(swift);
        entityManager.flush();

        return swift.getSwiftId();
    }
}
