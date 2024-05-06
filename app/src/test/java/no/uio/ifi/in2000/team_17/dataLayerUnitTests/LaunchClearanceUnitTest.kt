package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.model.Available
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase.Companion.canLaunch
import org.junit.Test

class LaunchClearanceUnitTest {
    val dummyWeatherPoint: WeatherPointInTime = WeatherPointInTime(
        date = "00",
        groundWind = WindLayer(8.5, 10.0, 111.0),
        maxWindShear = WindShear(24.5, 200.0),
        time = "00:00",
        maxWind = WindLayer(17.2, 200.0, direction = 123.4),
        temperature = -2.0,
        pressure = 1000.0,
        height = 10.0,
        cloudFraction = 14.5,
        rain = Rain(0.0, 0.0, 0.0, probability = 0.0),
        fog = 0.0,
        available = Available(true, true, true, true, true, true, true, true, true, true)
    )
    val dummyThreshods = Thresholds.getDefaultInstance()

    @Test
    fun LaunchClearance_FalseRain() {
        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.rain.probability = 100.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun LaunchClearance_FalseClouds() {
        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.cloudFraction = 100.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun LaunchClearance_FalseHumidity() {
        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.humidity = 100.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)

    }

    @Test
    fun LaunchClearance_FalseDewPoint() {
        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.dewPoint = 20.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun LaunchClearance_FalseWindShear() {

        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.maxWindShear.speed = 100.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun LaunchClearance_FalseWindSpeed() {
        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.groundWind.speed = 100.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun LaunchClearance_FalseFog() {

        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        dummy.fog = 50.0
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun LaunchClearance_True() {
        //Arrange - mock groundWeatherPoint object
        var dummy = dummyWeatherPoint
        //Act
        val result = canLaunch(dummy, dummyThreshods)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

}