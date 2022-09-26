package com.extend.domain.dto;

import com.extend.domain.lookup.VirtualCardStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualCard implements Serializable {
    private String id;
    private String recipientId;
    private String cardholderId;
    private VirtualCardStatus status;
    private String displayName;
    private String creditCardDisplayName;
    private BigDecimal balanceCents;
    private BigDecimal availableCredit;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS")
    private LocalDateTime validTo;

    public BigDecimal getAvailableCredit() {
        if (Objects.nonNull(this.balanceCents)) {
            return balanceCents
                    .divide(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.FLOOR);
        }
        return BigDecimal.ZERO;
    }
}
