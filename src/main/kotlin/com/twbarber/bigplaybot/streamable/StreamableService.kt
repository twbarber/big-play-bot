package com.twbarber.bigplaybot.streamable

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.twbarber.bigplaybot.Config
import com.twbarber.bigplaybot.nfl.http.NFLApiClient
import com.twbarber.bigplaybot.nfl.model.BigPlay
import com.twbarber.bigplaybot.slack.SlackService
import com.twbarber.bigplaybot.streamable.model.CreateResponse
import com.twbarber.bigplaybot.streamable.model.RetrieveVideoResponse
import mu.KotlinLogging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class StreamableService {

    companion object {
        val LOG = KotlinLogging.logger {}
    }

    @Autowired
    lateinit var config: Config

    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    fun post(url: String): String {
        val credential: String = Credentials.basic(config.streamableUser, config.streamablePassword)
        val request = Request.Builder()
            .addHeader("Authorization", credential)
            .addHeader("User-Agent", "Slack NFL Big Play Bot")
            .url(config.streamableUploadUrl + url)
            .build()
        val response = client.newCall(request).execute()
        val createResponse: CreateResponse = mapper.readValue(response.body()?.string()!!)
        return createResponse.shortcode
    }

    fun retrieve(shortCode: String): String {
        val credential: String = Credentials.basic(config.streamableUser, config.streamablePassword)
        val request = Request.Builder()
            .addHeader("Authorization", credential)
            .addHeader("User-Agent", "Slack NFL Big Play Bot")
            .url(config.streamableShareUrl + shortCode)
            .build()
        val response = client.newCall(request).execute()
        val retrieveVideoResponse: RetrieveVideoResponse = mapper.readValue(response.body()?.string()!!)
        LOG.info(retrieveVideoResponse.url)
        return retrieveVideoResponse.url
    }

}