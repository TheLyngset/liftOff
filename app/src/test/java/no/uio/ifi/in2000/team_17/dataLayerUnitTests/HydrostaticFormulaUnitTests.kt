package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import no.uio.ifi.in2000.team_17.data.calculateHeight
import org.junit.Test

class HydrostaticFormulaUnitTests {
    @Test
    fun heigth_isCorrect(){
        //arrange and act
        val result = calculateHeight(10.0, -10.0, 101.32)
        //assert
        //assertEquals(10.0 , result)
        //assertTrue(result in 9.0..11.0)
    }
}