package ejb.ws.session;

import ejb.mas.entity.SACH;
import ejb.mas.session.MEPSSessionBeanLocal;
import ejb.mas.session.SACHSessionBeanLocal;
import ejb.otherbanks.session.OtherBankSessionBeanLocal;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

@WebService(serviceName = "SACHWebService")
@Stateless()

public class SACHWebService {

    @EJB
    private OtherBankSessionBeanLocal otherBankSessionBeanLocal;

    @EJB
    private MEPSSessionBeanLocal mEPSSessionBeanLocal;

    @EJB
    private SACHSessionBeanLocal sACHSessionBeanLocal;

    @WebMethod(operationName = "SACHTransferMTD")
//    @Oneway
    public void SACHTransferMTD(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum, @WebParam(name = "toBankAccountNum") String toBankAccountNum, @WebParam(name = "transferAmt") Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime,
                "DBS&Merlion", "FAST", toBankAccountNum, "DBS", fromBankAccountNum, "Merlion",
                currentTimeMilis, transferAmt);
        SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transferAmt;
        Double merlionTotalCredit = 0 - transferAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);

        otherBankSessionBeanLocal.actualMTOFastTransfer(fromBankAccountNum, toBankAccountNum, transferAmt);
        mEPSSessionBeanLocal.MEPSSettlementMTD("88776655", "44332211", transferAmt);

    }

    @WebMethod(operationName = "SACHNonStandingGIROTransferMTD")
//    @Oneway
    public void SACHNonStandingGIROTransferMTD(@WebParam(name = "fromBankAccountNum") String fromBankAccountNum,
            @WebParam(name = "toBankAccountNum") String toBankAccountNum,
            @WebParam(name = "transferAmt") Double transferAmt) {

        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        Long currentTimeMilis = cal.getTimeInMillis();

        Long sachId = sACHSessionBeanLocal.addNewSACH(0.0, 0.0, currentTime,
                "DBS&Merlion", "Non Standing GIRO", toBankAccountNum, "DBS", fromBankAccountNum,
                "Merlion", currentTimeMilis, transferAmt);
        SACH sach = sACHSessionBeanLocal.retrieveSACHById(sachId);

        Double dbsTotalCredit = 0 + transferAmt;
        Double merlionTotalCredit = 0 - transferAmt;

        sach.setBankBTotalCredit(dbsTotalCredit);
        sach.setBankATotalCredit(merlionTotalCredit);
    }
}
