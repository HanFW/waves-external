package ejb.common.util;

import ejb.mas.session.CHIPSSessionBeanLocal;
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

    @Resource
    private SessionContext ctx;

    @PersistenceContext
    private EntityManager entityManager;

    private final String TIMER_NAME_10000MS = "EJB-TIMER-10000MS";
    private final int TIMER_DURATION_10000MS = 10000;

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

    @Timeout
    public void handleTimeout(Timer timer) {
        if (timer.getInfo().toString().equals(TIMER_NAME_10000MS)) {
            handleTimeout_10000ms();
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

    private void settleEachBankAccount() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionBank.MerlionBankWebService port = service_merlionBank.getMerlionBankWebServicePort();
        port.settleEachBankAccount();
    }
}
