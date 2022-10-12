package com.bonespirito.mqttretry.infrastructure.messaging.rabbitmq.config

const val CONSUMER_ID = "RABBITMQ-CONSUMER"
const val MAX_RETRIES = 3
const val X_RETRIES = "x-retries"
const val X_EXECUTE_AFTER = "x-execute_after"
const val NEXT_RETRY_MILLISECONDS = 30000L // Next execution must be after (1800000L) Milli/30 minute
