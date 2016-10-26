package ejb.mas.session;

import ejb.mas.entity.CHIPS;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CHIPSSessionBeanLocal {
    public Long addNewCHIPS(Double otherTotalCredit, Double merlionTotalCredit,
            String currentTime, String bankNames, String paymentMethod, String creditAccountNum,
            String creditBank, String debitAccountNum, String debitBank, Long currentTimeMilis,
            Double creditAmt);
    public void clearSWIFTTransferMTK(Long swiftId);
    public CHIPS retrieveCHIPSById(Long chipsId);
    public void ForwardPaymentInstructionToMEPS();
    public List<CHIPS> getAllCHIPS(String bankNames);
}
