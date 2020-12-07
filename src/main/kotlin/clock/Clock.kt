package clock

import java.time.Instant

interface Clock {
   fun now(): Instant
}

class NormalClock: Clock {
    override fun now() = Instant.now()!!
}

class SettableClock(var now: Instant): Clock {
    override fun now() = now
}