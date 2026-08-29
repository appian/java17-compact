package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.CalendarDataProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactCalendarDataProvider extends CalendarDataProvider {

  private final CalendarDataProvider delegate = LocaleProviderAdapter.forJRE().getCalendarDataProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public int getFirstDayOfWeek(Locale locale) {
    return delegate.getFirstDayOfWeek(locale);
  }

  @Override
  public int getMinimalDaysInFirstWeek(Locale locale) {
    return delegate.getMinimalDaysInFirstWeek(locale);
  }
}
