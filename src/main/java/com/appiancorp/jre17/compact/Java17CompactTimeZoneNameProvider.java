package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.TimeZoneNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactTimeZoneNameProvider extends TimeZoneNameProvider {

  private final TimeZoneNameProvider delegate = LocaleProviderAdapter.forJRE().getTimeZoneNameProvider();

  // Broadened to include country variants so this provider is selected (ahead of CLDR)
  // for locales like en_US under java.locale.providers=SPI,CLDR. See CompactLocaleSupport.
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
  public String getDisplayName(String id, boolean daylight, int style, Locale locale) {
    return delegate.getDisplayName(id, daylight, style, locale);
  }

  @Override
  public String getGenericDisplayName(String id, int style, Locale locale) {
    return delegate.getGenericDisplayName(id, style, locale);
  }
}
