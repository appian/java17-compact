/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Implements java.util.spi.LocaleNameProvider by delegating to the repackaged JDK 17 JRE/COMPAT provider (com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider), so a newer JDK can reproduce JDK 17 locale data when this jar is installed as an SPI provider. Also implements getDisplayScript and the Unicode-extension name methods, which the SPI base class otherwise leaves unanswered.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.LocaleNameProvider;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactLocaleNameProvider extends LocaleNameProvider {

  private final LocaleNameProvider delegate = LocaleProviderAdapter.forJRE().getLocaleNameProvider();

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
  public String getDisplayLanguage(String languageCode, Locale locale) {
    return delegate.getDisplayLanguage(languageCode, locale);
  }

  // getDisplayScript and the two Unicode-extension methods default to null in
  // LocaleNameProvider, so script names ("Latin") and extension names ("ca:japanese") would not
  // be answered at all. JDK 17's JRE provider implemented all three.
  @Override
  public String getDisplayScript(String scriptCode, Locale locale) {
    return delegate.getDisplayScript(scriptCode, locale);
  }

  @Override
  public String getDisplayUnicodeExtensionKey(String key, Locale locale) {
    return delegate.getDisplayUnicodeExtensionKey(key, locale);
  }

  @Override
  public String getDisplayUnicodeExtensionType(String extType, String key, Locale locale) {
    return delegate.getDisplayUnicodeExtensionType(extType, key, locale);
  }

  @Override
  public String getDisplayCountry(String countryCode, Locale locale) {
    return delegate.getDisplayCountry(countryCode, locale);
  }

  @Override
  public String getDisplayVariant(String variant, Locale locale) {
    return delegate.getDisplayVariant(variant, locale);
  }
}
