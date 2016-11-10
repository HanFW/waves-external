package ejb.common.util;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@LocalBean
@Startup

public class initSessionBean {
    
    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionLocal;

    @PostConstruct
    public void init() {
//        ejbTimerSessionLocal.createTimer10000MS();;
//        ejbTimerSessionLocal.createTimer30000MS();
//        ejbTimerSessionLocal.createTimer40000MS();
//        ejbTimerSessionLocal.createTimer50000MS();
    }
}
