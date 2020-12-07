import clock.SettableClock
import event.RpmEventStatistic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import kotlin.math.abs

class StatisticTest {
    private val EPSILON = 0.0000000001

    private fun equals(a: Double, b: Double): Boolean {
        return abs(a - b) <= EPSILON
    }

    @Test
    fun testNoEvents() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = RpmEventStatistic(clock)
        val eventName = "event"
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        assertEquals(0, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun testOneEvent() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = RpmEventStatistic(clock)
        val eventName = "event"
        statsManager.incEvent(eventName)
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1 / 60.0))
        assertEquals(1, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun testOneEventManyIncrements() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = RpmEventStatistic(clock)
        val eventName = "event"
        val count = 100
        for (i in 1..count) {
            statsManager.incEvent(eventName)
            assertTrue(equals(statsManager.getEventStatisticByName(eventName), i / 60.0))
            assertEquals(1, statsManager.getAllEventStatistic().size)
        }
    }

    @Test
    fun testManyEvents() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = RpmEventStatistic(clock)
        val count = 100
        for (i in 1..count) {
            val name = "event$i"
            statsManager.incEvent(name)
            assertTrue(equals(statsManager.getEventStatisticByName(name), 1 / 60.0))
        }
        assertEquals(count, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun testOldEvent() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = RpmEventStatistic(clock)
        val eventName = "event"
        statsManager.incEvent(eventName)
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1 / 60.0))
        clock.now = Instant.ofEpochSecond(10000000L)
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
    }

    @Test
    fun testOldEventAndNewEvent() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = RpmEventStatistic(clock)
        val eventName = "event"
        statsManager.incEvent(eventName)
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1 / 60.0))
        clock.now = Instant.ofEpochSecond(10000000L)
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        val eventName2 = "event2"
        statsManager.incEvent(eventName2)
        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        assertTrue(equals(statsManager.getEventStatisticByName(eventName2), 1 / 60.0))
        assertEquals(2, statsManager.getAllEventStatistic().size)
    }
}