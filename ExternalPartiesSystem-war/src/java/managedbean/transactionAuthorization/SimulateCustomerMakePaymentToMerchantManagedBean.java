/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.transactionAuthorization;

import ejb.card.entity.TransactionToBeAuthorized;
import ejb.card.session.MasterCardNetworkClearingSessionBeanLocal;
import ejb.card.session.TransactionAutorizationSessionBeanLocal;
import ejb.card.session.VisaNetworkClearingSessionBeanLocal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jingyuan
 */
@Named(value = "simulateCustomerMakePaymentToMerchantManagedBean")
@RequestScoped
public class SimulateCustomerMakePaymentToMerchantManagedBean {

    /**
     * Creates a new instance of
     * SimulateCustomerMakePaymentToMerchantManagedBean
     */
    
    public SimulateCustomerMakePaymentToMerchantManagedBean() {
    }

    private String cardType;
    private String cardNum;
    private boolean debitCard;
    private boolean creditCard;
    private String debitCardPwd;
    private String csc;
    private String customerSignature;
    private double transactionAmt;
    private TransactionToBeAuthorized transaction;

    @EJB
    private TransactionAutorizationSessionBeanLocal transactionAutorizationSessionBeanLocal;

    @EJB
    private VisaNetworkClearingSessionBeanLocal visaNetworkClearingSessionBeanLocal;

    @EJB
    private MasterCardNetworkClearingSessionBeanLocal masterCardNetworkClearingSessionBeanLocal;

    public void showPanel() {
        if (cardType.equals("debit")) {
            debitCard = true;
            System.err.println("debitCard: "+debitCard);
        } else {
            creditCard = true;
        }
    }

    public void forwardTransactionAuthorization() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        DateFormat df = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        Date transactionTime1 = new Date();
        String transactionTime = df.format(transactionTime1);

        if (cardType.equals("debit")) {
            transaction = transactionAutorizationSessionBeanLocal.createTransactionToBeAuthorized("debit", cardNum, null, debitCardPwd, transactionAmt, transactionTime, "watsons");
        } else if (cardType.equals("credit")) {
            transaction = transactionAutorizationSessionBeanLocal.createTransactionToBeAuthorized("credit", cardNum, customerSignature, null, transactionAmt, transactionTime, "watsons");
        }
        System.out.println("transaction forwarded from merchant!");

        Long id = transaction.getTransactionToBeAuthorizedId();
        String result = transactionAutorizationSessionBeanLocal.checkTransactionAuthorization(id);
        if (result.equals("not authorized")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transaction unauthorized!", null);
        } else {
            String[] info = result.split("-");
            String cardNetwork = info[1];
            
            transactionAutorizationSessionBeanLocal.updateTransactionStatus(transaction.getTransactionToBeAuthorizedId());
            
           
            if (cardNetwork.equals("Visa")) {
                visaNetworkClearingSessionBeanLocal.createNewVisaClearingRecord(transactionAmt, "Watsons", transactionTime, "new", "watsons");
            } else {
                masterCardNetworkClearingSessionBeanLocal.createNewMasterCardClearingRecord(transactionAmt, "Watsons", transactionTime, "new", "watsons");
            }
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tranaction authorized!", null);

        }
        context.addMessage(null, message);

    }

    public String getCardType() {
        System.out.println("cardType: " + cardType);
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
        System.out.println("set cardType: " + cardType);
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
        System.out.println("set cardNum: " + cardNum);
    }

    public boolean isDebitCard() {
        return debitCard;
    }

    public void setDebitCard(boolean debiCard) {
        this.debitCard = debiCard;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
        System.out.println("set cardPwd: " + debitCardPwd);
    }

    public String getCsc() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc = csc;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
        System.out.println("set signature: " + customerSignature);
    }

    public double getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(double transactionAmt) {
        this.transactionAmt = transactionAmt;
        System.out.println("set transaction amount: " + transactionAmt);
    }

}
