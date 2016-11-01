/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.mas.session;

import ejb.mas.entity.MEPS;
import ejb.mas.entity.Settlement;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface MEPSCardSettlementHistorySessionBeanLocal {

    public List<MEPS> getAllVisaAndMerlionSettlements();

    public List<MEPS> getAllCitiAndVisaSettlements();

    public List<MEPS> getAllMasterCardAndMerlionSettlements();

    public List<MEPS> getAllCitiAndMasterCardSettlements();


}
