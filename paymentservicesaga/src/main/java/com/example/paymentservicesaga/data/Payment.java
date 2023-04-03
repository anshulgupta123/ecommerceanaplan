package com.example.paymentservicesaga.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private String paymentId;
    private String orderId;
    private Double payableAmount;
    private String paymentMode;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private String paymentStatus;
}
