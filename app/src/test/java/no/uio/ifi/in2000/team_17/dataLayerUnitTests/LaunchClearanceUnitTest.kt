package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.model.Available
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase.Companion.canLaunch
import org.junit.Test

class LaunchClearanceUnitTest {

    private val dummyWeatherPoint: WeatherPointInTime = WeatherPointInTime(
        date = "00",
        groundWind = WindLayer(4.5, 10.0, 111.0),
        maxWindShear = WindShear(10.3, 200.0),
        time = "00:00",
        maxWind = WindLayer(5.2, 200.0, direction = 123.4),
        temperature = -2.0,
        pressure = 1000.0,
        height = 10.0,
        cloudFraction = 0.5,
        rain = Rain(0.0, 0.0, 0.0, probability = 0.0),
        humidity = 30.0,
        dewPoint = 2.0,
        fog = 0.0,
        available = Available(
            date = true,
            time = true,
            groundWind = true,
            maxWindShear = true,
            maxWind = true,
            cloudFraction = true,
            rain = true,
            humidity = true,
            dewPoint = true,
            fog = true,
            temperature = true
        )
    )

    private val dummyThresholds = ThresholdsSerializer.defaultValue

    @Test
    fun launchClearance_TrueGreen() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy()
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.GREEN)
    }

    @Test
    fun launchClearance_BelowThresholdYellow() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(cloudFraction = 14.0)
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.YELLOW)
    }

    @Test
    fun launchClearance_AboveThresholdYellow() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(cloudFraction = 16.0)
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.YELLOW)
    }

    @Test
    fun launchClearance_FalseRain() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(rain = Rain(0.0, 0.0, 0.0, 100.0))
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun launchClearance_FalseClouds() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(cloudFraction = 100.0)
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun launchClearance_FalseHumidity() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(humidity = 100.0)
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun launchClearance_FalseDewPoint() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(dewPoint = 100.0)
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun launchClearance_FalseWindShear() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(maxWindShear = WindShear(50.0, 100.0))
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun launchClearance_FalseWindSpeed() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(groundWind = WindLayer(100.0, 10.0, 123.4))
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }

    @Test
    fun launchClearance_FalseFog() {
        //Arrange - mock groundWeatherPoint object
        val dummy = dummyWeatherPoint.copy(fog = 100.0)
        //Act
        val result = canLaunch(dummy, dummyThresholds)
        //Assert
        assert(result == TrafficLightColor.RED)
    }
}