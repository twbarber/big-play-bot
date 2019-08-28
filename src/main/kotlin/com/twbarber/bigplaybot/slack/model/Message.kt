package com.twbarber.bigplaybot.slack.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    val channel: String,
    val text: String,
    @JsonProperty("icon_emoji")
    val emoji: String?
)