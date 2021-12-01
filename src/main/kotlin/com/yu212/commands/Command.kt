package com.yu212.commands

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class Command {
    abstract val data: CommandData

    abstract fun execute(event: SlashCommandEvent)
}
