package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class LegacyFloatTest {

    @Test
    void basicValues() {
        assertEquals("0.0", LegacyFloat.toString(0.0f));
        assertEquals("-0.0", LegacyFloat.toString(-0.0f));
        assertEquals("1.0", LegacyFloat.toString(1.0f));
        assertEquals("-1.0", LegacyFloat.toString(-1.0f));
        assertEquals("0.1", LegacyFloat.toString(0.1f));
        assertEquals("100.0", LegacyFloat.toString(100.0f));
        assertEquals("123.456", LegacyFloat.toString(123.456f));
    }

    @Test
    void specialValues() {
        assertEquals("NaN", LegacyFloat.toString(Float.NaN));
        assertEquals("Infinity", LegacyFloat.toString(Float.POSITIVE_INFINITY));
        assertEquals("-Infinity", LegacyFloat.toString(Float.NEGATIVE_INFINITY));
    }

    @Test
    void extremeValues() {
        assertEquals("1.4E-45", LegacyFloat.toString(Float.MIN_VALUE));
        assertEquals("3.4028235E38", LegacyFloat.toString(Float.MAX_VALUE));
        assertEquals("1.17549435E-38", LegacyFloat.toString(Float.MIN_NORMAL));
    }

    @Test
    void matchesCurrentJdkFloatToString() {
        // This library is meant to reproduce the JDK 17 Float.toString(float) algorithm
        // exactly, so on a JDK 17 runtime it must be byte-for-byte identical to
        // java.lang.Float.toString(float).
        float[] samples = {
            0.0f, -0.0f, 1.0f, -1.0f, 0.1f, 100.0f, 1234567.0f,
            0.001f, 1e7f, 1e-3f, 1e-4f,
            Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL,
            123.456f, 0.33333334f, 9999999.0f, 10000000.0f,
            (float) Math.PI, (float) Math.E, (float) -Math.PI
        };
        for (float f : samples) {
            assertEquals(Float.toString(f), LegacyFloat.toString(f),
                "mismatch for bits=" + Float.floatToIntBits(f));
        }
    }

    /**
     * JDK-8202555: prior to JDK 19, Float.toString(float) (like Double.toString(double))
     * did not always produce the shortest decimal string that round-trips to the same
     * float. See https://inside.java/2022/09/23/quality-heads-up/.
     *
     * Example: (float) 1e11 and 1.0e11f parse to the exact same float, but the legacy
     * (pre-JDK-19) algorithm printed (float) 1e11 back out as "9.9999998E10"
     * (8 significant digits) instead of the shortest form "1.0E11".
     *
     * LegacyFloat intentionally reproduces the JDK 17 (pre-fix) behaviour, so this test
     * pins the *old*, longer output -- it must NOT match what JDK 19+'s Float.toString
     * would produce for the same input.
     */
    @Test
    void reproducesPreJdk19ShortestDecimalBug() {
        float f = (float) 1e11;

        // Sanity check: (float) 1e11 and 1.0e11f really are the same float.
        assertEquals(Float.floatToIntBits((float) 1e11), Float.floatToIntBits(1.0e11f));

        // Legacy (JDK 17) behaviour: NOT the shortest representation.
        assertEquals("9.9999998E10", LegacyFloat.toString(f));

        // The shortest/correct representation, as produced by JDK 19+, must differ.
        String legacyOutput = LegacyFloat.toString(f);
        assertNotEquals("1.0E11", legacyOutput,
            "LegacyFloat should reproduce the pre-JDK-19 (non-shortest) output, "
                + "not the JDK 19+ fixed/shortest output");
    }
}
