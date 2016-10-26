package ejb.mas.session;

import ejb.mas.entity.SACH;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SACHSessionBeanLocal {

    public void SACHTransferMTD(String fromBankAccount, String toBankAccount, Double transferAmt);

    public SACH retrieveSACHById(Long sachId);

    public Long addNewSACH(Double otherTotalCredit, Double merlionTotalCredit,
            String currentTime, String bankNames, String paymentMethod, String creditAccountNum,
            String creditBank, String debitAccountNum, String debitBank, Long currentTimeMilis,
            Double creditAmt);

    public List<SACH> getAllSACH(String bankNames);

    public void SACHTransferDTM(String fromBankAccount, String toBankAccount, Double transferAmt);

    public void ForwardPaymentInstructionToMEPS();

    public void ntucInitiateGIRO(Long billId);

    public void clearMerlionReceivedCheque(Long chequeId);

    public void clearDBSReceivedCheque(Long chequeId);
}
