package com.bonespirito.mqttretry.utils

import com.bonespirito.mqttretry.domain.model.Material
import com.bonespirito.mqttretry.domain.model.Order
import com.bonespirito.mqttretry.domain.payload.MaterialPayloadRequestItem
import com.bonespirito.mqttretry.domain.payload.OrderPayloadRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

fun Order.toPayload() = OrderPayloadRequest(
    id = this.id,
    items = this.items.map { it.toPayload() },
    createdAt = this.createdAt.format(ISO_LOCAL_DATE_TIME)
)

fun OrderPayloadRequest.toVO() = Order(
    id = this.id,
    items = this.items!!.map { it.toVO(this.id) },
    createdAt = LocalDateTime.parse(this.createdAt)
)

fun Material.toPayload() = MaterialPayloadRequestItem(
    id = this.id,
    skuCode = this.skuCode,
    quantity = this.quantity,
    brand = this.brand
)

fun MaterialPayloadRequestItem.toVO(orderId: Long?) = Material(
    id = this.id,
    orderId = orderId,
    skuCode = this.skuCode,
    quantity = this.quantity,
    brand = this.brand
)
