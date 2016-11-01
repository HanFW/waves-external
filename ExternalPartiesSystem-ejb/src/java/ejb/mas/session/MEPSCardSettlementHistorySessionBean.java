/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.mas.session;

import ejb.mas.entity.MEPS;
import ejb.mas.entity.MEPSMasterBankAccount;
import ejb.mas.entity.Settlement;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class MEPSCardSettlementHistorySessionBean implements MEPSCardSettlementHistorySessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    MEPSMasterBankAccountSessionBeanLocal mEPSMasterBankAccountSessionBeanLocal;

    @PersistenceContext
    EntityManager em;

    @Override
    public List<MEPS> getAllVisaAndMerlionSettlements() {

        List<MEPS> settlements = new ArrayList<>();
        Query q = em.createQuery("select m from MEPS m where m.bankNames=:bankNames");
        q.setParameter("bankNames", "Merlion & Visa network");

        if (!q.getResultList().isEmpty()) {
            settlements = q.getResultList();
        }

        return settlements;
    }

    @Override
    public List<MEPS> getAllCitiAndVisaSettlements() {
        List<MEPS> settlements = new ArrayList<>();
        Query q = em.createQuery("select m from MEPS m where m.bankNames=:bankNames");
        q.setParameter("bankNames", "Visa network & CitiBank");

        if (!q.getResultList().isEmpty()) {
            settlements = q.getResultList();
        }

        return settlements;
    }

    @Override
    public List<MEPS> getAllMasterCardAndMerlionSettlements() {
        List<MEPS> settlements = new ArrayList<>();
        Query q = em.createQuery("select m from MEPS m where m.bankNames=:bankNames");
        q.setParameter("bankNames", "Merlion & MasterCard network");

        if (!q.getResultList().isEmpty()) {
            settlements = q.getResultList();
        }

        return settlements;
    }

    @Override
    public List<MEPS> getAllCitiAndMasterCardSettlements() {
        List<MEPS> settlements = new ArrayList<>();
        Query q = em.createQuery("select m from MEPS m where m.bankNames=:bankNames");
        q.setParameter("bankNames", "MasterCard Network & CitiBank");

        if (!q.getResultList().isEmpty()) {
            settlements = q.getResultList();
        }

        return settlements;
    }

}
