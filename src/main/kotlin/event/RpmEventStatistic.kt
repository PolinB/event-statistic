package event

import clock.Clock

class RpmEventStatistic(private val clock: Clock): EventStatistic {
    private val SECONDS_IN_MINUTE = 60
    private val MINUTS_IN_HOUR = 60

    private val events = mutableMapOf<String, MutableList<Long>>()

    override fun incEvent(name: String) {
        val now = clock.now()
        if (!events.containsKey(name)) {
            events[name] = mutableListOf()
        }
        events[name]!!.add(now.epochSecond)
    }

    override fun getEventStatisticByName(name: String): Double {
        val now = clock.now()
        return if (!events.containsKey(name) || events[name]!!.isEmpty()) {
            0.0
        } else {
            deleteOldEvents(now.epochSecond, events[name]!!).toDouble() / MINUTS_IN_HOUR
        }
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        val now = clock.now()
        return events.mapValues{ (_, value) ->
            deleteOldEvents(now.epochSecond, value).toDouble() / MINUTS_IN_HOUR
        }
    }

    override fun printStatistic() {
        println("===========STATISTIC===========")
        getAllEventStatistic().forEach{ (name, rpm) ->
            println("$name: $rpm")
        }
    }

    private fun deleteOldEvents(currentTime: Long, list: MutableList<Long>) =
        list.filter { it > currentTime - MINUTS_IN_HOUR * SECONDS_IN_MINUTE }.count()
}