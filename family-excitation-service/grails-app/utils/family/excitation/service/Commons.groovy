package family.excitation.service

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoField

class Commons {
    static def timeToRange(long time) {
        def begin = new Date(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).with(ChronoField.NANO_OF_DAY, 0).toInstant().toEpochMilli())
        def end = new Date(begin.time + 24 * 60 * 60 * 1000 - 1000)
        return [begin, end]
    }
}
