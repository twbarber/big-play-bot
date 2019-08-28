package com.twbarber.bigplaybot.nfl.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.twbarber.bigplaybot.Config
import com.twbarber.bigplaybot.nfl.model.BigPlay
import com.twbarber.bigplaybot.nfl.model.BigPlayResponse
import com.twbarber.bigplaybot.slack.SlackService
import com.twbarber.bigplaybot.streamable.StreamableService
import mu.KotlinLogging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException


@Service
open class NFLApiClient {

    companion object {
        val LOG = KotlinLogging.logger {}
    }

    @Autowired
    lateinit var config: Config

    @Autowired
    lateinit var streamableService: StreamableService

    @Autowired
    lateinit var slack: SlackService


    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private var lastRun: Map<Int, BigPlay> = HashMap()

    @Scheduled(fixedRate = 5000L)
    fun refresh() {
        LOG.info { "Retrieving JSON" }
        val request = Request.Builder()
                .url(config.nflUrl)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                parseJson(response)
                response.body()?.close()
            }
        })
        LOG.info { "Iteration Complete." }
    }

    fun parseJson(response: Response) {
        val bigPlayResponse: BigPlayResponse = mapper.readValue(response.body()?.string()!!)
        val highlights = bigPlayResponse.bigPlays
        LOG.info { "Previous: ${lastRun.size}, Latest: ${highlights.size}, Diff: ${highlights.size - lastRun.size}" }
        if (lastRun.isNotEmpty()) {
            highlights.filter { !lastRun.containsKey(it.id) }.forEach {
                val shortcode = streamableService.post(it.video.videoPlayBackUrl)
                val videoUrl = streamableService.retrieve(shortcode)
                logAndSendHighlight(it, videoUrl)
            }
        } else {
            highlights.shuffled().take(1).forEach {
                val shortcode = streamableService.post(it.video.videoPlayBackUrl)
                val videoUrl = streamableService.retrieve(shortcode)
                logAndSendHighlight(it, videoUrl)
            }
        }
        lastRun = highlights.associateBy { it.id }
    }

    private fun logAndSendHighlight(it: BigPlay, streamableUrl: String) {
        LOG.info { "Posting to Slack: ${it.id} - ${it.video.briefHeadline}" }
        slack.send("<https://$streamableUrl|${it.video.briefHeadline}>")
    }


}

