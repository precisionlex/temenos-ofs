package com.precisionlex.service;

import com.precisionlex.model.*;
import com.precisionlex.enums.*;
import com.precisionlex.utils.GeneralOfsParser;
import com.precisionlex.utils.NumberUtils;
import com.precisionlex.utils.StringUtils;

import java.util.List;
import java.util.Optional;

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

        if (ofsResponseText.contains("TAFJERR-1060: Session Expiration") || ofsResponseText.equals("-1")) {
            // resend failed request
            messageSender.sendMessage(request.getOfsRequestText(), correlationId);
            return;
        }

        // Example: handle NEW_BLOCK
        if (request.getRequestType().equals(RequestType.NEW_BLOCK.getLabel())) {
            ReceivedDoc doc = receivedDocStore.findByMessageAndDocId(request.getReceivedMessageId(), request.getDocumentId());
            CurrentBlockInfo blockInfo = blockInfoStore.findById(request.getCurrentBlockInfoId());

            blockInfo.setAcLockedId(ofsResponseText.split("/")[0]);
            blockInfo.setStatus(BlockStatus.ACTIVE.getLabel());
            blockInfoStore.save(blockInfo);

            updateFreeDisposableAmount(doc);
            updateDocAndSendCardstaMessage(doc, ReturnResult.ACCEPTED);
        }

        // … replicate for other request types (CARD_CHN, CARD_CAN, PAYM_INIT, PAYM_STATUS, GET_BALANCE, etc.)
    }

    private void updateFreeDisposableAmount(ReceivedDoc doc) {
        if (doc.getFreeDisposableAmount() != null) {
            FreeDisposableAmount record = freeDisposableAmountStore.findByAccount(doc.getPayerAccount());
            if (record != null) {
                record.setAmount(doc.getFreeDisposableAmount());
            } else {
                record = new FreeDisposableAmount(doc.getPayerAccount(), doc.getFreeDisposableAmount());
            }
            freeDisposableAmountStore.save(record);
        }
    }

    private void updateDocAndSendCardstaMessage(ReceivedDoc doc, ReturnResult result) {
        doc.setReturnResult(result.getLabel());
        receivedDocStore.save(doc);

        if (result.equals(ReturnResult.DECLINED) && doc.getCurrentBlockInfoId() != null) {
            CurrentBlockInfo blockInfo = blockInfoStore.findById(doc.getCurrentBlockInfoId());
            blockInfo.setStatus(BlockStatus.CANCELED.getLabel());
            blockInfoStore.save(blockInfo);
        }

        cardstaMessageSender.send(doc);
    }
}
