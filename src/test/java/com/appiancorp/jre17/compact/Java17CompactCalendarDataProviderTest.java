package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.spi.CalendarDataProvider;

import org.junit.jupiter.api.Test;

class Java17CompactCalendarDataProviderTest {

    private final CalendarDataProvider provider = new Java17CompactCalendarDataProvider();

    // No CalendarData_*.java resource bundle has been ported into this
    // repository at all (unlike FormatData/CollationData/TimeZoneNames, which
    // have at least a base bundle). The "nb nb-NO nn-NO" entry produced by
    // generateLocaleDataMetaInfo's implicit-locale injection in build.gradle.kts
    // is metadata-only and does not correspond to any actual bundle class, so
    // every real lookup below throws MissingResourceException today.
    @Test
    void getAvailableLocalesReflectsMetadataEvenThoughNoBundlesExist() {
        // getAvailableLocales() is driven purely by the langtag metadata
        // (BaseLocaleDataMetaInfo/NonBaseLocaleDataMetaInfo), not by checking
        // whether a bundle class actually exists, so it is non-empty despite
        // no CalendarData bundle having been ported.
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
    }

    @Test
    void norwegianFirstDayOfWeekThrowsBecauseNoBundleIsPorted() {
        assertThrows(java.util.MissingResourceException.class,
            () -> provider.getFirstDayOfWeek(Locale.forLanguageTag("nb-NO")));
    }

    @Test
    void usFirstDayOfWeekThrowsBecauseNoBundleIsPorted() {
        assertThrows(java.util.MissingResourceException.class, () -> provider.getFirstDayOfWeek(Locale.US));
    }

    @Test
    void rootLocaleAlsoThrowsBecauseNoBundleIsPorted() {
        // Even Locale.ROOT, which real CalendarData bundles fall back to,
        // throws here since no root bundle has been ported either.
        assertThrows(java.util.MissingResourceException.class, () -> provider.getFirstDayOfWeek(Locale.ROOT));
    }

    @Test
    void minimalDaysInFirstWeekAlsoThrowsBecauseNoBundleIsPorted() {
        assertThrows(java.util.MissingResourceException.class,
            () -> provider.getMinimalDaysInFirstWeek(Locale.forLanguageTag("nb-NO")));
    }

    @Test
    void nullLocaleThrowsNullPointerExceptionRatherThanMissingResourceException() {
        // The null check happens before any bundle lookup is attempted.
        assertThrows(NullPointerException.class, () -> provider.getFirstDayOfWeek(null));
        assertThrows(NullPointerException.class, () -> provider.getMinimalDaysInFirstWeek(null));
    }
}
