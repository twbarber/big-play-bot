package scraper

import com.natpryce.konfig.ConfigurationProperties
import mu.KotlinLogging
import scraper.settings.refreshInterval
import scraper.settings.slackChannel
import scraper.settings.slackWebhookUrl

class Main {

    companion object {

        private val LOG = KotlinLogging.logger {}

        @JvmStatic
        fun main(args: Array<String>) {
            LOG.info {  "Process Running..." }
            val config = ConfigurationProperties.fromResource("application.properties")
            val scraper = Scraper(config[slackWebhookUrl], config[slackChannel])
            LOG.info {  "Configuration Loaded..." }
            while (true) {
                scraper.refresh()
                Thread.sleep(config[refreshInterval])
            }
        }
    }

}

