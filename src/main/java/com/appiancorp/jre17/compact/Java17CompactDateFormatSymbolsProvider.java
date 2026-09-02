package com.appiancorp.jre17.compact;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactDateFormatSymbolsProvider extends DateFormatSymbolsProvider {

  private final DateFormatSymbolsProvider delegate = LocaleProviderAdapter.forJRE().getDateFormatSymbolsProvider();

  // Broadened to include country variants so this provider is selected (ahead of CLDR)
  // for locales like de_DE under java.locale.providers=SPI,CLDR; otherwise CLDR shadows it
  // and returns e.g. "März" for German March instead of the JDK 17 "Mär". See
  // CompactLocaleSupport.
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
  public DateFormatSymbols getInstance(Locale locale) {
    return delegate.getInstance(locale);
  }
}
