package com.appiancorp.jre17.compact;

import java.text.DateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactDateFormatProvider extends DateFormatProvider {

  private final DateFormatProvider delegate = LocaleProviderAdapter.forJRE().getDateFormatProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public DateFormat getTimeInstance(int style, Locale locale) {
    return delegate.getTimeInstance(style, locale);
  }

  @Override
  public DateFormat getDateInstance(int style, Locale locale) {
    return delegate.getDateInstance(style, locale);
  }

  @Override
  public DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) {
    return delegate.getDateTimeInstance(dateStyle, timeStyle, locale);
  }
}
