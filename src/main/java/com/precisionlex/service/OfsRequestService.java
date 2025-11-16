package com.precisionlex.service;

import com.precisionlex.model.*;
import com.precisionlex.enums.RequestType;
import com.precisionlex.utils.Ofs;
import com.precisionlex.interfaces.*;

import java.util.List;

public class OfsRequestService {

    private final OfsRequestStore requestStore;
    private final BlockInfoStore blockInfoStore;
    private final MessageSender messageSender;

    public OfsRequestService(OfsRequestStore requestStore,
                             BlockInfoStore blockInfoStore,
                             MessageSender messageSender) {
        this.requestStore = requestStore;
        this.blockInfoStore = blockInfoStore;
        this.messageSender = messageSender;
    }

    public OfsRequest createNewBlock(ReceivedDoc block) {
        OfsRequest request = requestStore.createEmpty();
        String correlationId = generateCorrelationId(RequestType.NEW_BLOCK.getLabel(), request.getId());
        String ofsReqMessage = Ofs.generateAcLockedEventReq(block, correlationId);

        prepareAndSend(correlationId, RequestType.NEW_BLOCK.getLabel(), block.getDocId(),
                block.getReceivedMessageId(), block.getCurrentBlockInfoId(), ofsReqMessage, request);

        return request;
    }

    public OfsRequest updateAcLockedEventReq(ReceivedDoc block, CurrentBlockInfo info) {
        OfsRequest request = requestStore.createEmpty();
        String correlationId = generateCorrelationId(RequestType.CARD_CHN.getLabel(), request.getId());
        String ofsReqMessage = Ofs.generateUpdateAcLockedEventReq(info.getAcLockedId(), block.getAmount());

        prepareAndSend(correlationId, RequestType.CARD_CHN.getLabel(), block.getDocId(),
                block.getReceivedMessageId(), block.getCurrentBlockInfoId(), ofsReqMessage, request);

        return request;
    }

    public OfsRequest reverseAcLockedEventReq(ReceivedDoc block, CurrentBlockInfo info) {
        if (info != null && info.getAcLockedId() != null && !info.getAcLockedId().isEmpty()) {
            OfsRequest request = requestStore.createEmpty();
            String correlationId = generateCorrelationId(RequestType.CARD_CAN.getLabel(), request.getId());
            String ofsReqMessage = Ofs.generateReverseAcLockedEventReq(info.getAcLockedId());

            prepareAndSend(correlationId, RequestType.CARD_CAN.getLabel(), block.getDocId(),
                    block.getReceivedMessageId(), block.getCurrentBlockInfoId(), ofsReqMessage, request);

            return request;
        } else {
            messageSender.sendError("The current block is missing to reverse Lock to ID=" + block.getDocId(), "");
            return null;
        }
    }

    public OfsRequest sendIbanValidationRequest(ReceivedDoc block, RequestType requestType) {
        OfsRequest request = requestStore.createEmpty();
        String correlationId = generateCorrelationId(requestType.getLabel(), request.getId());
        String ofsReqMessage = Ofs.generateValidateIbanEnquiry(block.getPayerAccount());

        prepareAndSend(correlationId, requestType.getLabel(), block.getDocId(),
                block.getReceivedMessageId(), block.getCurrentBlockInfoId(), ofsReqMessage, request);

        return request;
    }

    public OfsRequest initiateBlockinfGetBalanceRequest(String accountId) {
        List<CurrentBlockInfo> activeBlocks = blockInfoStore.findActiveByAccountId(accountId);

        if (!activeBlocks.isEmpty()) {
            OfsRequest request = requestStore.createEmpty();
            String correlationId = generateCorrelationId(RequestType.GET_BALANCE.getLabel(), request.getId());
            String ofsReqMessage = Ofs.generateGetBalanceEnquiry(accountId);

            CurrentBlockInfo lastBlock = activeBlocks.get(activeBlocks.size() - 1);

            request.setCorrelationId(correlationId);
            request.setRequestType(RequestType.GET_BALANCE.getLabel());
            request.setOfsRequestText(ofsReqMessage);
            request.setCurrentBlockInfoId(lastBlock.getId());

            requestStore.save(request);
            messageSender.sendMessage(ofsReqMessage, correlationId);

            return request;
        }
        return null;
    }

    public OfsRequest sendPaymentOrder(RequestedPayment payment) {
        OfsRequest request = requestStore.createEmpty();
        String correlationId = generateCorrelationId(RequestType.PAYM_INIT.getLabel(), request.getId());
        String ofsReqMessage = Ofs.generatePaymentOrderOfs(payment, correlationId);

        prepareAndSend(correlationId, RequestType.PAYM_INIT.getLabel(), payment.getInstrId(),
                payment.getReceivedMessageId(), payment.getCurrentBlockInfoId(), ofsReqMessage, request);

        return request;
    }

    public OfsRequest sendPaymentStatusRequest(RequestedPayment payment, RequestType requestType) {
        OfsRequest request = requestStore.createEmpty();
        String correlationId = generateCorrelationId(requestType.getLabel(), request.getId());
        String ofsReqMessage = Ofs.generatePaymentStatusEnquiry(payment.getFtNumber());

        prepareAndSend(correlationId, requestType.getLabel(), payment.getInstrId(),
                payment.getReceivedMessageId(), payment.getCurrentBlockInfoId(), ofsReqMessage, request);

        return request;
    }

    public void resendFailedOfsRequest(OfsRequest request) {
        String correlationId = request.getCorrelationId();
        String ofsReqMessage = request.getOfsRequestText();
        messageSender.sendMessage(ofsReqMessage, correlationId);
    }

    private void prepareAndSend(String correlationId, String requestType, String documentId,
                                String receivedMessageId, String currentBlockInfoId,
                                String ofsReqMessage, OfsRequest request) {
        request.setCorrelationId(correlationId);
        request.setRequestType(requestType);
        request.setDocumentId(documentId);
        request.setReceivedMessageId(receivedMessageId);
        request.setCurrentBlockInfoId(currentBlockInfoId);
        request.setOfsRequestText(ofsReqMessage);

        requestStore.save(request);
        messageSender.sendMessage(ofsReqMessage, correlationId);
    }

    private String generateCorrelationId(String prefix, String id) {
        return prefix + String.format("%010d", id);
    }
}
