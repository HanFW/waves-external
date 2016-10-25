package ejb.mas.session;

import ejb.mas.entity.MEPS;
import ejb.mas.entity.MEPSMasterBankAccount;
import ejb.mas.entity.Settlement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class MEPSSessionBean implements MEPSSessionBeanLocal {

    @EJB
    private MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    @EJB
    private MEPSMasterBankAccountSessionBeanLocal mEPSMasterBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewMEPS(String settlementRef, String settlementDate, String bankNames) {

        MEPS meps = new MEPS();

        meps.setSettlementRef(settlementRef);
        meps.setSettlementDate(settlementDate);
        meps.setBankNames(bankNames);

        entityManager.persist(meps);
        entityManager.flush();

        return meps.getMepsId();
    }

    @Override
    public void MEPSSettlementMTD(String fromMasterBankAccountNum, String toMasterBankAccountNum, Double transferAmt) {

        DecimalFormat df = new DecimalFormat("#.00");

        MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(fromMasterBankAccountNum);
        MEPSMasterBankAccount dbsMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(toMasterBankAccountNum);

        String settlementRefMerlion = "Pay " + "S$" + transferAmt + " to DBS";
        String settlementRefDBS = "Receive " + "S$" + transferAmt + " from Merlion Bank";
        String settlementRef = "Merlion Bank pays DBS S$" + transferAmt;

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";

        Long newMepsId = addNewMEPS(settlementRef, currentTime, bankNames);

        Double merlionCurrentBalance = Double.valueOf(merlionMasterBankAccount.getMasterBankAccountBalance()) - transferAmt;
        Double dbsCurrentBalance = Double.valueOf(dbsMasterBankAccount.getMasterBankAccountBalance()) + transferAmt;

        Long newDBSMasterAccountTransaction = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                settlementRefDBS, " ", transferAmt.toString(), dbsMasterBankAccount.getMasterBankAccountId());
        Long newMerlionMasterAccountTransaction = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                settlementRefMerlion, transferAmt.toString(), " ", merlionMasterBankAccount.getMasterBankAccountId());

        merlionMasterBankAccount.setMasterBankAccountBalance(df.format(merlionCurrentBalance));
        dbsMasterBankAccount.setMasterBankAccountBalance(df.format(dbsCurrentBalance));
    }

    @Override
    public List<MEPS> getAllMEPS(String bankNames) {

        Query query = entityManager.createQuery("SELECT m FROM MEPS m Where m.bankNames=:bankNames");
        query.setParameter("bankNames", bankNames);

        return query.getResultList();
    }

    @Override
    public void MEPSSettlementDTM(String fromMasterBankAccountNum, String toMasterBankAccountNum, Double transferAmt) {

        DecimalFormat df = new DecimalFormat("#.00");

        MEPSMasterBankAccount merlionMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(toMasterBankAccountNum);
        MEPSMasterBankAccount dbsMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(fromMasterBankAccountNum);

        String settlementRefMerlion = "Receive " + "S$" + transferAmt + " from DBS";
        String settlementRefDBS = "Pay " + "S$" + transferAmt + " to Merlion Bank";
        String settlementRef = "DBS pays Merlion Bank S$" + transferAmt;

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        String bankNames = "DBS&Merlion";

        Long newMepsId = addNewMEPS(settlementRef, currentTime, bankNames);

        Double merlionCurrentBalance = Double.valueOf(merlionMasterBankAccount.getMasterBankAccountBalance()) + transferAmt;
        Double dbsCurrentBalance = Double.valueOf(dbsMasterBankAccount.getMasterBankAccountBalance()) - transferAmt;

        Long newDBSMasterAccountTransaction = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                settlementRefDBS, transferAmt.toString(), " ", dbsMasterBankAccount.getMasterBankAccountId());
        Long newMerlionMasterAccountTransaction = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                settlementRefMerlion, " ", transferAmt.toString(), merlionMasterBankAccount.getMasterBankAccountId());

        merlionMasterBankAccount.setMasterBankAccountBalance(df.format(merlionCurrentBalance));
        dbsMasterBankAccount.setMasterBankAccountBalance(df.format(dbsCurrentBalance));
    }

    @Override
    public void MEPSSettlement() {

        Query query = entityManager.createQuery("SELECT s FROM Settlement s WHERE s.settlementStatus = :settlementStatus");
        query.setParameter("settlementStatus", "New");
        List<Settlement> settlements = query.getResultList();

        if (settlements.isEmpty()) {
            System.out.println("No settlement");
        } else {
            for (Settlement settlement : settlements) {

                String creditMEPSBankAccountNum = settlement.getCreditMEPSBankAccountNum();
                String dailySettlementAmt = settlement.getDailySettlementAmt();
                String debitMEPSBankAccountNum = settlement.getDebitMEPSBankAccountNum();

                MEPSMasterBankAccount creditMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(creditMEPSBankAccountNum);
                MEPSMasterBankAccount debitMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveMEPSMasterBankAccountByAccNum(debitMEPSBankAccountNum);

                String currentCreditBankAccountBalance = creditMasterBankAccount.getMasterBankAccountBalance();
                String currentDebitBankAccountBalance = debitMasterBankAccount.getMasterBankAccountBalance();

                Double totalCreditBankAccountBalance = Double.valueOf(currentCreditBankAccountBalance) + Double.valueOf(dailySettlementAmt);
                Double totalDebitBankAccountBalance = Double.valueOf(currentDebitBankAccountBalance) + Double.valueOf(dailySettlementAmt);

                creditMasterBankAccount.setMasterBankAccountBalance(totalCreditBankAccountBalance.toString());
                debitMasterBankAccount.setMasterBankAccountBalance(totalDebitBankAccountBalance.toString());

                settlement.setSettlementStatus("Done");

                Calendar cal = Calendar.getInstance();
                String currentTime = cal.getTime().toString();
                String settlementRef = debitMasterBankAccount.getBankName() + " pays S$" + dailySettlementAmt + " to "
                        + creditMasterBankAccount.getBankName();
                String bankNames = creditMasterBankAccount.getBankName() + "&" + debitMasterBankAccount.getBankName();
                Long newMepsId = addNewMEPS(settlementRef, currentTime, bankNames);

                String creditTransactionRef = creditMasterBankAccount.getBankName() + " had received S$" + dailySettlementAmt + " from "
                        + debitMasterBankAccount.getBankName();
                String debitTransactionRef = debitMasterBankAccount.getBankName() + " had paid S$" + dailySettlementAmt + " to "
                        + creditMasterBankAccount.getBankName();

                Long creditAccountTransactionId = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                        creditTransactionRef, " ", dailySettlementAmt, creditMasterBankAccount.getMasterBankAccountId());
                Long debitAccountTransactionId = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                        debitTransactionRef, dailySettlementAmt, " ", debitMasterBankAccount.getMasterBankAccountId());

            }
        }
    }
}
