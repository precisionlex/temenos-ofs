package com.precisionlex.utils;

import com.precisionlex.model.ReceivedDoc;
import com.precisionlex.model.RequestedPayment;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
public class Ofs {
    public static final String ENQUIRY_HEADER = "ENQUIRY.SELECT,,////////////////////New-1-99-1";
    public static final String AC_LOCKED_EVENTS_TEMPLATE = "AC.LOCKED.EVENTS,BNX.DEBT.REGISTER/%s/PROCESS//0,/LT0010001,%s";
    public static final String PAYMENT_ORDER_HEADER = "PP.ORDER.ENTRY,BNX.DEBT.REGISTER/I/PROCESS//1,/LT0010001,/";

    public static String generateAcLockedEventReq(ReceivedDocs block, String correlationId) {
        StringBuilder request = new StringBuilder();
        request.append(String.format(AC_LOCKED_EVENTS_TEMPLATE, "I", "/" + correlationId + DateUtils.getCurrentTimestamp())).append(",");
        request.append("ACCOUNT.NUMBER:1:1=").append(block.getPayerAccount()).append(",");
        request.append("L.ORIG.AMT:1:1=").append(NumberUtils.doubleToString(block.getAmount())).append(",");
        request.append("LOCKED.TYPE:1:1=DEBT.REGISTER,");
        String remittanceInfId = block.getRemittanceInformationId();

        String executeType = block.getExecuteType();
        String lockDescription;
        if (executeType.length() + remittanceInfId.length() < 34) {
            lockDescription = executeType + " " + remittanceInfId;
        } else {
            lockDescription = StringUtils.substring(remittanceInfId,0, 35);
        }
        request.append("DESCRIPTION:1:1=\"").append(StringUtils.replaceTextChars(lockDescription)).append("\",");

        String reserveAltKey = StringUtils.substring(remittanceInfId, 0, 69);
        request.append("RESERVE.ALT.KEY:1:1=\"").append(reserveAltKey).append("\",");

        List<String> reasonList = StringUtils.splitLine(block.getRemittanceInformationReason(), 65);
        IntStream.range(0, reasonList.size()).forEach(i ->
                request.append("L.MERCHANT:").append(i + 1).append(":1=\"").append(StringUtils.replaceTextChars(StringUtils.toLatin(reasonList.get(i)))).append("\",")
        );
        return request.toString();
    }


    public static String generateUpdateAcLockedEventReq(String acLockedEventId, double amount) {
        String request = String.format(AC_LOCKED_EVENTS_TEMPLATE, "I", acLockedEventId);
        request += "/,L.ORIG.AMT:1:1=" + NumberUtils.doubleToString(amount) + ",";
        return request;
    }

    public static String generateReverseAcLockedEventReq(String acLockedEventId) {
        return String.format(AC_LOCKED_EVENTS_TEMPLATE, "R", acLockedEventId);
    }

    public static String generateValidateIbanEnquiry(String iban) {
        return ENQUIRY_HEADER + ",BNX.GET.CUST.DET.BY.ALT.ACCOUNT,ALT.ACCT.ID:EQ=" + iban;
    }

    public static String generatePaymentStatusEnquiry(String ftNumber) {
        return ENQUIRY_HEADER + ",BNX.GET.PAYM.STATUS.BY.FT.NUMBER,@ID:EQ=" + ftNumber;
    }

    public static String generatePaymentOrderOfs(RequestedPayments requestedPayments, String correlationId) {
        String request = PAYMENT_ORDER_HEADER + correlationId + DateUtils.getCurrentTimestamp() + ",";
        request += "IncomingMessageType=SEPA,";
        request += "DebitAccountNumber=" + requestedPayments.getDbtrAccount() + ",";
        request += "ReceiverInstitutionBIC=" + requestedPayments.getCdtrBic() + ",";
        request += "BeneficiaryAccount=" + requestedPayments.getCdtrAccount() + ",";
        request += "BeneficiaryName=" + StringUtils.toLatin(requestedPayments.getCdtrName()) + ",";
        request += "TransactionCurrency=" + requestedPayments.getCurrency() + ",";
        request += "TransactionAmount=" + requestedPayments.getAmount() + ",";
        request += "SendersReferenceNumber=" + requestedPayments.getEndToEndId() + ",";
        request += "PaymentDetails:1:1=" + StringUtils.replaceTextChars(StringUtils.toLatin(requestedPayments.getPmtDetailsUstrd()));

        return request;
    }

    public static String generateGetBalanceEnquiry(String iban) {
        return ENQUIRY_HEADER + ",BNX.GET.CUST.DET.BY.ALT.ACCOUNT,ALT.ACCT.ID:EQ=" + iban;
    }

}
