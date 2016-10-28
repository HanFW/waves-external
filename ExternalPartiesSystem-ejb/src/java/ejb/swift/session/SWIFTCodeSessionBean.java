package ejb.swift.session;

import ejb.swift.entity.SWIFTCode;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class SWIFTCodeSessionBean implements SWIFTCodeSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SWIFTCode retrieveSWIFTBySWIFTCode(String swiftCode) {
        SWIFTCode receivedInstitutionSwiftCode = new SWIFTCode();

        try {
            Query query = entityManager.createQuery("Select s From SWIFTCode s Where s.swiftCode=:swiftCode");
            query.setParameter("swiftCode", swiftCode);

            if (query.getResultList().isEmpty()) {
                return new SWIFTCode();
            } else {
                receivedInstitutionSwiftCode = (SWIFTCode) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SWIFTCode();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return receivedInstitutionSwiftCode;
    }

}
