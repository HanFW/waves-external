package ejb.mas.session;

import ejb.mas.entity.SWIFTCode;
import javax.ejb.Local;

@Local
public interface SWIFTCodeSessionBeanLocal {
    public SWIFTCode retrieveSWIFTBySWIFTCode(String swiftCode);
}
