package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertFalse
import no.uio.ifi.in2000.team_17.data.WeatherPoint
import org.junit.Test
import no.uio.ifi.in2000.team_17.data.locationforecast.launchClearance

class LaunchClearanceUnitTest {
    @Test
    fun LaunchClearance_FalseRain(){
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 1.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 7.0
        val mockGroundWeatherPoint = WeatherPoint(windSpeed,
            windFromDirection, 0.0,
            airTemperature, pressureSeaLevel, height, cloudFraction, rain, relativeHumidity,dewPoint)

        //Act
        val result = launchClearance(mockGroundWeatherPoint)
        //Assert
        assertFalse(result)
    }
    @Test
    fun LaunchClearance_FalseClouds(){
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 16.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 7.0
        val mockGroundWeatherPoint = WeatherPoint(windSpeed,
            windFromDirection, 0.0,
            airTemperature, pressureSeaLevel, height, cloudFraction, rain, relativeHumidity,dewPoint)

        //Act
        val result = launchClearance(mockGroundWeatherPoint)
        //Assert
        assertFalse(result)

    }
    @Test
    fun LaunchClearance_FalseHumidity(){
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 76.0
        val height = 0.0
        val dewPoint = 7.0
        val mockGroundWeatherPoint = WeatherPoint(windSpeed,
            windFromDirection, 0.0,
            airTemperature, pressureSeaLevel, height, cloudFraction, rain, relativeHumidity,dewPoint)

        //Act
        val result = launchClearance(mockGroundWeatherPoint)
        //Assert
        assertFalse(result)

    }
    @Test
    fun LaunchClearance_FalseDewPoint(){

        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 16.0
        val mockGroundWeatherPoint = WeatherPoint(windSpeed,
            windFromDirection, 0.0,
            airTemperature, pressureSeaLevel, height, cloudFraction, rain, relativeHumidity,dewPoint)

        //Act
        val result = launchClearance(mockGroundWeatherPoint)
        //Assert
        assertFalse(result)
    }

    @Test
    fun LaunchClearance_True(){
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 14.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 7.0
        val mockGroundWeatherPoint = WeatherPoint(windSpeed,
            windFromDirection, 0.0,
            airTemperature, pressureSeaLevel, height, cloudFraction, rain, relativeHumidity,dewPoint)

        //Act
        val result = launchClearance(mockGroundWeatherPoint)
        //Assert
       assertTrue(result)
    }

}