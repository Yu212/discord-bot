package com.yu212.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl

class SayCommand: Command() {
    override val data: CommandData by lazy {
        CommandDataImpl("say", "description")
                .addOption(OptionType.STRING, "message", "message to say", true)
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val message = event.getOption("message")!!.asString
        event.deferReply(true).queue()
        event.channel.sendMessage(message).queue {
            event.hook.sendMessage("正常完了").queue()
        }
    }
}
