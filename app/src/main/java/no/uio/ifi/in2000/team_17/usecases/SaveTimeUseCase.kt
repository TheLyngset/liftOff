package no.uio.ifi.in2000.team_17.usecases

import no.uio.ifi.in2000.team_17.model.WeatherDataLists
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SaveTimeUseCase {
    companion object {
        @JvmStatic
        fun timeIndexToString(timeIndex: Int): String {
            return LocalDateTime.now().plusHours(timeIndex.toLong()).toString()
        }

        @JvmStatic
        fun timeStringToIndex(timeString: String, weatherDataList: WeatherDataLists): Int {
            val now = LocalDateTime.now()
            val time = LocalDateTime.parse(timeString)
            return if (time.isBefore(now)) {
                0
            } else {
                weatherDataList.dateTime.indexOfFirst{
                    it == timeString
                }
            }
        }
    }
}