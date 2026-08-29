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

class Java17CompactDateFormatProviderTest {

    private final DateFormatProvider provider = new Java17CompactDateFormatProvider();

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
}
