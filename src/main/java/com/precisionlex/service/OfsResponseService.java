package com.precisionlex.service;

import com.precisionlex.model.*;
import com.precisionlex.enums.*;
import com.precisionlex.interfaces.*;

public class OfsResponseService {

    private final OfsRequestStore requestStore;
    private final ReceivedDocStore receivedDocStore;
    private final BlockInfoStore blockInfoStore;
    private final RequestedPaymentStore requestedPaymentStore;
    private final FreeDisposableAmountStore freeDisposableAmountStore;
    private final AvailableBalanceStore availableBalanceStore;
    private final PayerMismatchStore payerMismatchStore;
    private final MessageSender messageSender;
    private final CardstaMessageSender cardstaMessageSender;
    private final NameValidator nameValidator;
    private final EmailNotifier emailNotifier;

    public OfsResponseService(OfsRequestStore requestStore,
                              ReceivedDocStore receivedDocStore,
                              BlockInfoStore blockInfoStore,
                              RequestedPaymentStore requestedPaymentStore,
                              FreeDisposableAmountStore freeDisposableAmountStore,
                              AvailableBalanceStore availableBalanceStore,
                              PayerMismatchStore payerMismatchStore,
                              MessageSender messageSender,
                              CardstaMessageSender cardstaMessageSender,
                              NameValidator nameValidator,
                              EmailNotifier emailNotifier) {
        this.requestStore = requestStore;
        this.receivedDocStore = receivedDocStore;
        this.blockInfoStore = blockInfoStore;
        this.requestedPaymentStore = requestedPaymentStore;
        this.freeDisposableAmountStore = freeDisposableAmountStore;
        this.availableBalanceStore = availableBalanceStore;
        this.payerMismatchStore = payerMismatchStore;
        this.messageSender = messageSender;
        this.cardstaMessageSender = cardstaMessageSender;
        this.nameValidator = nameValidator;
        this.emailNotifier = emailNotifier;
    }

    public void processOfsResponse(String ofsResponseText, String correlationId) {
        OfsRequest request = requestStore.findByCorrelationId(correlationId);
        if (request == null) {
            messageSender.sendError("Missing Ofs request entry", ofsResponseText);
            return;
        }

        request.setOfsResponseText(ofsResponseText);
        requestStore.save(request);

        if (ofsResponseText.contains("TAFJERR-1060: Session Expiration") || "-1".equals(ofsResponseText)) {
            // resend failed request
            messageSender.sendMessage(request.getOfsRequestText(), correlationId);
            return;
        }

        RequestType type = RequestType.fromLabel(request.getRequestType());

        switch (type) {
            case NEW_BLOCK -> handleNewBlock(request, ofsResponseText);
            case CARD_CHN, CARD_CAN -> {
                ReceivedDoc doc = receivedDocStore.findByMessageAndDocId(
                        request.getReceivedMessageId(), request.getDocumentId());
                updateFreeDisposableAmount(doc);
                updateDocAndSendCardstaMessage(doc, ReturnResult.ACCEPTED);
            }
            case PAYM_INIT -> {
                RequestedPayment payment = requestedPaymentStore.findByInstrId(request.getDocumentId());
                payment.setProcessStatus(PaymentProcessStatus.IN_PROGRESS.name());
                payment.setFtNumber("FT-" + correlationId); // stubbed
                requestedPaymentStore.save(payment);
                emailNotifier.notifyPaymentInit(payment);
            }
            case PAYM_STATUS -> {
                RequestedPayment payment = requestedPaymentStore.findByInstrId(request.getDocumentId());
                payment.setProcessStatus(PaymentProcessStatus.SUCCESS.name()); // stubbed
                requestedPaymentStore.save(payment);
                cardstaMessageSender.send(payment);
            }
            case GET_BALANCE -> {
                AvailableAccountBalance balance = availableBalanceStore.findByAccountId(request.getDocumentId());
                if (balance != null) {
                    messageSender.sendMessage("Balance retrieved: " + balance.getAvailableAmt(), correlationId);
                }
            }
            default -> messageSender.sendError("Unsupported request type", ofsResponseText);
        }
    }

    private void handleNewBlock(OfsRequest request, String ofsResponseText) {
        ReceivedDoc doc = receivedDocStore.findByMessageAndDocId(
                request.getReceivedMessageId(), request.getDocumentId());
        CurrentBlockInfo blockInfo = blockInfoStore.findById(request.getCurrentBlockInfoId());

        if (blockInfo != null) {
            blockInfo.setAcLockedId(ofsResponseText.split("/")[0]);
            blockInfo.setStatus(BlockStatus.ACTIVE.getLabel());
            blockInfoStore.save(blockInfo);
        }

        updateFreeDisposableAmount(doc);
        updateDocAndSendCardstaMessage(doc, ReturnResult.ACCEPTED);
    }

    private void updateFreeDisposableAmount(ReceivedDoc doc) {
        if (doc != null && doc.getFreeDisposableAmount() != null) {
            FreeDisposableAmount record = freeDisposableAmountStore.findByAccount(doc.getPayerAccount());
            if (record != null) {
                record.setAmount(doc.getFreeDisposableAmount());
            } else {
                record = new FreeDisposableAmount();
                record.setPayerAccount(doc.getPayerAccount());
                record.setAmount(doc.getFreeDisposableAmount());
                record.setReceivedDocId(doc.getId());
            }
            freeDisposableAmountStore.save(record);
        }
    }

    private void updateDocAndSendCardstaMessage(ReceivedDoc doc, ReturnResult result) {
        if (doc == null) return;

        doc.setReturnResult(result.getLabel());
        receivedDocStore.save(doc);

        if (result == ReturnResult.DECLINED && doc.getCurrentBlockInfoId() != null) {
            CurrentBlockInfo blockInfo = blockInfoStore.findById(doc.getCurrentBlockInfoId());
            if (blockInfo != null) {
                blockInfo.setStatus(BlockStatus.CANCELED.getLabel());
                blockInfoStore.save(blockInfo);
            }
        }

        cardstaMessageSender.send(doc);
    }
}
