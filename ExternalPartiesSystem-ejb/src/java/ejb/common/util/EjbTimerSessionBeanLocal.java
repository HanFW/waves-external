
package ejb.common.util;

import javax.ejb.Local;

@Local
public interface EjbTimerSessionBeanLocal {
    public void createTimer10000MS();
    public void cancelTimer10000MS();
}
