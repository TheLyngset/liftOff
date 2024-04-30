package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import junit.framework.TestCase.assertEquals
import no.uio.ifi.in2000.team_17.data.calculateWindShear
import org.junit.Test
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class WindShearUnitTests {

    // Test that the function returns 0 when the speeds and directions are equal
    @Test
    fun calculateWindShearReturnsZeroForEqualSpeedsAndDirections() {
        val s_0 = 10.0
        val d_0 = 45.0
        val s_1 = 10.0
        val d_1 = 45.0

        val result = calculateWindShear(s_0, d_0, s_1, d_1)

        assertEquals(0.0, result, 0.001)
    }

    // Test that the function returns the correct value for different speeds and directions
    @Test
    fun calculateWindShearReturnsCorrectValueForDifferentSpeedsAndDirections() {
        val s_0 = 10.0
        val d_0 = 45.0
        val s_1 = 20.0
        val d_1 = 90.0

        val result = calculateWindShear(s_0, d_0, s_1, d_1)

        val expectedResult = sqrt(
            (s_1 * cos(d_1 * PI / 180) - s_0 * cos(d_0 * PI / 180)).pow(2) + (s_1 * sin(d_1 * PI / 180) - s_0 * sin(
                d_0 * PI / 180
            )).pow(2)
        )

        assertEquals(expectedResult, result, 0.01)
    }

}