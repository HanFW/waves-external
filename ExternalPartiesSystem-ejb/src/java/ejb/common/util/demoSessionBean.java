package ejb.common.util;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

@Singleton
@LocalBean
public class demoSessionBean {

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionLocal;
    
    public void createTimer10000() {
        ejbTimerSessionLocal.createTimer10000MS();
    }
    
    public void cancelTimer10000() {
        ejbTimerSessionLocal.cancelTimer10000MS();
    }
    
    public void createTimer30000() {
        ejbTimerSessionLocal.createTimer30000MS();
    }
    
    public void cancelTimer30000() {
        ejbTimerSessionLocal.cancelTimer30000MS();
    }
    
    public void createTimer40000() {
        ejbTimerSessionLocal.createTimer40000MS();
    }
    
    public void cancelTimer40000() {
        ejbTimerSessionLocal.cancelTimer40000MS();
    }
    
    public void createTimer50000() {
        ejbTimerSessionLocal.createTimer50000MS();
    }
    
    public void cancelTimer50000() {
        ejbTimerSessionLocal.cancelTimer50000MS();
    }
}
