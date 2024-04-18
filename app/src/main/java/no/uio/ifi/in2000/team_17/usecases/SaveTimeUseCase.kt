package no.uio.ifi.in2000.team_17.usecases

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SaveTimeUseCase {


    companion object {
        @JvmStatic
        fun timeIndexToString(timeIndex: Int): String {
            return LocalDateTime.now().plusHours(timeIndex.toLong()).toString()
        }
        @JvmStatic
        fun timeStringToIndex(timeString: String): Int{
            val now = LocalDateTime.now()
            val time = LocalDateTime.parse(timeString)
            return if (time.isBefore(now)){0}
            else{ now.until(time, ChronoUnit.HOURS).toInt() + 1 } //Todo this aproach breaks on new year
        }
    }
}