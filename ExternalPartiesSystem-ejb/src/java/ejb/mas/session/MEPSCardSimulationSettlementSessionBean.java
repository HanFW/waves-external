/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.mas.session;

import ejb.card.entity.MasterCardClearingNetwork;
import ejb.card.entity.VisaClearingNetwork;
import ejb.card.session.MasterCardNetworkClearingSessionBeanLocal;
import ejb.card.session.VisaNetworkClearingSessionBeanLocal;
import ejb.mas.entity.MEPSMasterBankAccount;
import ejb.mas.entity.Settlement;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class MEPSCardSimulationSettlementSessionBean implements MEPSCardSimulationSettlementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    MEPSMasterBankAccountSessionBeanLocal mEPSMasterBankAccountSessionBeanLocal;

    @EJB
    SettlementSessionBeanLocal settlementSessionBeanLocal;

    @EJB
    MEPSSessionBeanLocal mEPSSessionBeanLocal;

    @EJB
    MEPSMasterAccountTransactionSessionBeanLocal mEPSMasterAccountTransactionSessionBeanLocal;

    @EJB
    MasterCardNetworkClearingSessionBeanLocal masterCardNetworkClearingSessionBeanLocal;

    @EJB
    VisaNetworkClearingSessionBeanLocal visaNetworkClearingSessionBeanLocal;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void recordSettlementInformationOfVisa() {
        System.out.println("record visa settlements session bean");
        List<VisaClearingNetwork> visaRecords = visaNetworkClearingSessionBeanLocal.getAllVisaRecords();
        System.out.println("test settlement records: visaRecords" + visaRecords);

        Double dailySettlementAmt = 0.0;
        String dailySettlementRef = "";

        for (int i = 0; i < visaRecords.size(); i++) {
            dailySettlementAmt = dailySettlementAmt + visaRecords.get(i).getTransactionAmt();
            visaRecords.get(i).setStatus("done");
            entityManager.flush();
        }

        if (dailySettlementAmt != 0) {
            MEPSMasterBankAccount merlionBanksMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("merlionBank");
            MEPSMasterBankAccount visaNetworkMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("visa");
            MEPSMasterBankAccount citiBankMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("citi");

            String merlionBankMEPSBankAccountNum = merlionBanksMasterBankAccount.getMasterBankAccountNum();
            String visaMEPSBankAccountNum = visaNetworkMasterBankAccount.getMasterBankAccountNum();
            String citiBankMEPSBankAccountNum = citiBankMasterBankAccount.getMasterBankAccountNum();

            Calendar cal = Calendar.getInstance();
            String updateDate = cal.getTime().toString();

            Long settlementId = settlementSessionBeanLocal.addNewSettlement(dailySettlementAmt.toString(), "Visa network settlement",
                    updateDate, "", "New", "merlion", visaMEPSBankAccountNum,
                    "visa", merlionBankMEPSBankAccountNum, "visa network");
            Long settlemetId2 = settlementSessionBeanLocal.addNewSettlement(dailySettlementAmt.toString(), "Visa network settlement",
                    updateDate, "", "New", "visa", citiBankMEPSBankAccountNum,
                    "citi", visaMEPSBankAccountNum, "visa network");

        }//total transaction amt is not zero
        else {
            dailySettlementRef = "Not Applicable";
        }// no need settlement
    }

    @Override
    public void recordSettlementInformationOfMasterCard() {
        List<MasterCardClearingNetwork> masterCardRecords = masterCardNetworkClearingSessionBeanLocal.getAllMasterCardRecords();

        Double dailySettlementAmt = 0.0;
        String dailySettlementRef = "";

        for (int i = 0; i < masterCardRecords.size(); i++) {
            dailySettlementAmt = dailySettlementAmt + masterCardRecords.get(i).getTransactionAmt();
            masterCardRecords.get(i).setStatus("done");
            entityManager.flush();
        }

        if (dailySettlementAmt != 0) {
            MEPSMasterBankAccount merlionBanksMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("merlionBank");
            MEPSMasterBankAccount masterCardNetworkMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("masterCard");
            MEPSMasterBankAccount citiBankMasterBankAccount = mEPSMasterBankAccountSessionBeanLocal.retrieveBankAccountByBankName("citi");

            String merlionBankMEPSBankAccountNum = merlionBanksMasterBankAccount.getMasterBankAccountNum();
            String masterCardMEPSBankAccountNum = masterCardNetworkMasterBankAccount.getMasterBankAccountNum();
            String citiBankMEPSBankAccountNum = citiBankMasterBankAccount.getMasterBankAccountNum();

            Calendar cal = Calendar.getInstance();
            String updateDate = cal.getTime().toString();

            Long settlementId = settlementSessionBeanLocal.addNewSettlement(dailySettlementAmt.toString(), "MasterCard network settlement",
                    updateDate, "", "New", "merlion", masterCardMEPSBankAccountNum,
                    "masterCard", merlionBankMEPSBankAccountNum, "masterCard network");
            Long settlemetId2 = settlementSessionBeanLocal.addNewSettlement(dailySettlementAmt.toString(), "MasterCard network settlement",
                    updateDate, "", "New", "masterCard", citiBankMEPSBankAccountNum,
                    "citi", masterCardMEPSBankAccountNum, "masterCard network");

        }//total transaction amt is not zero
        else {
            dailySettlementRef = "Not Applicable";
        }// no need settlement
    }

    private void MEPSSettlementTransaction(List<Settlement> settlements) {

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
                System.out.println("****");
                Double totalDebitBankAccountBalance = Double.valueOf(currentDebitBankAccountBalance) - Double.valueOf(dailySettlementAmt);

                creditMasterBankAccount.setMasterBankAccountBalance(totalCreditBankAccountBalance.toString());
                debitMasterBankAccount.setMasterBankAccountBalance(totalDebitBankAccountBalance.toString());

                settlement.setSettlementStatus("done");
                entityManager.flush();

                Calendar cal = Calendar.getInstance();
                String currentTime = cal.getTime().toString();
                String settlementRef = debitMasterBankAccount.getBankName() + " pays S$" + dailySettlementAmt + " to "
                        + creditMasterBankAccount.getBankName();

                String bankNames = "";
                if (creditMasterBankAccount.getBankName().equals("visa")) {
                    bankNames = "Merlion & Visa network";
                } else if (creditMasterBankAccount.getBankName().equals("masterCard")) {
                    bankNames = "Merlion & MasterCard network";
                } else if (debitMasterBankAccount.getBankName().equals("visa")) {
                    bankNames = "Visa network & CitiBank";
                } else if (debitMasterBankAccount.getBankName().equals("masterCard")) {
                    bankNames = "MasterCard network & CitiBank";
                } else {
                    bankNames = debitMasterBankAccount.getBankName() + "&" + creditMasterBankAccount.getBankName();
                }
                Long newMepsId = mEPSSessionBeanLocal.addNewMEPS(settlementRef, currentTime, bankNames);

                String creditTransactionRef = creditMasterBankAccount.getBankName() + " had received S$" + dailySettlementAmt + " from "
                        + debitMasterBankAccount.getBankName();
                String debitTransactionRef = debitMasterBankAccount.getBankName() + " had paid S$" + dailySettlementAmt + " to "
                        + creditMasterBankAccount.getBankName();

                Long creditAccountTransactionId = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                        creditTransactionRef, " ", dailySettlementAmt, creditMasterBankAccount.getMasterBankAccountId());
                Long debitAccountTransactionId = mEPSMasterAccountTransactionSessionBeanLocal.addNewMasterAccountTransaction(currentTime,
                        debitTransactionRef, dailySettlementAmt, " ", debitMasterBankAccount.getMasterBankAccountId());

            }
        }//end else
    }

    @Override
    public void MEPSVisaSettlementVTC() {
        List<Settlement> settlements = settlementSessionBeanLocal.getAllVisaNetworkVTCSettlement();
        System.out.println("managedBean MEPSVisaSettlementVTC():" + settlements);
        MEPSSettlementTransaction(settlements);
    }

    @Override
    public void MEPSVisaSettlementMTV() {
        List<Settlement> settlements = settlementSessionBeanLocal.getAllVisaNetworkMTVSettlement();
        System.out.println("managedBean MEPSVisaSettlementMTV():" + settlements);
        MEPSSettlementTransaction(settlements);
    }

    @Override
    public void MEPSMasterCardSettlementMTM() {
        List<Settlement> settlements = settlementSessionBeanLocal.getAllMasterCardNetworkMTMSettlement();
        System.out.println("managedBean MEPSMasterCardSettlementMTM():" + settlements);
        MEPSSettlementTransaction(settlements);
    }

    @Override
    public void MEPSMasterCardSettlementMTC() {
        List<Settlement> settlements = settlementSessionBeanLocal.getAllMasterCardNetworkMTCSettlement();
        System.out.println("managedBean MEPSMasterCardSettlementMTC():" + settlements);
        MEPSSettlementTransaction(settlements);
    }

}
