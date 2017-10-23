package scraper

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.longType
import com.natpryce.konfig.stringType
import scraper.settings.refreshInterval
import scraper.settings.slackChannel
import scraper.settings.slackWebhookUrl


object settings : PropertyGroup() {
    val slackWebhookUrl by stringType
    val slackChannel by stringType
    val refreshInterval by longType
}

fun main(args: Array<String>) {
    val config = ConfigurationProperties.fromResource("application.properties")
    val scraper = Scraper(config[slackWebhookUrl], config[slackChannel])
    while (true) {
        scraper.refresh()
        Thread.sleep(config[refreshInterval])
    }
}
