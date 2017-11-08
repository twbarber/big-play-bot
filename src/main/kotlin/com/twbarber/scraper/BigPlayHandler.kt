package com.twbarber.scraper

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.twbarber.amazon.SnsService
import kotlinx.coroutines.experimental.runBlocking
import mu.KotlinLogging

object BigPlayHandler {

    private val LOG = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        LOG.info { "Process Running..." }
        val awsRegion = Region.getRegion(Regions.US_EAST_1)
        val topicArn = System.getenv("TOPIC_ARN") ?: ""
        LOG.info { "Configuration Loaded..." }
        val newHighlights = BigPlayRepository.run()
        newHighlights.forEach {
            runBlocking {
                val message = "<${it.videoUrl}|${it.title}>"
                println(message)
                SnsService.send(awsRegion, topicArn, message)
            }
        }
    }

    private class ConfigurationException(msg: String) : Throwable(msg)

}

