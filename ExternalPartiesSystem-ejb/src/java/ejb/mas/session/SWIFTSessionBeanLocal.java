package ejb.mas.session;

import ejb.mas.entity.SWIFT;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SWIFTSessionBeanLocal {
    public List<SWIFT> getAllSWIFT();
    public SWIFT retrieveSWIFTById(Long swiftId);
    public void sendMessageToReceivedInstitution(Long swiftId);
}
