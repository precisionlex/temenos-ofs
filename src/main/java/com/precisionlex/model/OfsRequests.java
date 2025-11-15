package com.precisionlex.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfsRequest {
    private Long id;
    private String correlationId;
    private String requestType;
    private String ofsRequestText;
    private String ofsResponseText;
    private String documentId;
    private String receivedMessageId;
    private String currentBlockInfoId;
}
