/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Implements java.text.spi.NumberFormatProvider by delegating to the repackaged JDK 17 JRE/COMPAT provider (com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider), so a newer JDK can reproduce JDK 17 locale data when this jar is installed as an SPI provider. Also implements getCompactNumberInstance, which the SPI base class otherwise rejects with UnsupportedOperationException.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactNumberFormatProvider extends NumberFormatProvider {

  private final NumberFormatProvider delegate = LocaleProviderAdapter.forJRE().getNumberFormatProvider();

  private final Locale[] availableLocales = delegate.getAvailableLocales();

  @Override
  public Locale[] getAvailableLocales() {
    return availableLocales.clone();
  }

  @Override
  public boolean isSupportedLocale(Locale locale) {
    return delegate.isSupportedLocale(locale);
  }

  // NumberFormatProvider#getCompactNumberInstance (JDK 12+) throws
  // UnsupportedOperationException unless overridden. JDK 17's JRE provider implemented it, so
  // without this override a compact number request would fail here instead of being answered.
  @Override
  public NumberFormat getCompactNumberInstance(Locale locale, NumberFormat.Style formatStyle) {
    return delegate.getCompactNumberInstance(locale, formatStyle);
  }

  @Override
  public NumberFormat getCurrencyInstance(Locale locale) {
    return delegate.getCurrencyInstance(locale);
  }

  @Override
  public NumberFormat getIntegerInstance(Locale locale) {
    return delegate.getIntegerInstance(locale);
  }

  @Override
  public NumberFormat getNumberInstance(Locale locale) {
    return delegate.getNumberInstance(locale);
  }

  @Override
  public NumberFormat getPercentInstance(Locale locale) {
    return delegate.getPercentInstance(locale);
  }
}
