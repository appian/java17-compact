package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.CalendarDataProvider;

// stub implementation for jre17 compatibility
public class Java17CompactCalendarDataProvider extends CalendarDataProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public int getFirstDayOfWeek(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public int getMinimalDaysInFirstWeek(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
