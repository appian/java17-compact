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

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactCalendarNameProviderTest {

    private final CalendarNameProvider provider = new Java17CompactCalendarNameProvider();
    private final CalendarNameProvider jreProvider = LocaleProviderAdapter.forJRE().getCalendarNameProvider();

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

    @Test
    void everyAdvertisedLocaleMatchesJreForNamesAndMaps() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        int[] fields = { Calendar.ERA, Calendar.MONTH, Calendar.DAY_OF_WEEK, Calendar.AM_PM };
        int[][] values = {
            { Calendar.ERA },
            { Calendar.JANUARY, Calendar.JUNE, Calendar.DECEMBER },
            { Calendar.SUNDAY, Calendar.WEDNESDAY, Calendar.SATURDAY },
            { Calendar.AM, Calendar.PM }
        };
        int[] styles = { Calendar.LONG, Calendar.SHORT };
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
                int field = fields[fieldIndex];
                for (int value : values[fieldIndex]) {
                    for (int style : styles) {
                        String expected = jreProvider.getDisplayName("gregory", field, value, style, locale);
                        String actual = provider.getDisplayName("gregory", field, value, style, locale);
                        assertEquals(expected, actual,
                            "display name mismatch for " + locale + ", field=" + field
                                + ", value=" + value + ", style=" + style);
                    }
                }
                for (int style : styles) {
                    assertEquals(jreProvider.getDisplayNames("gregory", field, style, locale),
                        provider.getDisplayNames("gregory", field, style, locale),
                        "display-name map mismatch for " + locale + ", field=" + field
                            + ", style=" + style);
                }
            }
        }
    }
}
