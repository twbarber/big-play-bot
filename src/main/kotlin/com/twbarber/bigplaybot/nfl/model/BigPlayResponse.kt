package com.twbarber.bigplaybot.nfl.model

import com.fasterxml.jackson.annotation.JsonProperty

data class BigPlayResponse(
    @JsonProperty("bigPlays")
    val bigPlays: List<BigPlay>
)