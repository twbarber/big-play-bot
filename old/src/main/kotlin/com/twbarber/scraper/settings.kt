package com.twbarber.scraper

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.longType
import com.natpryce.konfig.stringType

object settings : PropertyGroup() {
    val slackWebhookUrl by stringType
    val slackChannel by stringType
    val refreshInterval by longType
}