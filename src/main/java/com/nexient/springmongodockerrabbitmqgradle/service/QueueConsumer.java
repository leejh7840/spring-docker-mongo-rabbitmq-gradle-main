package com.nexient.springmongodockerrabbitmqgradle.service;

import com.nexient.springmongodockerrabbitmqgradle.domain.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * QueueConsumer class is a consumer of the RabbitMQ Queue.
 * This class will always listen to the Queue defined in MessagingConfig file.
 *
 * @author  Vraj Patel vpatel@nexient.com
 * @version 1.0
 * @since   02-28-2022
 */
@Component
public class QueueConsumer {

    /**
     * This method is a RabbitListener method.
     * It will always listen to the RabbitMQ Queue in the background.
     *
     * @param orderStatus
     */
    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(OrderStatus orderStatus) {
        System.out.println("Message was received from queue : " + orderStatus);
    }

}
