package ejb.otherbanks.session;

import javax.ejb.Local;

@Local
public interface OtherBankSessionBeanLocal {

    public void actualMTOFastTransfer(String fromAccountNum, String toAccountNum, Double transferAmt);

    public void creditPaymentToAccountMTD(String fromBankAccountNum, String toBankAccountNum,
            Double paymentAmt);

    public void askForCreditOtherBankAccount(Long billId);

    public void settleEachOtherBankAccount();

    public void creditPayementToAccountMTK(String fromBankAccountNum, String toBankAccountNum, Double paymentAmt);
    
    public void merchantVisaNetworkSettlement();
    
    public void merchantMasterCardNetworkSettlement();
}
