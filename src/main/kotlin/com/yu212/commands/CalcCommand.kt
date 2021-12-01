package com.yu212.commands

import com.yu212.EventWaiter
import com.yu212.Id
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Button
import java.lang.AssertionError
import java.math.BigInteger

class CalcCommand: Command() {
    override val data: CommandData by lazy {
        CommandData("calc", "description")
    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply(true).queue()
        val message = event.hook.sendMessage(MessageBuilder()
                .setContent("press any button...")
                .setActionRows(buttons(disabledOperators = true))
                .build())
                .complete()
        var lastPressed: String? = null
        EventWaiter.waitForButtonClickEvent(message.idLong) { e ->
            val pressed = e.button!!.label
            val expr = if (lastPressed == null) "" else e.message.contentRaw
            if (pressed == "=") {
                e.deferEdit()
                        .setContent("$expr=${calculate(expr)}")
                        .setActionRows()
                        .queue()
                return@waitForButtonClickEvent true
            } else {
                val disabledNumbers = (lastPressed == null || lastPressed!! in "*/+-") && pressed == "0"
                e.deferEdit()
                        .setContent(expr + pressed)
                        .setActionRows(buttons(disabledNumbers = disabledNumbers, disabledOperators = pressed in "*/+-"))
                        .queue()
                lastPressed = pressed
                return@waitForButtonClickEvent false
            }
        }
    }

    private fun buttons(disabledNumbers: Boolean = false, disabledOperators: Boolean = false): List<ActionRow> {
        return listOf(
                ActionRow.of(Button.secondary(Id.of("1"), "1").withDisabled(disabledNumbers), Button.secondary(Id.of("2"), "2").withDisabled(disabledNumbers), Button.secondary(Id.of("3"), "3").withDisabled(disabledNumbers)),
                ActionRow.of(Button.secondary(Id.of("4"), "4").withDisabled(disabledNumbers), Button.secondary(Id.of("5"), "5").withDisabled(disabledNumbers), Button.secondary(Id.of("6"), "6").withDisabled(disabledNumbers)),
                ActionRow.of(Button.secondary(Id.of("7"), "7").withDisabled(disabledNumbers), Button.secondary(Id.of("8"), "8").withDisabled(disabledNumbers), Button.secondary(Id.of("9"), "9").withDisabled(disabledNumbers)),
                ActionRow.of(Button.secondary(Id.of("0"), "0").withDisabled(disabledNumbers), Button.success(Id.of("+"), "+").withDisabled(disabledOperators), Button.success(Id.of("-"), "-").withDisabled(disabledOperators)),
                ActionRow.of(Button.primary(Id.of("="), "=").withDisabled(disabledOperators), Button.success(Id.of("*"), "*").withDisabled(disabledOperators), Button.success(Id.of("/"), "/").withDisabled(disabledOperators)))
    }

    private fun calculate(expr: String): BigInteger {
        var expr = expr
        val regex1 = Regex("""(\d+)([*/])(\d+)""")
        val regex2 = Regex("""(\d+)([+-])(\d+)""")
        while (true) {
            val found = regex1.find(expr) ?: regex2.find(expr)
            found ?: return expr.toBigInteger()
            val left = found.groups[1]!!.value.toBigInteger()
            val right = found.groups[3]!!.value.toBigInteger()
            val result = when (found.groups[2]!!.value) {
                "*" -> left * right
                "/" -> left / right
                "+" -> left + right
                "-" -> left - right
                else -> throw AssertionError()
            }
            expr = expr.replaceRange(found.range, result.toString())
        }
    }
}
