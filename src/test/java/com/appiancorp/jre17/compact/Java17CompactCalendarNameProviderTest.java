package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.spi.CalendarNameProvider;

import org.junit.jupiter.api.Test;

class Java17CompactCalendarNameProviderTest {

    private final CalendarNameProvider provider = new Java17CompactCalendarNameProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void gregorianLongMonthNameInUs() {
        String name = provider.getDisplayName("gregory", Calendar.MONTH, Calendar.JANUARY, Calendar.LONG, Locale.US);
        assertEquals("January", name);
    }

    @Test
    void gregorianShortMonthNameInUs() {
        String name = provider.getDisplayName("gregory", Calendar.MONTH, Calendar.JANUARY, Calendar.SHORT, Locale.US);
        assertEquals("Jan", name);
    }

    @Test
    void gregorianDayOfWeekNameInUs() {
        String name = provider.getDisplayName("gregory", Calendar.DAY_OF_WEEK, Calendar.SUNDAY, Calendar.LONG, Locale.US);
        assertEquals("Sunday", name);
    }

    @Test
    void amPmMarkersInUs() {
        String am = provider.getDisplayName("gregory", Calendar.AM_PM, Calendar.AM, Calendar.LONG, Locale.US);
        assertEquals("AM", am);
    }

    @Test
    void getDisplayNamesReturnsAllMonthsMappedToValues() {
        Map<String, Integer> names = provider.getDisplayNames("gregory", Calendar.MONTH, Calendar.LONG, Locale.US);
        assertNotNull(names);
        assertEquals(Integer.valueOf(Calendar.JANUARY), names.get("January"));
        assertEquals(Integer.valueOf(Calendar.DECEMBER), names.get("December"));
    }

    @Test
    void outOfRangeFieldValueReturnsNull() {
        // Calendar.UNDECIMBER (13) has no month name in most locales/calendars;
        // requesting a wildly out-of-range value should return null, not throw.
        String name = provider.getDisplayName("gregory", Calendar.MONTH, 999, Calendar.LONG, Locale.US);
        assertEquals(null, name);
    }

    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> provider.getDisplayName("gregory", Calendar.MONTH, Calendar.JANUARY, Calendar.LONG, null));
    }
}
