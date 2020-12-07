import clock.NormalClock
import event.RpmEventStatistic

fun main() {
    val eventManager = RpmEventStatistic(NormalClock())
    eventManager.incEvent("event1")
    eventManager.incEvent("event2")
    eventManager.incEvent("event2")
    eventManager.incEvent("event1")
    eventManager.incEvent("event2")
    eventManager.incEvent("event1")
    eventManager.incEvent("event1")
    eventManager.printStatistic()
}