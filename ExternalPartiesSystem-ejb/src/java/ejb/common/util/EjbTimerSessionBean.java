package ejb.common.util;

import ejb.chips.session.CHIPSSessionBeanLocal;
import ejb.mas.session.MEPSCardSimulationSettlementSessionBeanLocal;
import ejb.mas.session.MEPSSessionBeanLocal;
import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionBank.MerlionBankWebService_Service;

@Stateless
@LocalBean

public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @EJB
    private CHIPSSessionBeanLocal cHIPSSessionBeanLocal;

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/MerlionBankWebService/MerlionBankWebService.wsdl")
    private MerlionBankWebService_Service service_merlionBank;

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @EJB
    private MEPSSessionBeanLocal mEPSSessionBeanLocal;

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @EJB
    private MEPSCardSimulationSettlementSessionBeanLocal mEPSCardSimulationSettlementSessionBeanLocal;

    @Resource
    private SessionContext ctx;

    @PersistenceContext
    private EntityManager entityManager;

    private final String TIMER_NAME_10000MS = "EJB-TIMER-10000MS";
    private final int TIMER_DURATION_10000MS = 10000;

    private final String TIMER_NAME_30000MS = "EJB-TIMER-30000MS";
    private final int TIMER_DURATION_30000MS = 30000;

    private final String TIMER_NAME_40000MS = "EJB-TIMER-40000MS";
    private final int TIMER_DURATION_40000MS = 40000;

    private final String TIMER_NAME_50000MS = "EJB-TIMER-50000MS";
    private final int TIMER_DURATION_50000MS = 50000;

    public EjbTimerSessionBean() {

    }

    public EjbTimerSessionBean(String bankAccountNum) {

    }

    @Override
    public void createTimer10000MS() {
        TimerService timerService = ctx.getTimerService();

        Timer timer10000ms = timerService.createTimer(TIMER_DURATION_10000MS,
                TIMER_DURATION_10000MS, new String(TIMER_NAME_10000MS));
        System.out.println("{***10000MS Timer created" + String.valueOf(timer10000ms.getTimeRemaining()) + ","
                + timer10000ms.getInfo().toString());
    }

    @Override
    public void cancelTimer10000MS() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            Timer timer = (Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_NAME_10000MS));
            {
                timer.cancel();
                System.out.println("*** 10000MS Timer cancelled");
            }
        }
    }

    @Override
    public void createTimer30000MS() {
        TimerService timerService = ctx.getTimerService();

        Timer timer30000ms = timerService.createTimer(TIMER_DURATION_30000MS,
                TIMER_DURATION_30000MS, new String(TIMER_NAME_30000MS));

        System.out.println("{***30000MS Timer created" + String.valueOf(timer30000ms.getTimeRemaining()) + ","
                + timer30000ms.getInfo().toString());

    }

    @Override
    public void cancelTimer30000MS() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            Timer timer = (Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_NAME_30000MS));
            {
                timer.cancel();
                System.out.println("*** 30000MS Timer cancelled");
            }
        }
    }

    @Override
    public void createTimer40000MS() {
        TimerService timerService = ctx.getTimerService();

        Timer timer40000ms = timerService.createTimer(TIMER_DURATION_40000MS,
                TIMER_DURATION_40000MS, new String(TIMER_NAME_40000MS));

        System.out.println("{***40000MS Timer created" + String.valueOf(timer40000ms.getTimeRemaining()) + ","
                + timer40000ms.getInfo().toString());
    }

    @Override
    public void cancelTimer40000MS() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            Timer timer = (Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_NAME_40000MS));
            {
                timer.cancel();
                System.out.println("*** 40000MS Timer cancelled");
            }
        }
    }

    @Override
    public void createTimer50000MS() {
        TimerService timerService = ctx.getTimerService();

        Timer timer50000ms = timerService.createTimer(TIMER_DURATION_50000MS,
                TIMER_DURATION_50000MS, new String(TIMER_NAME_50000MS));

        System.out.println("{***50000MS Timer created" + String.valueOf(timer50000ms.getTimeRemaining()) + ","
                + timer50000ms.getInfo().toString());
    }

    @Override
    public void cancelTimer50000MS() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            Timer timer = (Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_NAME_50000MS));
            {
                timer.cancel();
                System.out.println("*** 50000MS Timer cancelled");
            }
        }
    }

    @Timeout
    public void handleTimeout(Timer timer) {
        if (timer.getInfo().toString().equals(TIMER_NAME_10000MS)) {
            handleTimeout_10000ms();
        } else if (timer.getInfo().toString().equals(TIMER_NAME_30000MS)) {
            handleTimeout_30000ms();
        } else if (timer.getInfo().toString().equals(TIMER_NAME_40000MS)) {
            handleTimeout_40000ms();
        } else if (timer.getInfo().toString().equals(TIMER_NAME_50000MS)) {
            handleTimeout_50000ms();
        }
    }

    private void handleTimeout_10000ms() {
        System.out.println("*** 10000MS Timer timeout");

        sACHSessionBeanLocal.ForwardPaymentInstructionToMEPS();
        cHIPSSessionBeanLocal.ForwardPaymentInstructionToMEPS();
        mEPSSessionBeanLocal.MEPSSettlement();
        otherBankSessionBeanLocal.settleEachOtherBankAccount();
        settleEachBankAccount();

    }

    private void handleTimeout_30000ms() {
        System.out.println("*** 30000MS Timer timeout");

        mEPSCardSimulationSettlementSessionBeanLocal.recordSettlementInformationOfMasterCard();
        System.out.println("record settlement info of masterCard");
        mEPSCardSimulationSettlementSessionBeanLocal.recordSettlementInformationOfVisa();
        System.out.println("record settlement info of visa");
        otherBankSessionBeanLocal.merchantVisaNetworkSettlement();
        System.out.println("citibank pays merchant - visa network");
        mEPSCardSimulationSettlementSessionBeanLocal.MEPSMasterCardSettlementMTM();
        System.out.println("MEPS settlement merlion to masterCard");
    }

    private void handleTimeout_40000ms() {
        System.out.println("*** 40000MS Timer timeout");

        mEPSCardSimulationSettlementSessionBeanLocal.MEPSVisaSettlementVTC();
        System.out.println("MEPS settlement visa to citibank");
        mEPSCardSimulationSettlementSessionBeanLocal.MEPSMasterCardSettlementMTC();
        System.out.println("MEPS settlement masterCard to citibank");
    }

    private void handleTimeout_50000ms() {
        System.out.println("*** 50000MS Timer timeout");

        mEPSCardSimulationSettlementSessionBeanLocal.MEPSVisaSettlementMTV();
        System.out.println("MEPS settlement merlion bank to visa");
        otherBankSessionBeanLocal.merchantMasterCardNetworkSettlement();
        System.out.println("citibank pays merchant - masterCard network");
    }

    private void settleEachBankAccount() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.settleEachBankAccount();
    }
}
