package com.twbarber.bigplaybot.slack

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.twbarber.bigplaybot.Config
import com.twbarber.bigplaybot.slack.model.Message
import mu.KotlinLogging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SlackService {

    companion object {
        val LOG = KotlinLogging.logger {}
    }

    @Autowired
    lateinit var config: Config

    val mapper = jacksonObjectMapper()

    private val client = OkHttpClient()

    fun send(text: String) {
        val message =
            Message(config.slackChannel, text, ":football:")

        val body = FormBody.Builder()
            .add("payload", mapper.writeValueAsString(message))
            .build()
        val request = Request.Builder()
            .url(config.slackWebhookUrl)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = LOG.info { "NOT ok" }
            override fun onResponse(call: Call, response: Response) {
                LOG.info { "Slack Post Succeeded." }
                response.body()?.close()
            }
        })
    }

}
