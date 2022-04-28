package ir.ali.weatherforecast

import ir.ali.weatherforecast.utils.DialogAppear

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @DialogAppear
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}