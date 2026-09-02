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

import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Concrete implementation of the  {@link java.text.spi.DecimalFormatSymbolsProvider
 * DecimalFormatSymbolsProvider} class for the JRE LocaleProviderAdapter.
 *
 * @author Naoto Sato
 * @author Masayoshi Okutsu
 */
public class DecimalFormatSymbolsProviderImpl extends DecimalFormatSymbolsProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    // java.text.DecimalFormatSymbols loads its number symbols in its constructor from
    // whatever LocaleProviderAdapter the *host* runtime selects. As an SPI drop-in on a
    // newer JDK that means the CLDR (or, with providers=SPI only, root/English) number
    // elements -- decimal/grouping separators etc. -- instead of the ported JDK 17
    // JRE/COMPAT values. We rebuild those fields from the ported JRE NumberElements (see
    // createFromJreResources). Cache per locale and hand out clones.
    private final ConcurrentMap<Locale, DecimalFormatSymbols> cache = new ConcurrentHashMap<>();

    public DecimalFormatSymbolsProviderImpl(LocaleProviderAdapter.Type type, Set<String> langtags) {
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
     * Returns a new <code>DecimalFormatSymbols</code> instance for the
     * specified locale.
     *
     * @param locale the desired locale
     * @exception NullPointerException if <code>locale</code> is null
     * @exception IllegalArgumentException if <code>locale</code> isn't
     *     one of the locales returned from
     *     {@link java.util.spi.LocaleServiceProvider#getAvailableLocales()
     *     getAvailableLocales()}.
     * @return a <code>DecimalFormatSymbols</code> instance.
     * @see java.text.DecimalFormatSymbols#getInstance(java.util.Locale)
     */
    @Override
    public DecimalFormatSymbols getInstance(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        return (DecimalFormatSymbols) cache.computeIfAbsent(locale, this::createFromJreResources).clone();
    }

    /**
     * Rebuilds the number-element fields of a {@code DecimalFormatSymbols} from the ported
     * JRE NumberElements, mirroring {@code java.text.DecimalFormatSymbols#initialize}. The
     * currency symbol fields are left as populated by {@code new DecimalFormatSymbols} since
     * they are derived from {@code java.util.Currency}/the currency-name providers rather
     * than the number-format resource bundle.
     */
    private DecimalFormatSymbols createFromJreResources(Locale locale) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
        String[] ne = (String[]) LocaleProviderAdapter.forType(type)
            .getLocaleResources(locale).getDecimalFormatSymbolsData()[0];

        char decimalSeparator = ne[0].charAt(0);
        char groupingSeparator = ne[1].charAt(0);
        dfs.setDecimalSeparator(decimalSeparator);
        dfs.setGroupingSeparator(groupingSeparator);
        dfs.setPatternSeparator(ne[2].charAt(0));
        dfs.setPercent(ne[3].charAt(0));
        dfs.setZeroDigit(ne[4].charAt(0));
        dfs.setDigit(ne[5].charAt(0));
        dfs.setMinusSign(ne[6].charAt(0));
        dfs.setExponentSeparator(ne[7]);
        dfs.setPerMill(ne[8].charAt(0));
        dfs.setInfinity(ne[9]);
        dfs.setNaN(ne[10]);
        dfs.setMonetaryDecimalSeparator(
            ne.length < 12 || ne[11].isEmpty() ? decimalSeparator : ne[11].charAt(0));
        dfs.setMonetaryGroupingSeparator(
            ne.length < 13 || ne[12].isEmpty() ? groupingSeparator : ne[12].charAt(0));
        return dfs;
    }

    @Override
    public Set<String> getAvailableLanguageTags() {
        return langtags;
    }
}
