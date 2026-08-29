package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.spi.TimeZoneNameProvider;

import org.junit.jupiter.api.Test;

class Java17CompactTimeZoneNameProviderTest {

    private final TimeZoneNameProvider provider = new Java17CompactTimeZoneNameProvider();

    // TimeZoneNames' base bundle covers plain "en" only (not "en-US") -- see
    // BaseLocaleDataMetaInfo's "TimeZoneNames" entry -- while the ext bundles add
    // de, en-CA, en-GB, en-IE, es, fr, hi, it, ja, ko, etc. (see
    // NonBaseLocaleDataMetaInfo). Locale.UK (en-GB) is used here since it is
    // actually covered.
    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUk() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.UK));
    }

    @Test
    void newYorkLongStandardNameInUk() {
        String name = provider.getDisplayName("America/New_York", false, TimeZone.LONG, Locale.UK);
        assertNotNull(name);
        assertTrue(name.toLowerCase(Locale.UK).contains("eastern"));
    }

    @Test
    void newYorkLongDaylightNameInUk() {
        String name = provider.getDisplayName("America/New_York", true, TimeZone.LONG, Locale.UK);
        assertNotNull(name);
        assertTrue(name.toLowerCase(Locale.UK).contains("daylight"));
    }

    @Test
    void newYorkShortStandardNameInUk() {
        String name = provider.getDisplayName("America/New_York", false, TimeZone.SHORT, Locale.UK);
        assertNotNull(name);
    }

    @Test
    void unknownTimeZoneIdReturnsNull() {
        String name = provider.getDisplayName("Not/A_Real_Zone", false, TimeZone.LONG, Locale.UK);
        assertEquals(null, name);
    }

    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getDisplayName(null, false, TimeZone.LONG, Locale.UK));
        assertThrows(NullPointerException.class,
            () -> provider.getDisplayName("America/New_York", false, TimeZone.LONG, null));
    }
}
