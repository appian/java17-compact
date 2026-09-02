# JRE17 Compatibility Library

Library which contains Utilities for JRE17 compatibility.

It ports the JDK 17 legacy `JRE`/`COMPAT` locale-provider stack (removed from the
JDK after 18/20) and exposes it as standard locale Service Provider Interface
(SPI) implementations, so a newer JDK (e.g. JDK 25) can reproduce JDK 17's
`java.locale.providers=JRE`/`COMPAT` locale behavior.

## Locale SPI providers

The jar registers implementations for all eleven locale SPIs via committed
`META-INF/services` files, so it is a genuine **drop-in**: put the jar on the
class path and put `SPI` first in `java.locale.providers`.

Registered providers:

- `java.text.spi`: `BreakIteratorProvider`, `CollatorProvider`,
  `DateFormatProvider`, `DateFormatSymbolsProvider`,
  `DecimalFormatSymbolsProvider`, `NumberFormatProvider`
- `java.util.spi`: `CalendarDataProvider`, `CalendarNameProvider`,
  `CurrencyNameProvider`, `LocaleNameProvider`, `TimeZoneNameProvider`

### Usage

```sh
java -Djava.locale.providers=SPI,CLDR \
  -cp 'com.appiancorp.jre17.compact.jar:application.jar' \
  com.example.Application
```

With `SPI,CLDR`, the JDK consults these providers first (JDK 17 JRE/COMPAT
data) and falls back to CLDR for anything they do not supply. For example, on a
JDK 25/26 runtime the symbol for `TWD` in `zh-HK` resolves to `TWD` (JDK 17
JRE/COMPAT behavior) instead of the CLDR value `NT$`.

## Build

```sh
./gradlew jar
```

The jar is written to `build/libs/com.appiancorp.jre17.compact-1.0.0.jar`.

## Legacy numeric formatting

`LegacyDouble` / `LegacyFloat` reproduce JDK 17's `Double.toString` /
`Float.toString` output via the ported `FloatingDecimal`.
