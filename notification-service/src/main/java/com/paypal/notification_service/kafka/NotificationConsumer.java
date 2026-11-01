package com.paypal.notification_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paypal.notification_service.entity.Notification;
import com.paypal.notification_service.entity.Transaction;
import com.paypal.notification_service.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationConsumer  {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(NotificationRepository notificationRepository, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;

        //SetUp ObjectMapper with JavaTimeModule to handle LocalDateTime
        this.objectMapper = objectMapper;
        this.objectMapper.registerModules(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @KafkaListener(topics = "txn-initiated", groupId = "notification-group")
    public void listener(String message) throws JsonProcessingException{
        Transaction txn = objectMapper.readValue(message, Transaction.class);

        System.out.println("Received transaction: "+ txn);

        Notification notification = new Notification();

        Long receiverUserId = txn.getReceiverUserId();
        Long senderUserId = txn.getSenderUserId();
        String notify = "$" + txn.getAmount() + " received from: "+ txn.getSenderUserId();
        System.out.println(notify);
        notification.setMessage(notify);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

}
