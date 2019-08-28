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

    @Value("\${app.streamable.upload_url}")
    lateinit var streamableUploadUrl: String

    @Value("\${app.streamable.share_url}")
    lateinit var streamableShareUrl: String

    @Value("\${app.streamable.user}")
    lateinit var streamableUser: String

    @Value("\${app.streamable.password}")
    lateinit var streamablePassword: String


}