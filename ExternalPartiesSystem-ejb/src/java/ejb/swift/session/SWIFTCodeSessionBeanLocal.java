package ejb.swift.session;

import ejb.swift.entity.SWIFTCode;
import javax.ejb.Local;

@Local
public interface SWIFTCodeSessionBeanLocal {
    public SWIFTCode retrieveSWIFTBySWIFTCode(String swiftCode);
}
