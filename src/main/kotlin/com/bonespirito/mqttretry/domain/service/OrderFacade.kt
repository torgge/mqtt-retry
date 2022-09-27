package com.bonespirito.mqttretry.domain.service

import com.bonespirito.mqttretry.domain.model.Order

interface OrderFacade {
    fun process(order: Order, retry: Int = 0)
}
