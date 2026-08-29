package com.appiancorp.jre17.compact;

import java.text.Collator;
import java.text.spi.CollatorProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactCollatorProvider extends CollatorProvider {

  private final CollatorProvider delegate = LocaleProviderAdapter.forJRE().getCollatorProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public Collator getInstance(Locale locale) {
    return delegate.getInstance(locale);
  }
}
