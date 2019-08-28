package com.twbarber.bigplaybot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class Config {

    @Value("\${app.slackWebhookUrl}")
    lateinit var slackWebhookUrl: String

    @Value("\${app.slackChannel}")
    lateinit var slackChannel: String

    @Value("\${app.nflUrl}")
    lateinit var nflUrl: String

    @Value("\${app.refreshInterval}")
    lateinit var refreshInterval: String

}