package managedbean.otherbanks;

import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.entity.OtherBankAccount;
import ejb.otherbanks.session.OtherBankAccountSessionBeanLocal;
import ejb.otherbanks.session.OtherBankChequeSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "dBSEnterChequeInformationManagedBean")
@RequestScoped

public class DBSEnterChequeInformationManagedBean {

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @EJB
    private OtherBankChequeSessionBeanLocal otherBankChequeSessionBeanLocal;

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    private String transactionAmt;
    private String receivedBankAccountNum;
    private String customerName;
    private String customerMobile;
    private String issuedBankAccountNum;
    private Integer chequeNum;

    private ExternalContext ec;

    public DBSEnterChequeInformationManagedBean() {
    }

    public String getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(String transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getReceivedBankAccountNum() {
        return receivedBankAccountNum;
    }

    public void setReceivedBankAccountNum(String receivedBankAccountNum) {
        this.receivedBankAccountNum = receivedBankAccountNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getIssuedBankAccountNum() {
        return issuedBankAccountNum;
    }

    public void setIssuedBankAccountNum(String issuedBankAccountNum) {
        this.issuedBankAccountNum = issuedBankAccountNum;
    }

    public Integer getChequeNum() {
        return chequeNum;
    }

    public void setChequeNum(Integer chequeNum) {
        this.chequeNum = chequeNum;
    }
    
    public void submit() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();

        OtherBankAccount dbsBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(receivedBankAccountNum);
        String currentAvailableBalance = dbsBankAccount.getAvailableBankAccountBalance();
        Double totalBalance = Double.valueOf(currentAvailableBalance) + Double.valueOf(transactionAmt);

        Long receivedChequeId = otherBankChequeSessionBeanLocal.addNewReceivedCheque(transactionDate,
                transactionAmt, receivedBankAccountNum, customerName, customerMobile, issuedBankAccountNum,
                chequeNum.toString(), issuedBankAccountNum);

        sACHSessionBeanLocal.clearDBSReceivedCheque(chequeNum.toString());
        sACHSessionBeanLocal.receiveChequeInformationFromOtherBank(chequeNum.toString(), transactionAmt,
                issuedBankAccountNum);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Input Received Cheque Information", ""));
    }

}
