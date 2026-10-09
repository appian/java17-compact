/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Implements java.util.spi.CalendarNameProvider by delegating to the repackaged JDK 17 JRE/COMPAT provider (com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider), so a newer JDK can reproduce JDK 17 locale data when this jar is installed as an SPI provider.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
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

  private final Locale[] availableLocales = delegate.getAvailableLocales();

  @Override
  public Locale[] getAvailableLocales() {
    return availableLocales.clone();
  }

  @Override
  public boolean isSupportedLocale(Locale locale) {
    return delegate.isSupportedLocale(locale);
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
