# NOTICE

This project includes source code copied from OpenJDK
(https://github.com/openjdk/jdk17u), licensed under the GNU General Public
License, version 2, with the Classpath Exception. See LICENSE for the full text.

## Derivative work

This repository contains a derivative work of [OpenJDK](https://github.com/openjdk/jdk17u)
(commit `a891cf0c38a201bcb97dc31f94b904ba59065b80`), which is licensed under
GPLv2 with Classpath Exception.

All copied files live under `src/main/thirdparty/`. Each modified file
carries an Appian modification notice in its license header identifying it
as changed, with a brief description of the change; the header is preceded
by the original, unmodified OpenJDK/Oracle copyright and license block
(and, for some files, an additional legacy Taligent/IBM copyright block),
which must not be altered or removed.

The following 374 files under `src/main/thirdparty/` have been modified
from their original OpenJDK sources (all other files under that directory,
including the `*.properties`, tzdata, and Unicode data files, are
unmodified, byte-for-byte copies -- see
`src/main/thirdparty/README.md` for their SHA-256 provenance hashes):

- `com/appiancorp/jre17/compact/thirdparty/FloatingDecimal.java`
- `com/appiancorp/jre17/compact/thirdparty/FDBigInteger.java`
- `com/appiancorp/jre17/compact/thirdparty/DoubleConsts.java`
- `com/appiancorp/jre17/compact/thirdparty/FloatConsts.java`
- All 371 files under
  `com/appiancorp/jre17/compact/thirdparty/{build,sun}/**/*.java` and
  `com/appiancorp/jre17/compact/thirdparty/sun/util/locale/provider/LocaleDataMetaInfo-XLocales.java.template`
  (the JRE/COMPAT locale provider stack)

Nature of the modifications (see each file's own header for its specific
description, and `src/main/thirdparty/README.md` for full file-level
provenance):

1. **Repackaging** (all 374 files): every file's `package` declaration and
   internal imports were changed from their original OpenJDK package
   (`sun.*`, `jdk.internal.math.*`, `build.tools.*`) to
   `com.appiancorp.jre17.compact.thirdparty.*`, so the classes can be
   compiled and loaded outside the JDK's own module system.
2. **Targeted adaptations** (a small subset of the 374, e.g.
   `FDBigInteger.java`, `Bundles.java`, `LocaleProviderAdapter.java`,
   `JRELocaleProviderAdapter.java`, `CalendarSystem.java`,
   `ZoneInfoFile.java`, `TimeZoneNameUtility.java`,
   `GenerateBreakIteratorData.java`,
   `{Open,Parallel}ListResourceBundle.java`,
   `BreakIteratorResourceBundle.java`,
   `{BreakIteratorInfo,BreakIteratorRules,CollationData}.java`,
   `DateFormatSymbolsProviderImpl.java`,
   `DecimalFormatSymbolsProviderImpl.java`): beyond the mechanical
   repackage, these files needed small behavioral changes to compile and
   run correctly outside the JDK's own module/build system -- replacing
   JDK-internal-only APIs (`jdk.internal.misc.CDS`,
   `jdk.internal.access.SharedSecrets`) that are unreachable from ordinary
   application code, adjusting resource/classpath lookups to this
   project's build layout, and preventing these providers from being
   shadowed by the JDK's own built-in CLDR provider.

Two additional files under `src/main/thirdparty/com/appiancorp/jre17/compact/util/`
(`AppianListResourceBundle.java`, `AppianResourceBundleState.java`) are
original Appian code, not derived from OpenJDK, and carry an ordinary
Appian copyright header rather than a GPLv2 modification notice.
