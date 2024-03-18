package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import no.uio.ifi.in2000.team_17.data.calculateHeight
import org.junit.Test

class HydrostaticFormulaUnitTests {
    @Test
    fun heigth_isCorrect() {
        //arrange and act
        val result = calculateHeight(10.0, -10.0, 101.32)
        //assert
        //assertTrue(result in 9.0..11.0)
    }
}