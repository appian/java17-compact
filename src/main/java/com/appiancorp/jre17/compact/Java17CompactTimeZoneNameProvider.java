package com.appiancorp.jre17.compact;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.spi.TimeZoneNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactTimeZoneNameProvider extends TimeZoneNameProvider {

  private final TimeZoneNameProvider delegate = LocaleProviderAdapter.forJRE().getTimeZoneNameProvider();

  // Locales this provider advertises. See getAvailableLocales() for why this is
  // broadened beyond the delegate's language-level locales.
  private final Locale[] availableLocales = computeAvailableLocales();

  private Locale[] computeAvailableLocales() {
    // The delegate reports language-level locales only (e.g. "en", "de", "ja"),
    // never country variants (e.g. "en_US").
    //
    // When this jar is used as an SPI drop-in (java.locale.providers=SPI,CLDR),
    // the JDK wraps this provider in sun.util.locale.provider
    // .SPILocaleProviderAdapter$*Delegate. That delegate decides whether the SPI
    // is even *consulted* for a locale via isSupportedLocale, which is a direct
    // map lookup (map.get(locale)) with NO language fallback -- the map keys are
    // exactly the Locales returned here. Meanwhile the per-ID zone-name lookup
    // (LocaleServiceProviderPool) walks candidate locales [en_US, en, root] in
    // adapter-preference order (SPI before CLDR) and consults, at each candidate,
    // only the providers reported as supporting THAT candidate.
    //
    // If we advertise "en" but not "en_US", the SPI is skipped at the en_US
    // candidate; CLDR is consulted there instead and, on a modern JDK, returns a
    // synthesized GMT-offset short name (e.g. "GMT+14:00" for Pacific/Kiritimati)
    // rather than null -- which short-circuits the lookup before the "en"
    // candidate (where we would supply the JDK 17 abbreviation "LINT") is reached.
    // JDK 17 (COMPAT) produced "LINT" here because its JRE stack served zone names
    // for every locale via English/root fallback and CLDR did not shadow it.
    //
    // To restore that behavior we advertise, for every language the delegate
    // serves, that language plus its country variants (language x ISO country),
    // so the SPI is selected (ahead of CLDR) at the country candidate and
    // supplies the JDK 17 name. Name resolution itself already performs the
    // language/root fallback, so this only affects provider selection, not the
    // values returned.
    //
    // Country codes come from Locale.getISOCountries() (a static ISO 3166 table)
    // rather than Locale.getAvailableLocales(): the latter forces locale-provider
    // pool initialization, which instantiates this very provider, causing
    // re-entrant initialization failure.
    Locale[] base = delegate.getAvailableLocales();
    Set<Locale> result = new LinkedHashSet<>(Arrays.asList(base));
    String[] countries = Locale.getISOCountries();
    for (Locale l : base) {
      String language = l.getLanguage();
      if (language.isEmpty()) {
        continue;
      }
      for (String country : countries) {
        result.add(new Locale.Builder().setLanguage(language).setRegion(country).build());
      }
    }
    return result.toArray(new Locale[0]);
  }

  @Override
  public Locale[] getAvailableLocales() {
    return availableLocales.clone();
  }

  @Override
  public boolean isSupportedLocale(Locale locale) {
    if (delegate.isSupportedLocale(locale)) {
      return true;
    }
    // Country/variant locales are supported when the delegate serves their
    // language (English/root fallback), matching JDK 17 COMPAT behavior.
    Locale stripped = locale.stripExtensions();
    if (stripped.getCountry().isEmpty() && stripped.getVariant().isEmpty()) {
      return false;
    }
    Locale languageOnly = new Locale.Builder().setLanguage(stripped.getLanguage()).build();
    return delegate.isSupportedLocale(languageOnly);
  }

  @Override
  public String getDisplayName(String id, boolean daylight, int style, Locale locale) {
    return delegate.getDisplayName(id, daylight, style, locale);
  }

  @Override
  public String getGenericDisplayName(String id, int style, Locale locale) {
    return delegate.getGenericDisplayName(id, style, locale);
  }
}
