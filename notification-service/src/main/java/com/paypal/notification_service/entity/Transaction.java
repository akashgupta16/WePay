package com.paypal.notification_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long senderUserId;


    @Column(nullable=false)
    private Long receiverUserId;

    @Column(nullable = false)
    @Positive(message="Amount must be positive")
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private String status;

    //default constructor
    public Transaction()
    {

    }

    public Transaction(Long id, Long senderUserId, Long receiverUserId, Double amount, LocalDateTime timeStamp, String status) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //Lifecycle callback to set default values before persist
    @PrePersist
    public void prePersist()
    {
        if( timeStamp==null)
        {
            timeStamp= LocalDateTime.now();
        }

        if(status==null)
        {
            status="PENDING";
        }
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderUserId=" + senderUserId +
                ", receiverUserId=" + receiverUserId +
                ", amount=" + amount +
                ", timeStamp=" + timeStamp +
                ", status='" + status + '\'' +
                '}';
    }
}

