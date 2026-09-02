/*
 * Copyright (c) 1999, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Concrete implementation of the  {@link java.text.spi.DateFormatSymbolsProvider
 * DateFormatSymbolsProvider} class for the JRE LocaleProviderAdapter.
 *
 * @author Naoto Sato
 * @author Masayoshi Okutsu
 */
public class DateFormatSymbolsProviderImpl extends DateFormatSymbolsProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    // java.text.DateFormatSymbols loads its era/month/weekday/am-pm/zone data in its
    // constructor from whatever LocaleProviderAdapter the *host* runtime selects. When
    // this ported stack runs as an SPI drop-in on a newer JDK, that data comes from the
    // host's CLDR (or, with java.locale.providers=SPI only, the root/English fallback)
    // rather than the ported JDK 17 JRE/COMPAT FormatData -- e.g. German "MMM" for March
    // yields the CLDR "März" (or English "Mar") instead of the JDK 17 "Mär", and zone
    // abbreviations such as LINT/IRDT are lost. We therefore build the DateFormatSymbols
    // explicitly from the ported JRE resources (see createFromJreResources, which mirrors
    // java.text.DateFormatSymbols#initializeData). Cache the built instance per locale and
    // hand out clones, since getInstance must return a fresh, caller-mutable instance.
    private final ConcurrentMap<Locale, DateFormatSymbols> cache = new ConcurrentHashMap<>();

    public DateFormatSymbolsProviderImpl(LocaleProviderAdapter.Type type, Set<String> langtags) {
        this.type = type;
        this.langtags = langtags;
    }

    /**
     * Returns an array of all locales for which this locale service provider
     * can provide localized objects or names.
     *
     * @return An array of all locales for which this locale service provider
     * can provide localized objects or names.
     */
    @Override
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(langtags);
    }

    @Override
    public boolean isSupportedLocale(Locale locale) {
        return LocaleProviderAdapter.forType(type).isSupportedProviderLocale(locale, langtags);
    }

    /**
     * Returns a new <code>DateFormatSymbols</code> instance for the
     * specified locale.
     *
     * @param locale the desired locale
     * @exception NullPointerException if <code>locale</code> is null
     * @exception IllegalArgumentException if <code>locale</code> isn't
     *     one of the locales returned from
     *     {@link java.util.spi.LocaleServiceProvider#getAvailableLocales()
     *     getAvailableLocales()}.
     * @return a <code>DateFormatSymbols</code> instance.
     * @see java.text.DateFormatSymbols#getInstance(java.util.Locale)
     */
    @Override
    public DateFormatSymbols getInstance(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        return (DateFormatSymbols) cache.computeIfAbsent(locale, this::createFromJreResources).clone();
    }

    /**
     * Builds a {@code DateFormatSymbols} entirely from the ported JRE resources,
     * mirroring the field mapping of {@code java.text.DateFormatSymbols#initializeData}
     * so the result matches JDK 17 JRE/COMPAT rather than the host runtime's locale data.
     */
    private DateFormatSymbols createFromJreResources(Locale locale) {
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        LocaleProviderAdapter adapter = LocaleProviderAdapter.forType(type);
        ResourceBundle rb = ((ResourceBundleBasedAdapter) adapter).getLocaleData().getDateFormatData(locale);

        if (rb.containsKey("Eras")) {
            dfs.setEras(rb.getStringArray("Eras"));
        } else if (rb.containsKey("long.Eras")) {
            dfs.setEras(rb.getStringArray("long.Eras"));
        } else if (rb.containsKey("short.Eras")) {
            dfs.setEras(rb.getStringArray("short.Eras"));
        }

        dfs.setMonths(rb.getStringArray("MonthNames"));
        dfs.setShortMonths(rb.getStringArray("MonthAbbreviations"));

        String[] ampms = rb.getStringArray("AmPmMarkers");
        if (ampms.length > 2) {
            ampms = Arrays.copyOf(ampms, 2);
        }
        dfs.setAmPmStrings(ampms);

        if (rb.containsKey("DateTimePatternChars")) {
            dfs.setLocalPatternChars(rb.getString("DateTimePatternChars"));
        }

        dfs.setWeekdays(toOneBasedArray(rb.getStringArray("DayNames")));
        dfs.setShortWeekdays(toOneBasedArray(rb.getStringArray("DayAbbreviations")));

        dfs.setZoneStrings(adapter.getLocaleResources(locale).getZoneStrings());
        return dfs;
    }

    /**
     * java.text.DateFormatSymbols stores weekday arrays one-based (index 0 unused,
     * indices Calendar.SUNDAY..SATURDAY populated); FormatData supplies 0-based length-7
     * arrays.
     */
    private static String[] toOneBasedArray(String[] src) {
        String[] dst = new String[src.length + 1];
        dst[0] = "";
        System.arraycopy(src, 0, dst, 1, src.length);
        return dst;
    }

    @Override
    public Set<String> getAvailableLanguageTags() {
        return langtags;
    }
}
