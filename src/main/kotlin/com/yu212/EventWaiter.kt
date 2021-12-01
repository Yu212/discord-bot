package com.yu212

import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.concurrent.Executors

object EventWaiter: EventListener {
    private val threadPool = Executors.newSingleThreadScheduledExecutor()
    private val waitingEvents: MutableMap<Class<*>, MutableSet<(GenericEvent) -> Boolean>> = mutableMapOf()

    fun <E: Event> waitForEvent(classType: Class<E>, action: (E) -> Boolean) {
        waitingEvents.computeIfAbsent(classType) { mutableSetOf() }.add(action as (GenericEvent) -> Boolean)
    }

    fun <E: GenericMessageEvent> waitForMessageEvent(messageId: Long, classType: Class<E>, action: (E) -> Boolean) {
        waitForEvent(classType) { event -> messageId == event.messageIdLong && action(event) }
    }

    fun waitForButtonClickEvent(messageId: Long, action: (ButtonClickEvent) -> Boolean) {
        waitForEvent(ButtonClickEvent::class.java) { event -> messageId == event.messageIdLong && action(event) }
    }

    fun waitForSelectionMenuEvent(messageId: Long, action: (SelectionMenuEvent) -> Boolean) {
        waitForEvent(SelectionMenuEvent::class.java) { event -> messageId == event.messageIdLong && action(event) }
    }

    override fun onEvent(event: GenericEvent) {
        var clazz: Class<*> = event.javaClass
        while (true) {
            waitingEvents[clazz]?.run {
                removeIf { action -> action(event) }
            }
            if (waitingEvents[clazz]?.isEmpty() == true) {
                waitingEvents.remove(clazz)
            }
            if (event is ShutdownEvent) {
                threadPool.shutdown()
            }
            clazz = clazz.superclass ?: return
        }
    }
}
