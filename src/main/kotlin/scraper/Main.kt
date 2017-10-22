package scraper

import com.natpryce.konfig.*
import scraper.settings.refreshInterval
import scraper.settings.slackChannel
import scraper.settings.slackWebhookUrl

fun main(args: Array<String>) {
    val config = ConfigurationProperties.fromResource("application.properties")
    val scraper = Scraper(config[slackWebhookUrl], config[slackChannel])
    while (true) {
        scraper.refresh()
        Thread.sleep(config[refreshInterval])
    }
}

object settings : PropertyGroup() {
    val slackWebhookUrl by stringType
    val slackChannel by stringType
    val refreshInterval by longType
}