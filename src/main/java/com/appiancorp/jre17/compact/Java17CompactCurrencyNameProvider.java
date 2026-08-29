package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.CurrencyNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactCurrencyNameProvider extends CurrencyNameProvider {

  private final CurrencyNameProvider delegate = LocaleProviderAdapter.forJRE().getCurrencyNameProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public String getSymbol(String currencyCode, Locale locale) {
    return delegate.getSymbol(currencyCode, locale);
  }

  @Override
  public String getDisplayName(String currencyCode, Locale locale) {
    return delegate.getDisplayName(currencyCode, locale);
  }
}
