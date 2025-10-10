package com.ead.course.consumers;

import com.ead.course.dtos.UserEventDto;
import com.ead.course.enums.ActionType;
import com.ead.course.models.UserModel;
import com.ead.course.service.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    final UserService userService;

    public UserConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${ead.broker.queue.userEventQueue.name}", durable = "true"),
                    exchange = @Exchange(value = "${ead.broker.exchange.userEventExchange}", type =
                            ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
            )
    )
    public void listenUserEvent(@Payload UserEventDto userEventDto) {
        var userModel = userEventDto.convertToUserModel();
        switch (ActionType.valueOf(userEventDto.actionType())) {
            case CREATE, UPDATE -> userService.save(userModel);
            case DELETE -> userService.delete(userModel.getUserId());
            default -> throw new IllegalArgumentException("Invalid action type: " + userEventDto.actionType());
        }
    }
}
