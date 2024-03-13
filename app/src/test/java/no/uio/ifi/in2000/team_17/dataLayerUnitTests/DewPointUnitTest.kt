package no.uio.ifi.in2000.team_17.dataLayerUnitTests

import junit.framework.TestCase.assertTrue
import org.junit.Test
import no.uio.ifi.in2000.team_17.data.locationforecast.computeDewPointGround

class DewPointUnitTest {
    @Test
    fun dewPoint_isCorrectHUMover50(){
        //arrange and act
        val result = computeDewPointGround(10.0, 100.0)
        //assert
        //assertEquals(10.0 , result)
        assertTrue(result in 9.0..11.0);
    }

    @Test
    fun dewPoint_isCorrectNegTemp(){
        //arrange and act
        val result = computeDewPointGround(-10.0, 60.0)
        //assert
        //assertEquals(-18.0 , result)
        assertTrue(result in -19.0..-17.0);

    }
    @Test
    fun dewPoint_isCorrectPosTemp(){
        //arrange and act
        val result = computeDewPointGround(15.0, 60.0)
        //assert
       // assertEquals(7.29 , result) -- +- 1 degree
        assertTrue(result in 6.29..8.29);
    }



}
