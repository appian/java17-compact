package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.TimeZoneNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactTimeZoneNameProvider extends TimeZoneNameProvider {

  private final TimeZoneNameProvider delegate = LocaleProviderAdapter.forJRE().getTimeZoneNameProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public String getDisplayName(String id, boolean daylight, int style, Locale locale) {
    return delegate.getDisplayName(id, daylight, style, locale);
  }
}
