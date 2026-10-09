# JRE17 Compatibility Library

Library which contains Utilities for JRE17 compatibility.

It ports the JDK 17 legacy `JRE`/`COMPAT` locale-provider stack (removed from the
JDK after 18/20) and exposes it as standard locale Service Provider Interface
(SPI) implementations, so a newer JDK (e.g. JDK 25) can reproduce JDK 17's
`java.locale.providers=JRE`/`COMPAT` locale behavior.

## Locale SPI providers

The jar registers implementations for all eleven locale SPIs via committed
`META-INF/services` files, so it is a genuine **drop-in**: put the jar on the
system class path and set `java.locale.providers=SPI`.

Registered providers:

- `java.text.spi`: `BreakIteratorProvider`, `CollatorProvider`,
  `DateFormatProvider`, `DateFormatSymbolsProvider`,
  `DecimalFormatSymbolsProvider`, `NumberFormatProvider`
- `java.util.spi`: `CalendarDataProvider`, `CalendarNameProvider`,
  `CurrencyNameProvider`, `LocaleNameProvider`, `TimeZoneNameProvider`

### Usage

Replace `-Djava.locale.providers=JRE,SPI` (JDK 17) with `-Djava.locale.providers=SPI`:

```sh
java -Djava.locale.providers=SPI \
  -cp 'com.appiancorp.jre17.compact.jar:application.jar' \
  com.example.Application
```

- The jar must be on the **system class path**. The JDK loads SPI providers only through the
  system class loader, so `WEB-INF/lib` of a web application is not enough.
- Do **not** add `CLDR` (`SPI,CLDR`). CLDR answers every locale the jar does not list exactly and
  supplies the join patterns for `Locale#getDisplayName`, so results no longer match JDK 17.

With `SPI` the output matches JDK 17 `JRE,SPI`. The one known difference is the name of a Unicode
`rg` extension (`fr_FR-u-rg-chzzzz`): "Switzerland" instead of "Suisse".

## Build

```sh
./gradlew jar
```

The jar is written to `build/libs/com.appiancorp.jre17.compact-1.0.0.jar`.

## Legacy numeric formatting

`LegacyDouble` / `LegacyFloat` reproduce JDK 17's `Double.toString` /
`Float.toString` output via the ported `FloatingDecimal`.

## License / third-party notices

This project contains a derivative work of [OpenJDK](https://github.com/openjdk/jdk17u),
licensed under the GNU General Public License, version 2, with the Classpath
Exception. See [LICENSE](LICENSE) for the full license text and
[NOTICE.md](NOTICE.md) for the derivative-work statement and the list of
modified files. Full per-file provenance (original OpenJDK path, copyright,
and SHA-256 hash) is in [src/main/thirdparty/README.md](src/main/thirdparty/README.md).
