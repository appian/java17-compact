package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactDateFormatProviderTest {

    private final DateFormatProvider provider = new Java17CompactDateFormatProvider();
    private final DateFormatProvider jreProvider = LocaleProviderAdapter.forJRE().getDateFormatProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void usShortDateInstanceFormatsMonthSlashDaySlashYear() {
        DateFormat format = provider.getDateInstance(DateFormat.SHORT, Locale.US);
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.clear();
        cal.set(2024, Calendar.MARCH, 15);
        String formatted = format.format(cal.getTime());
        assertEquals("3/15/24", formatted);
    }

    @Test
    void usTimeInstanceContainsColonSeparator() {
        DateFormat format = provider.getTimeInstance(DateFormat.SHORT, Locale.US);
        Date now = new Date();
        assertTrue(format.format(now).contains(":"));
    }

    @Test
    void dateTimeInstanceCombinesDateAndTimeStyles() {
        DateFormat dateOnly = provider.getDateInstance(DateFormat.LONG, Locale.US);
        DateFormat combined = provider.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.US);
        Date now = new Date();
        // The combined format's output must be strictly longer than the date-only
        // format's output, since it additionally includes a time component.
        assertTrue(combined.format(now).length() > dateOnly.format(now).length());
    }

    @Test
    void frenchLongDateInstanceUsesFrenchMonthName() {
        DateFormat format = provider.getDateInstance(DateFormat.LONG, Locale.FRENCH);
        Calendar cal = Calendar.getInstance(Locale.FRENCH);
        cal.clear();
        cal.set(2024, Calendar.MARCH, 15);
        String formatted = format.format(cal.getTime());
        assertTrue(formatted.toLowerCase(Locale.FRENCH).contains("mars"));
    }

    @Test
    void invalidStyleThrowsIndexOutOfBoundsException() {
        // DateFormatProviderImpl looks up the style directly as an array index into
        // its resource bundle's date/time pattern arrays, so an out-of-range style
        // surfaces as an ArrayIndexOutOfBoundsException rather than a validated
        // IllegalArgumentException.
        assertThrows(IndexOutOfBoundsException.class, () -> provider.getDateInstance(999, Locale.US));
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getDateInstance(DateFormat.SHORT, null));
        assertThrows(NullPointerException.class, () -> provider.getTimeInstance(DateFormat.SHORT, null));
        assertThrows(NullPointerException.class,
            () -> provider.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, null));
    }

    @Test
    void resultIsNotNullForAllStandardStyles() {
        int[] styles = { DateFormat.FULL, DateFormat.LONG, DateFormat.MEDIUM, DateFormat.SHORT };
        for (int style : styles) {
            assertNotNull(provider.getDateInstance(style, Locale.US));
            assertNotNull(provider.getTimeInstance(style, Locale.US));
        }
    }

    @Test
    void everyAdvertisedLocaleAndStyleMatchesJre() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        Date fixed = new Date(1710491696000L);
        int[] styles = { DateFormat.FULL, DateFormat.LONG, DateFormat.MEDIUM, DateFormat.SHORT };
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            for (int style : styles) {
                DateFormat actualDate = provider.getDateInstance(style, locale);
                DateFormat expectedDate = jreProvider.getDateInstance(style, locale);
                DateFormat actualTime = provider.getTimeInstance(style, locale);
                DateFormat expectedTime = jreProvider.getTimeInstance(style, locale);
                DateFormat actualDateTime = provider.getDateTimeInstance(style, style, locale);
                DateFormat expectedDateTime = jreProvider.getDateTimeInstance(style, style, locale);
                actualDate.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                expectedDate.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                actualTime.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                expectedTime.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                actualDateTime.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                expectedDateTime.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                assertEquals(expectedDate.format(fixed), actualDate.format(fixed),
                    "date mismatch for " + locale + ", style=" + style);
                assertEquals(expectedTime.format(fixed), actualTime.format(fixed),
                    "time mismatch for " + locale + ", style=" + style);
                assertEquals(expectedDateTime.format(fixed), actualDateTime.format(fixed),
                    "date-time mismatch for " + locale + ", style=" + style);
                assertEquals(expectedDate.isLenient(), actualDate.isLenient(), "date leniency for " + locale);
                assertEquals(expectedTime.isLenient(), actualTime.isLenient(), "time leniency for " + locale);
            }
        }
    }
}
