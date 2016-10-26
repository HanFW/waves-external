package managedbean.otherbanks;

import ejb.otherbanks.entity.OtherBankAccountTransaction;
import ejb.otherbanks.session.OtherTransactionSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "viewTransactionManagedBean")
@SessionScoped

public class ViewTransactionManagedBean implements Serializable {

    @EJB
    private OtherTransactionSessionBeanLocal otherTransactionSessionBeanLocal;

    private String bankAccountNum;

    public ViewTransactionManagedBean() {
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public List<OtherBankAccountTransaction> getTransaction() throws IOException {

        List<OtherBankAccountTransaction> transaction = otherTransactionSessionBeanLocal.retrieveAccTransactionByBankNum(bankAccountNum);

        return transaction;
    }
}
