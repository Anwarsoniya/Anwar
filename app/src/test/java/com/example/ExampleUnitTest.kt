package com.example

import com.example.ui.util.BanglaUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun banglaDigits_conversion_isCorrect() {
        assertEquals("১২৩৪৫", BanglaUtils.toBanglaDigits("12345"))
        assertEquals("০", BanglaUtils.toBanglaDigits("0"))
    }

    @Test
    fun banglaTaka_formatting_isCorrect() {
        assertEquals("৳৫০০", BanglaUtils.formatTaka(500.0))
        assertEquals("৳০", BanglaUtils.formatTaka(0.0))
    }

    @Test
    fun banglaNumber_formatting_isCorrect() {
        assertEquals("১০", BanglaUtils.formatNumber(10.0))
        assertEquals("১০.৫", BanglaUtils.formatNumber(10.5))
    }
}
