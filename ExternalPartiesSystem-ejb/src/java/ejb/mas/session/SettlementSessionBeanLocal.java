package ejb.mas.session;

import ejb.mas.entity.CHIPS;
import ejb.mas.entity.SACH;
import ejb.mas.entity.Settlement;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SettlementSessionBeanLocal {

    public Settlement retrieveSettlementById(Long settlementId);

    public Long addNewSettlement(String dailySettlementAmt, String dailySettlementRef,
            String updateDate, String bankNames, String settlementStatus, String creditMEPSBank,
            String creditMEPSBankAccountNum, String debitMEPSBank, String debitMEPSBankAccountNum);

    public void recordSettlementInformation(List<SACH> sachs);

    public List<Settlement> getAllSettlement();
    
    public void recordSettlementInformationCHIPS(List<CHIPS> chips);
}
