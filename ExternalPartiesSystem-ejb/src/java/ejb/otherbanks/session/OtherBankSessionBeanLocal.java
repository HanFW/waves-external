package ejb.otherbanks.session;

import javax.ejb.Local;

@Local
public interface OtherBankSessionBeanLocal {
    public void actualMTOFastTransfer(String fromAccountNum, String toAccountNum, Double transferAmt);
    public void creditPaymentToAccountMTD(String fromBankAccountNum, String toBankAccountNum, Double paymentAmt);
}
