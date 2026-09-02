package com.appiancorp.jre17.compact;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.spi.LocaleServiceProvider;

/**
 * Helpers that let the compact SPI providers be <em>selected</em> for country/region
 * locales (e.g. {@code en_US}, {@code de_DE}) when used as a drop-in with
 * {@code java.locale.providers=SPI,CLDR}.
 *
 * <p>The JDK wraps each installed SPI provider in
 * {@code sun.util.locale.provider.SPILocaleProviderAdapter$*Delegate}. That delegate
 * decides whether the SPI is consulted for a locale via {@code isSupportedLocale}, which
 * is a direct {@code map.get(locale)} keyed by the provider's {@code getAvailableLocales()}
 * with <strong>no</strong> language fallback. The repackaged JRE providers only advertise
 * language-level locales (e.g. {@code de}), never country variants (e.g. {@code de_DE}), so
 * they are skipped at the country candidate and CLDR shadows them there -- returning CLDR
 * (or, with {@code SPI} only, root/English) data instead of the JDK 17 JRE/COMPAT values.
 *
 * <p>Advertising every {@code language x ISO-country} combination for each served language
 * registers those keys in the delegate map, so the compact provider wins (ahead of CLDR) at
 * the country candidate. Name/data resolution itself already performs language/root
 * fallback, so this only affects provider selection, not the values returned.
 */
final class CompactLocaleSupport {

    private CompactLocaleSupport() {
    }

    /**
     * Expands the given language-level locales to also include their {@code language x
     * ISO-country} variants.
     *
     * <p>Country codes come from {@link Locale#getISOCountries()} (a static ISO 3166 table)
     * rather than {@link Locale#getAvailableLocales()}: the latter forces locale-provider
     * pool initialization, which instantiates the very provider calling this, causing
     * re-entrant initialization failure.
     */
    static Locale[] withCountryVariants(Locale[] base) {
        Set<Locale> result = new LinkedHashSet<>(Arrays.asList(base));
        String[] countries = Locale.getISOCountries();
        for (Locale locale : base) {
            String language = locale.getLanguage();
            if (language.isEmpty()) {
                continue;
            }
            for (String country : countries) {
                result.add(new Locale.Builder().setLanguage(language).setRegion(country).build());
            }
        }
        return result.toArray(new Locale[0]);
    }

    /**
     * Reports a locale as supported when the delegate supports it directly, or -- for a
     * country/variant locale -- when the delegate supports its language (English/root
     * fallback), matching JDK 17 COMPAT behavior.
     */
    static boolean isSupportedLocale(LocaleServiceProvider delegate, Locale locale) {
        if (delegate.isSupportedLocale(locale)) {
            return true;
        }
        Locale stripped = locale.stripExtensions();
        if (stripped.getCountry().isEmpty() && stripped.getVariant().isEmpty()) {
            return false;
        }
        return delegate.isSupportedLocale(new Locale.Builder().setLanguage(stripped.getLanguage()).build());
    }
}
