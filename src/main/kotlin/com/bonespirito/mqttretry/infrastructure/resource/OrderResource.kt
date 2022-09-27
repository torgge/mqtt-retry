package com.bonespirito.mqttretry.infrastructure.resource

import com.bonespirito.mqttretry.application.ConsumerService
import com.bonespirito.mqttretry.domain.model.Order
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.producer.OrderMessageProducer
import com.bonespirito.mqttretry.utils.toPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderResource(
    @Autowired val messageService: OrderMessageProducer,
    @Autowired val consumerService: ConsumerService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun postMessage(
        @RequestBody message: Order
    ): HttpEntity<Any?> {
        messageService.produce(message.toPayload(), 0)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(message)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/start"])
    @ResponseStatus(HttpStatus.CREATED)
    fun startConsume(): HttpEntity<Any?> {
        consumerService.startConsume()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/stop"])
    @ResponseStatus(HttpStatus.CREATED)
    fun stopConsume(): HttpEntity<Any?> {
        consumerService.stopConsume()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build()
    }
}
