package ejb.mas.session;

import javax.ejb.Local;

@Local
public interface SWIFTSessionBeanLocal {
    public Long addNewSwift(String swiftMessage, String transactionDate, String swiftCodeA,
            String swiftCodeB, String organizationA, String organizationB,
            String countryA, String countryB);
}
