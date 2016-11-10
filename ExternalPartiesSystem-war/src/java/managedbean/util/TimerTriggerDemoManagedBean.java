package managedbean.util;

import ejb.common.util.EjbTimerSessionBeanLocal;
import ejb.common.util.demoSessionBean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "timerTriggerDemoManagedBean")
@RequestScoped

public class TimerTriggerDemoManagedBean {
    @EJB
    private demoSessionBean demoSessionBean;

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    private ExternalContext ec;
    
    public TimerTriggerDemoManagedBean() {
    }
    
    public void create10000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.createTimer10000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 10000ms Timer Successfully", "Successfully"));
    }

    public void cancel10000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancelTimer10000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 10000ms Timer Successfully", "Successfully"));
    }
    
    public void create30000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.createTimer30000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 30000ms Timer Successfully", "Successfully"));
    }

    public void cancel30000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancelTimer30000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 30000ms Timer Successfully", "Successfully"));
    }
    
    public void create40000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.createTimer40000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 40000ms Timer Successfully", "Successfully"));
    }

    public void cancel40000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancelTimer40000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 40000ms Timer Successfully", "Successfully"));
    }
    
    public void create50000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.createTimer50000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Create 50000ms Timer Successfully", "Successfully"));
    }

    public void cancel50000msTimer() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        demoSessionBean.cancelTimer50000();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cancel 50000ms Timer Successfully", "Successfully"));
    }
    
}
