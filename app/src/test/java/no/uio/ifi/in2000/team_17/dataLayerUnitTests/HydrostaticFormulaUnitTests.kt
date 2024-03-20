package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import junit.framework.TestCase.assertEquals
import no.uio.ifi.in2000.team_17.data.calculateHeight
import org.junit.Test

class HydrostaticFormulaUnitTests {
    @Test
    fun height_isCorrect_1000() {
        //arrange and act
        val pressure = 1000.0
        val temperature = -5.0
        val pressureAtSeaLevel = 1013.25

        val result = calculateHeight(pressure, temperature, pressureAtSeaLevel)


        val expectedResult = 101.0

        //assert
        assertEquals(expectedResult, result, 10.0)
    }

    @Test
    fun height_isCorrect_750() {
        //arrange and act
        val pressure = 750.0
        val temperature = -25.0
        val pressureAtSeaLevel = 1013.25

        val result = calculateHeight(pressure, temperature, pressureAtSeaLevel)


        val expectedResult = 2185.238

        //assert
        assertEquals(expectedResult, result, 2.0)
    }

    @Test
    fun height_isCorrect_500() {
        //arrange and act
        val pressure = 500.0
        val temperature = -26.0
        val pressureAtSeaLevel = 1013.25

        val result = calculateHeight(pressure, temperature, pressureAtSeaLevel)


        val expectedResult = 5109.73

        //assert
        assertEquals(expectedResult, result, 2.0)
    }

    // Test that the function returns 0 when the pressure is equal to the pressure at sea level
    @Test
    fun calculateHeightReturnsZeroForEqualPressures() {
        val pressure = 1000.0
        val temperature = 15.0
        val pressureAtSeaLevel = 1000.0

        val result = calculateHeight(pressure, temperature, pressureAtSeaLevel)

        assertEquals(0.0, result, 0.01)
    }

}