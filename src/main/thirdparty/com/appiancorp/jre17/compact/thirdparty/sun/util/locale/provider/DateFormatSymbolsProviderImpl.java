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
import java.util.Locale;
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

    // Zone strings are normally resolved lazily by java.text.DateFormatSymbols.getZoneStrings()
    // through sun.util.locale.provider.TimeZoneNameUtility. When this ported stack runs as an
    // SPI drop-in on a newer JDK, that lazy path resolves against the *host* runtime's CLDR data
    // instead of the ported JDK 17 JRE/COMPAT TimeZoneNames, dropping legacy zone abbreviations
    // such as LINT and IRDT (so SimpleDateFormat could no longer parse them). We therefore
    // populate zoneStrings eagerly from the ported JRE resources below. Cache per locale since
    // getZoneStrings() rebuilds the ~600-row array on every call.
    private final ConcurrentMap<Locale, String[][]> zoneStringsCache = new ConcurrentHashMap<>();

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

        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        dfs.setZoneStrings(zoneStringsCache.computeIfAbsent(locale,
                loc -> LocaleProviderAdapter.forType(type).getLocaleResources(loc).getZoneStrings()));
        return dfs;
    }

    @Override
    public Set<String> getAvailableLanguageTags() {
        return langtags;
    }
}
