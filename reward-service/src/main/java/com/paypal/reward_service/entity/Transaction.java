package com.paypal.reward_service.entity;

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
    private Long senderId;


    @Column(nullable=false)
    private Long receiverId;

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

    public Transaction(Long id, Long senderId, Long receiverId, LocalDateTime timeStamp, Double amount, String status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
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
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", timeStamp=" + timeStamp +
                ", status='" + status + '\'' +
                '}';
    }
}

