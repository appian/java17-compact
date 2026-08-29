package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.Map;
import java.util.spi.CalendarNameProvider;

// stub implementation for jre17 compatibility
public class Java17CompactCalendarNameProvider extends CalendarNameProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String getDisplayName(String calendarType, int field, int value, int style, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Map<String, Integer> getDisplayNames(String calendarType, int field, int style, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
