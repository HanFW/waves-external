package managedbean.swift;

import ejb.swift.entity.SWIFT;
import ejb.swift.session.SWIFTSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "viewSWIFTMessageManagedBean")
@RequestScoped

public class ViewSWIFTMessageManagedBean {

    @EJB
    private SWIFTSessionBeanLocal sWIFTSessionBeanLocal;

    public ViewSWIFTMessageManagedBean() {
    }

    public List<SWIFT> getAllSWIFTMessage() {
        List<SWIFT> swiftMessages = sWIFTSessionBeanLocal.getAllSWIFT();
        return swiftMessages;
    }
}
