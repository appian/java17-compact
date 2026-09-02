package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.Map;
import java.util.spi.CalendarNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactCalendarNameProvider extends CalendarNameProvider {

  private final CalendarNameProvider delegate = LocaleProviderAdapter.forJRE().getCalendarNameProvider();

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
  public String getDisplayName(String calendarType, int field, int value, int style, Locale locale) {
    return delegate.getDisplayName(calendarType, field, value, style, locale);
  }

  @Override
  public Map<String, Integer> getDisplayNames(String calendarType, int field, int style, Locale locale) {
    return delegate.getDisplayNames(calendarType, field, style, locale);
  }
}
