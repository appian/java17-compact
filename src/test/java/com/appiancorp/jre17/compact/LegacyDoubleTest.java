package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class LegacyDoubleTest {

    @Test
    void basicValues() {
        assertEquals("0.0", LegacyDouble.toString(0.0d));
        assertEquals("-0.0", LegacyDouble.toString(-0.0d));
        assertEquals("1.0", LegacyDouble.toString(1.0d));
        assertEquals("-1.0", LegacyDouble.toString(-1.0d));
        assertEquals("0.1", LegacyDouble.toString(0.1d));
        assertEquals("100.0", LegacyDouble.toString(100.0d));
        assertEquals("123.456", LegacyDouble.toString(123.456d));
    }

    @Test
    void specialValues() {
        assertEquals("NaN", LegacyDouble.toString(Double.NaN));
        assertEquals("Infinity", LegacyDouble.toString(Double.POSITIVE_INFINITY));
        assertEquals("-Infinity", LegacyDouble.toString(Double.NEGATIVE_INFINITY));
    }

    @Test
    void extremeValues() {
        assertEquals("4.9E-324", LegacyDouble.toString(Double.MIN_VALUE));
        assertEquals("1.7976931348623157E308", LegacyDouble.toString(Double.MAX_VALUE));
        assertEquals("2.2250738585072014E-308", LegacyDouble.toString(Double.MIN_NORMAL));
    }

    @Test
    void matchesCurrentJdkDoubleToString() {
        // This library is meant to reproduce the JDK 17 Double.toString(double) algorithm
        // exactly, so on a JDK 17 runtime it must be byte-for-byte identical to
        // java.lang.Double.toString(double).
        double[] samples = {
            0.0, -0.0, 1.0, -1.0, 0.1, 100.0, 1234567.0, 12345678.0,
            0.001, 0.0009999, 1e7, 1e-3, 1e-4, 1.0E300, 1.0E-300,
            Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL,
            123.456, 0.3333333333333333, 9999999.0, 10000000.0,
            Math.PI, Math.E, -Math.PI
        };
        for (double d : samples) {
            assertEquals(Double.toString(d), LegacyDouble.toString(d),
                "mismatch for bits=" + Double.doubleToLongBits(d));
        }
    }

    /**
     * JDK-8202555: prior to JDK 19, Double.toString(double) did not always produce the
     * shortest decimal string that round-trips to the same double, as required by its
     * own specification. The fix landed in JDK 19 (see
     * https://inside.java/2022/09/23/quality-heads-up/).
     *
     * Example: 1e23 and 9.999999999999999E22 parse to the exact same double, but the
     * legacy (pre-JDK-19) algorithm printed 1e23 back out as "9.999999999999999E22"
     * (17 significant digits) instead of the shortest form "1.0E23".
     *
     * LegacyDouble intentionally reproduces the JDK 17 (pre-fix) behaviour, so this test
     * pins the *old*, longer output -- it must NOT match what JDK 19+'s Double.toString
     * would produce for the same input.
     */
    @Test
    void reproducesPreJdk19ShortestDecimalBug() {
        double d = 1e23;

        // Sanity check: 1e23 and 9.999999999999999E22 really are the same double.
        assertEquals(Double.doubleToLongBits(1e23), Double.doubleToLongBits(9.999999999999999E22));

        // Legacy (JDK 17) behaviour: NOT the shortest representation.
        assertEquals("9.999999999999999E22", LegacyDouble.toString(d));

        // The shortest/correct representation, as produced by JDK 19+, must differ.
        String legacyOutput = LegacyDouble.toString(d);
        assertNotEquals("1.0E23", legacyOutput,
            "LegacyDouble should reproduce the pre-JDK-19 (non-shortest) output, "
                + "not the JDK 19+ fixed/shortest output");
    }
}
