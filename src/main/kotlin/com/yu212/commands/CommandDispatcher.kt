package com.yu212.commands

import com.yu212.debugGuild
import com.yu212.jda
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandDispatcher: ListenerAdapter() {
    private val commandMap: MutableMap<Long, Command> = mutableMapOf()

    init {
        jda.addEventListener(this)
        jda.updateCommands().queue()
        debugGuild.updateCommands().queue()
    }

    fun register(command: Command) {
        jda.upsertCommand(command.data).queue {
            commandMap[it.idLong] = command
        }
        debugGuild.upsertCommand(command.data).queue {
            commandMap[it.idLong] = command
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val commandId = event.commandIdLong
        commandMap[commandId]?.execute(event)
    }
}
