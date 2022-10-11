package com.bonespirito.mqttretry.infrastructure.resource

import com.bonespirito.mqttretry.application.ConsumerService
import com.bonespirito.mqttretry.domain.model.Order
import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.producer.OrderMessageProducer
import com.bonespirito.mqttretry.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderResource(
    @Autowired private val messageService: OrderMessageProducer,
    @Autowired private val consumerService: ConsumerService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun postMessage(
        @RequestBody message: Order
    ): HttpEntity<Any?> {
        messageService.produce(message.toPayload(), INITIAL_RETRY_VALUE)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(message)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        value = [POST_MAPPING_DLQ_EXCHANGE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun postDlqMessage(
        @RequestBody message: Order
    ): HttpEntity<Any?> {
        messageService.dlqProduce(message.toPayload(), INITIAL_RETRY_VALUE)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(message)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        value = [POST_MAPPING_START]
    )
    @ResponseStatus(HttpStatus.OK)
    fun startConsume(): HttpEntity<Any?> {
        consumerService.startConsume()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build()
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        value = [POST_MAPPING_STOP]
    )
    @ResponseStatus(HttpStatus.OK)
    fun stopConsume(): HttpEntity<Any?> {
        consumerService.stopConsume()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build()
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        value = [POST_MAPPING_ASYNC_CONSUME]
    )
    @ResponseStatus(HttpStatus.OK)
    fun asyncConsume(): HttpEntity<Any?> {
        consumerService.asyncConsume()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build()
    }
}
