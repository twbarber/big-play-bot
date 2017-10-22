package scraper

import com.beust.klaxon.*
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime


class Scraper(private val slackWebhookUrl: String, private val slackChannel: String) {

    private val client = OkHttpClient()
    private val parser = Parser()
    private var lastRun: Map<Int, Highlight> = HashMap()

    fun refresh() {
        println("Retrieving JSON")
        val request = Request.Builder()
                .url("https://feeds.nfl.com/feeds-rs/bigPlayVideos.json")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                parseJson(response)
                response.body()?.close()
            }
        })
        println("Iteration Complete.")
    }

    fun parseJson(response: Response) {
        val jsonString = StringBuilder(response.body()?.string())
        val json: JsonObject = parser.parse(jsonString) as JsonObject
        val bigPlays = json.array<JsonObject>("bigPlays")
        val highlights: MutableSet<Highlight> = HashSet()
        bigPlays?.forEach {
            highlights.add(
                    Highlight(
                            it.int("id") ?: 0,
                            it.obj("video")?.string("briefHeadline") ?: "",
                            it.obj("video")?.array<JsonObject>("videoBitRates")!![4].string("videoPath") ?: ""
                    ))
        }
        println("Time: ${LocalDateTime.now()}, Previous: ${lastRun.size}, Latest: ${highlights.size}, Diff: ${highlights.size - lastRun.size}")
        if (!lastRun.isEmpty()) {
            highlights.filter { !lastRun.containsKey(it.id) }.forEach {
                val message = "{\"channel\": \"#${slackChannel}\", \"text\": " +
                        "\"<${it.videoUrl}|${it.title}>\", " +
                        "\"icon_emoji\": \":football:\"" +
                        "}"
                println("Posting to Slack: ${it.id} - ${it.title}")
                slack(message)
            }
        }
        lastRun = highlights.associate { it.id to it }
    }

    private fun slack(message: String) {
        val body = FormBody.Builder()
                .add("payload", message)
                .build()
        val request = Request.Builder()
                .url(slackWebhookUrl)
                .post(body)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = println("NOT ok")
            override fun onResponse(call: Call, response: Response) {
                println("Slack Post Succeeded.")
                response.body()?.close()
            }
        })

    }
}

data class Highlight(val id: Int, val title: String, val videoUrl: String)