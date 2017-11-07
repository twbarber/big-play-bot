package com.twbarber.scraper

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.array
import com.beust.klaxon.int
import com.beust.klaxon.obj
import com.beust.klaxon.string
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import mu.KotlinLogging


object BigPlayRepository {

    private val LOG = KotlinLogging.logger {}

    private val parser = Parser()
    private var lastRun: Map<Int, Highlight> = HashMap()

    fun run() : MutableSet<Highlight> {
        LOG.info { "Retrieving JSON" }
        val response = Fuel.get("https://feeds.nfl.com/feeds-rs/bigPlayVideos.json").response().second
        val highlights = parseJson(response)
        return highlights.also { LOG.info { "Iteration Complete." } }
    }

    private fun parseJson(response: Response) : MutableSet<Highlight> {
        val json: JsonObject = parser.parse(StringBuilder(String(response.data))) as JsonObject
        val bigPlays = json.array<JsonObject>("bigPlays")
        val highlights: MutableSet<Highlight> =
        bigPlays?.flatMap {
            mutableSetOf(
                    Highlight(
                            it.int("id") ?: 0,
                            it.obj("video")?.string("briefHeadline") ?: "",
                            it.obj("video")?.array<JsonObject>("videoBitRates")!![4]
                                    .string("videoPath") ?: ""
                    ))
        }?.toMutableSet() ?: mutableSetOf()
        LOG.info { "Previous: ${lastRun.size}, Latest: ${highlights.size}, Diff: ${highlights.size - lastRun.size}" }
        return highlights.filter { !lastRun.contains(it.id) }.toMutableSet()
                .also { lastRun = highlights.associate { it.id to it } }
    }

}

data class Highlight(val id: Int, val title: String, val videoUrl: String)

