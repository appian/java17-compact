package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Locale;
import java.util.spi.CalendarDataProvider;

import org.junit.jupiter.api.Test;

// The full set of JDK 17 JRE/COMPAT CalendarData resource bundles is now ported
// (CalendarData*.properties compiled to ListResourceBundle subclasses, plus the
// root/en base bundles), so firstDayOfWeek / minimalDaysInFirstWeek resolve for
// every advertised locale through the parent chain, matching
// java.locale.providers=COMPAT on JDK 17. Expected values were captured from
// JDK 17 running with -Djava.locale.providers=COMPAT.
class Java17CompactCalendarDataProviderTest {

    private final CalendarDataProvider provider = new Java17CompactCalendarDataProvider();

    @Test
    void getAvailableLocalesIsNonEmpty() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
    }

    @Test
    void norwegianFirstDayOfWeekIsMonday() {
        assertEquals(Calendar.MONDAY, provider.getFirstDayOfWeek(Locale.forLanguageTag("nb-NO")));
    }

    @Test
    void usFirstDayOfWeekIsSunday() {
        assertEquals(Calendar.SUNDAY, provider.getFirstDayOfWeek(Locale.US));
    }

    @Test
    void rootLocaleFirstDayOfWeekIsSunday() {
        // Locale.ROOT falls back to the base CalendarData bundle.
        assertEquals(Calendar.SUNDAY, provider.getFirstDayOfWeek(Locale.ROOT));
    }

    @Test
    void minimalDaysInFirstWeekResolvesThroughParentChain() {
        assertEquals(4, provider.getMinimalDaysInFirstWeek(Locale.forLanguageTag("nb-NO")));
        assertEquals(1, provider.getMinimalDaysInFirstWeek(Locale.US));
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getFirstDayOfWeek(null));
        assertThrows(NullPointerException.class, () -> provider.getMinimalDaysInFirstWeek(null));
    }

    @Test
    void everyAdvertisedLocaleResolvesCalendarData() {
        // Every metadata-advertised locale now resolves both values (via its
        // parent chain down to the base bundle); guards against a newly added
        // locale silently losing its data.
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            assertTrue(provider.getFirstDayOfWeek(locale) >= 1, "first day for " + locale);
            assertTrue(provider.getMinimalDaysInFirstWeek(locale) >= 1, "minimal days for " + locale);
        }
    }
}
