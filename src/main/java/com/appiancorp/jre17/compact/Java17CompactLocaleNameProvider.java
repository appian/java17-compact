package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.LocaleNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactLocaleNameProvider extends LocaleNameProvider {

  private final LocaleNameProvider delegate = LocaleProviderAdapter.forJRE().getLocaleNameProvider();

  // Broadened to include country variants so this provider is selected (ahead of
  // CLDR) for country locales under java.locale.providers=SPI,CLDR. See CompactLocaleSupport.
  private final Locale[] availableLocales = CompactLocaleSupport.withCountryVariants(delegate.getAvailableLocales());

  @Override
  public Locale[] getAvailableLocales() {
    return availableLocales.clone();
  }

  @Override
  public boolean isSupportedLocale(Locale locale) {
    return CompactLocaleSupport.isSupportedLocale(delegate, locale);
  }

  @Override
  public String getDisplayLanguage(String languageCode, Locale locale) {
    return delegate.getDisplayLanguage(languageCode, locale);
  }

  @Override
  public String getDisplayCountry(String countryCode, Locale locale) {
    return delegate.getDisplayCountry(countryCode, locale);
  }

  @Override
  public String getDisplayVariant(String variant, Locale locale) {
    return delegate.getDisplayVariant(variant, locale);
  }
}
