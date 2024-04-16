package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import no.uio.ifi.in2000.team_17.data.launchClearance
import no.uio.ifi.in2000.team_17.model.WeatherPointLayer
import org.junit.Test

class LaunchClearanceUnitTest {
    @Test
    fun LaunchClearance_FalseRain() {
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 1.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 7.0
        val maxWindSpeed = 17.2
        val maxWindShear = 24.5
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)
    }

    @Test
    fun LaunchClearance_FalseClouds() {
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 16.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 7.0
        val maxWindSpeed = 17.2
        val maxWindShear = 24.5
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)

    }

    @Test
    fun LaunchClearance_FalseHumidity() {
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 76.0
        val height = 0.0
        val dewPoint = 7.0
        val maxWindSpeed = 17.2
        val maxWindShear = 24.5
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)

    }

    @Test
    fun LaunchClearance_FalseDewPoint() {

        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 16.0
        val maxWindSpeed = 17.2
        val maxWindShear = 24.5
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)
    }

    @Test
    fun LaunchClearance_FalseWindShear() {

        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 14.0
        val maxWindSpeed = 17.2
        val maxWindShear = 25.0
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)
    }

    @Test
    fun LaunchClearance_FalseWindSpeed() {

        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 14.0
        val maxWindSpeed = 17.3
        val maxWindShear = 24.5
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)
    }

    @Test
    fun LaunchClearance_FalseFog() {

        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.5
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val cloudFraction = 15.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val height = 0.0
        val dewPoint = 14.0
        val maxWindSpeed = 1.3
        val maxWindShear = 24.5
        val fog = 0.5

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertFalse(result)
    }

    @Test
    fun LaunchClearance_True() {
        //Arrange - mock groundWeatherPoint object
        val windSpeed = 8.5
        val windFromDirection = 0.0
        val windShear = 24.0
        val airTemperature = -2.0
        val pressureSeaLevel = 1001.2
        val height = 0.0
        val cloudFraction = 14.0
        val rain = 0.0
        val relativeHumidity = 74.0
        val dewPoint = 7.0
        val maxWindSpeed = 16.0
        val maxWindShear = 24.0
        val fog = 0.0

        val mockGroundWeatherPoint = WeatherPointLayer(
            windSpeed = windSpeed,
            windDirection = windFromDirection,
            windShear = windShear,
            temperature = airTemperature,
            pressure = pressureSeaLevel,
            height = height,
            cloudFraction = cloudFraction,
            rain = rain,
            humidity = relativeHumidity,
            dewPoint = dewPoint,
            fog = fog
        )

        //Act
        val result = launchClearance(mockGroundWeatherPoint, maxWindSpeed, maxWindShear)
        //Assert
        assertTrue(result)
    }

}