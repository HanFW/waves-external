
package ejb.common.util;

import javax.ejb.Local;

@Local
public interface EjbTimerSessionBeanLocal {
    public void createTimer10000MS();
    public void cancelTimer10000MS();
    
    public void createTimer50000MS();
    public void cancelTimer50000MS();
    
    public void createTimer30000MS();
    public void cancelTimer30000MS();
    
    public void createTimer40000MS();
    public void cancelTimer40000MS();
}
