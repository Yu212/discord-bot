package com.yu212

import com.yu212.commands.CommandDispatcher
import com.yu212.commands.CalcCommand
import com.yu212.commands.SayCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild

lateinit var jda: JDA
lateinit var debugGuild: Guild

fun main(args: Array<String>) {
    val token = System.getenv("DISCORD_BOT_TOKEN") ?: args[0]
    jda = JDABuilder.createDefault(token)
            .addEventListeners(EventWaiter)
            .build()
            .awaitReady()
    debugGuild = jda.getGuildById(680339070777425941)!!
//    debugGuild = jda.getGuildById(444134366411489281)!!
    CommandDispatcher().run {
        register(CalcCommand())
        register(SayCommand())
    }
}
