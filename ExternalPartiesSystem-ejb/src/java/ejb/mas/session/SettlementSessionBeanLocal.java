package ejb.mas.session;

import ejb.mas.entity.SACH;
import ejb.mas.entity.Settlement;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SettlementSessionBeanLocal {

    public Settlement retrieveSettlementById(Long settlementId);

    public Long addNewSettlement(String dailySettlementAmt, String dailySettlementRef,
            String updateDate, String bankNames);

    public void recordSettlementInformation(List<SACH> sachs, String creditBank, String debitBank);
    
    public List<Settlement> getAllSettlement();
}
