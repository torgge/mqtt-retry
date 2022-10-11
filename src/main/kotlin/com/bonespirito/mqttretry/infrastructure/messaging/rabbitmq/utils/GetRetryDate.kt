package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.utils

import com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config.NEXT_RETRY_MILLISECONDS
import java.util.Date

fun getRetryDate() = Date(System.currentTimeMillis() + NEXT_RETRY_MILLISECONDS)

fun allowProcessMessage(date: Date) = date < Date(System.currentTimeMillis())
