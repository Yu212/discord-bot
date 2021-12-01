package com.yu212

import java.util.*

object Id {
    private const val BOT_ID = "Yu212"

    fun of(id: String) = "$BOT_ID:$id"
    fun random() = "$BOT_ID:${UUID.randomUUID()}"
}
