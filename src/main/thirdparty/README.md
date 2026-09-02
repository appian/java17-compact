# Third-Party Source

This folder contains source code that was copied from [OpenJDK](https://github.com/openjdk/jdk17u/), Later Modified.
All the files in here are copied under OpenJDK 17's GPLv2 with Classpath Exception.

Source repository: `git@github.com:openjdk/jdk17u.git`, commit `a891cf0c38a201bcb97dc31f94b904ba59065b80`.

| File | Path in OpenJDK | File Hash (SHA-256) | Last Modified in OpenJDK |
|---|---|---|---|
| FloatingDecimal.java | src/java.base/share/classes/jdk/internal/math/FloatingDecimal.java | 3bdf29124dd5f43fa01ea82a552d2ff9db1fc39bbc1e929888bb664fe4bf8d8b | 2019-08-27 |
| FDBigInteger.java | src/java.base/share/classes/jdk/internal/math/FDBigInteger.java | 0f24de911fd5b635826b702455040570aa9f172028844ecd7d88ed4f5763a8f0 | 2020-09-24 |
| DoubleConsts.java | src/java.base/share/classes/jdk/internal/math/DoubleConsts.java | 5c1967466c55f0531e644eb7ddaff2cd99ce93e719805b13b1023e9bf37a8434 | 2017-09-12 |
| FloatConsts.java | src/java.base/share/classes/jdk/internal/math/FloatConsts.java | c6cbbb8e899cb5e9a0eff4d3016b26cb6c2b939b77f14bf492aa531b39421dd0 | 2017-09-12 |

## JRE locale provider (`java.locale.providers=JRE`) sources

The following files implement the legacy JRE locale service provider stack
(`sun.util.locale.provider.JRELocaleProviderAdapter` and everything it needs:
resource bundle infrastructure, calendar support, and the `tzdb.dat` compiler
used to generate time-zone alias data), removed from the JDK when
`java.locale.providers=JRE`/`COMPAT` support was dropped after JDK 18/20.

All files below were copied verbatim (byte-for-byte, verified via SHA-256) from
`openjdk/jdk17u` into
`src/main/thirdparty/com/appiancorp/jre17/compact/thirdparty/`, mirroring
each file's original OpenJDK relative package path under that root (e.g.
`sun/util/locale/provider/JRELocaleProviderAdapter.java`,
`build/tools/tzdb/TzdbZoneRulesCompiler.java`, `tzdata/europe`). No content
was altered, generated, reformatted, or repackaged in this copy step --
`package`/`import` statements are byte-for-byte identical to the OpenJDK
source; repackaging into `com.appiancorp.jre17.compact.thirdparty` happens in
a subsequent commit.

| File | Path in OpenJDK | File Hash (SHA-256) | Last Modified in OpenJDK |
|---|---|---|---|
| TzdbZoneRulesCompiler.java | make/jdk/src/classes/build/tools/tzdb/TzdbZoneRulesCompiler.java | 0363805b6a5c69e9d4d257ac046c704405a194a975b5e5ba1ca8d1ba1cda8cb4 | 2024-11-28 |
| TzdbZoneRulesProvider.java | make/jdk/src/classes/build/tools/tzdb/TzdbZoneRulesProvider.java | 1ecd0949bcbfb68b40a079bb721cce626c9a3596d088015d335e4c3b63beba87 | 2024-11-06 |
| Ser.java | src/java.base/share/classes/java/time/zone/Ser.java | 4fb96f43cd3a73d890786b48aebd41db3a4f918e24b2a3c74c600811a1bab77b | 2019-09-26 |
| ZoneOffsetTransition.java | src/java.base/share/classes/java/time/zone/ZoneOffsetTransition.java | 72773bde53366f54dbadbafac5a0c032700b593e15451bd1dae24589a06a9c54 | 2021-04-22 |
| ZoneOffsetTransitionRule.java | src/java.base/share/classes/java/time/zone/ZoneOffsetTransitionRule.java | ab387ffb3342606b5f3418a2019905224b55aa2ba2f913b866e5069d9e89fe44 | 2021-04-22 |
| ZoneRules.java | src/java.base/share/classes/java/time/zone/ZoneRules.java | 75a82ed51eb401d5a302f636b0b52c2da04b374d65a2ff77bcfa3bc85fed4f98 | 2021-03-04 |
| GetPropertyAction.java | src/java.base/share/classes/sun/security/action/GetPropertyAction.java | 725e831bca45704979127b348c71f652352a28f8654f4ba99d8de411b36e0483 | 2025-01-17 |
| Debug.java | src/java.base/share/classes/sun/security/util/Debug.java | dad10eef7b03c1a5733e4ab691338659ab0338dbb8b89292ca0216910cb92f8d | 2024-09-23 |
| BreakIteratorInfo.java | src/java.base/share/classes/sun/text/resources/BreakIteratorInfo.java | bbde77e39da6f69b631197218a707dc5c8da6d2fe003cb70a7a3c00f4352a19b | 2017-09-12 |
| BreakIteratorInfoProvider.java | src/java.base/share/classes/sun/text/resources/BreakIteratorInfoProvider.java | ca40062f1a304c20f875c5e8e897bcbc71d579b75cadace00acf6b60900eb6fa | 2017-09-12 |
| BreakIteratorResources.java | src/java.base/share/classes/sun/text/resources/BreakIteratorResources.java | 9c6214b9114c8c04c322171492a753b73f2ee79d80e8173d2a6e319cd4d32576 | 2017-09-12 |
| BreakIteratorRules.java | src/java.base/share/classes/sun/text/resources/BreakIteratorRules.java | 7ac4cca9f001b7e1e3b9db76a82a899104fcb743947d34ffe012a89bf5466a4b | 2021-04-09 |
| CollationData.java | src/java.base/share/classes/sun/text/resources/CollationData.java | 67ac34d08787a916aa0afde50be297af83995dc2a7126d0b72c482455f9a05a6 | 2017-09-12 |
| CollationDataProvider.java | src/java.base/share/classes/sun/text/resources/CollationDataProvider.java | f6d17b6507d5c412d5f9d3124870b09f53fb7f943eef16d74e4a79b1220e0083 | 2017-09-12 |
| FormatData.java | src/java.base/share/classes/sun/text/resources/FormatData.java | 5d5dc37bbbfbd834bd9192faa0e466c1c44fe5b839a263012332806aa7e92ede | 2019-04-01 |
| FormatDataProvider.java | src/java.base/share/classes/sun/text/resources/FormatDataProvider.java | 7e284382186ec11f5de857658429b11cfe62110f8598c5da9f2e36fc69d3205d | 2017-09-12 |
| FormatData_en.java | src/java.base/share/classes/sun/text/resources/FormatData_en.java | 0642e6847bc3dd822a6802d4ee29a9b25ffbd8c520bdff71d95ebfad9cdd5621 | 2017-09-12 |
| FormatData_en_US.java | src/java.base/share/classes/sun/text/resources/FormatData_en_US.java | 3d34a619fc40f16d8368e45f5cd7e08258a67dab1e369c18c43ed3f61868382d | 2017-09-12 |
| JavaTimeSupplementary.java | src/java.base/share/classes/sun/text/resources/JavaTimeSupplementary.java | e37f0ccc357bfa6475f2ec37ceaa97940d314f120c5e1816e925b652694b85e2 | 2019-04-01 |
| JavaTimeSupplementaryProvider.java | src/java.base/share/classes/sun/text/resources/JavaTimeSupplementaryProvider.java | 117edc3a188654fca24d8e7bdd11b4543cfc469fd0e064370a2542f74027b1f2 | 2017-09-12 |
| JavaTimeSupplementary_en.java | src/java.base/share/classes/sun/text/resources/JavaTimeSupplementary_en.java | cb6fb22ef062384afb996d3b84bf037809bb646db71ab1295ed5ba5eedab84e7 | 2017-09-12 |
| JavaTimeDateTimePatternProvider.java | src/java.base/share/classes/sun/text/spi/JavaTimeDateTimePatternProvider.java | 648add11bb63ea54b0e6ff3422e8f64ce1a1651dbeca89040a27c7c3e12b3619 | 2017-09-12 |
| AbstractCalendar.java | src/java.base/share/classes/sun/util/calendar/AbstractCalendar.java | 09273342050e30a64ea2759785d12efd4db4e9104c9f286e3e5b72cc4a41458b | 2017-09-12 |
| BaseCalendar.java | src/java.base/share/classes/sun/util/calendar/BaseCalendar.java | b1657f23bda297dbb8a1bdbe8456161cc9396673d07cf652df1d1c609e365dfb | 2021-09-29 |
| CalendarDate.java | src/java.base/share/classes/sun/util/calendar/CalendarDate.java | e35b0c202833c0f9eea8e23ecc2e92fb8b8670ab3f49c667b182f833352b7faa | 2017-09-12 |
| CalendarSystem.java | src/java.base/share/classes/sun/util/calendar/CalendarSystem.java | 6ab0aeb8880e9eb967dba8d86628911d2d8191184fdc54bde1b08af4fbe80f99 | 2021-10-11 |
| CalendarUtils.java | src/java.base/share/classes/sun/util/calendar/CalendarUtils.java | 4487d0337d3a91aad9748582815c129ad414cf21c52b3b581d497bf9d8c39aab | 2017-09-12 |
| Era.java | src/java.base/share/classes/sun/util/calendar/Era.java | ea60ce8c6b37719c1955cfdfa66c25385bbf97e7a40f74ecee0270b3eb101fe1 | 2019-04-01 |
| Gregorian.java | src/java.base/share/classes/sun/util/calendar/Gregorian.java | 35d6e8f6c68d1222cc774656759c386af2fb6128cbbf407b8d0b89f5d1ff810c | 2017-09-12 |
| ImmutableGregorianDate.java | src/java.base/share/classes/sun/util/calendar/ImmutableGregorianDate.java | 1bd8a71d7b4558f0f21f8120b785164bb6e2f030cdabdb9977c45a6f8eac7729 | 2017-09-12 |
| JulianCalendar.java | src/java.base/share/classes/sun/util/calendar/JulianCalendar.java | 2b817702dec865c5244d3a1b3301189b461b75f7ac5f24c35b1594047f54ef2b | 2017-09-12 |
| LocalGregorianCalendar.java | src/java.base/share/classes/sun/util/calendar/LocalGregorianCalendar.java | e1855f95de700710f0862eff145eb21f1a0eb584429c557c235a2e2c28b13078 | 2019-04-01 |
| ZoneInfo.java | src/java.base/share/classes/sun/util/calendar/ZoneInfo.java | b9e153a2cfd13f8fd0e32bb49867c69b2ebff8566f726ec3c3fa83453a6058d8 | 2025-10-28 |
| ZoneInfoFile.java | src/java.base/share/classes/sun/util/calendar/ZoneInfoFile.java | 74adaac162b693c6bbeea05ca72c5c134132b8bd841c5da4ea5c0d5357e20810 | 2025-10-28 |
| AuxLocaleProviderAdapter.java | src/java.base/share/classes/sun/util/locale/provider/AuxLocaleProviderAdapter.java | 4b3e61ef6a98dc113a4739c913592f31a2fbd2b63939a20dbabe8d5377da4893 | 2021-06-02 |
| AvailableLanguageTags.java | src/java.base/share/classes/sun/util/locale/provider/AvailableLanguageTags.java | c0ee838cb65a2821cbe2730019b0e18ad496754f16302f8b4a9525de41d02ef6 | 2017-09-12 |
| BreakIteratorProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/BreakIteratorProviderImpl.java | 6b3a96234da0d0df08d4bac5403717a51d5c5396c97bb5e034f73085af0a8b9e | 2017-09-12 |
| CalendarDataProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/CalendarDataProviderImpl.java | 07f6a5e490bd296d8e53e8e245bd8ac9413a8f46ddd24c66ce285336a3b48450 | 2017-12-12 |
| CalendarDataUtility.java | src/java.base/share/classes/sun/util/locale/provider/CalendarDataUtility.java | e444b4e0458cfe49b114b58a7533ee7a1920ea66fd485f2f7c58285b2f023af5 | 2019-06-28 |
| CalendarNameProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/CalendarNameProviderImpl.java | d0fb101969e93d68ce76e6860fb495ccff743e99209449cc023ed592c3f5366b | 2021-04-16 |
| CalendarProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/CalendarProviderImpl.java | eef92b2c364e5f5ff171b131c683396090864d27e7fe0770aafc6498cbd5c3d4 | 2017-09-12 |
| CollationRules.java | src/java.base/share/classes/sun/util/locale/provider/CollationRules.java | 896d4f646131dbc28e6c97e08bd2d5c350ea41aac8c3abce8dbba35dff5abbd2 | 2017-09-12 |
| CollatorProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/CollatorProviderImpl.java | 96e08e72fc734e6749ca62c26d7e2a8e1b2a11dad91193acf2019578a1dffde4 | 2017-09-12 |
| CurrencyNameProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/CurrencyNameProviderImpl.java | 55075222c14fe7d4eea7b77d36bb9f8ceb00b09d8eabe2ed8903527b0b4685d4 | 2017-09-12 |
| DateFormatProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/DateFormatProviderImpl.java | 2e75f842c78ffd0d7936516174b34c9cdced2c411b32e5abffea4cc47871d8fe | 2017-12-12 |
| DateFormatSymbolsProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/DateFormatSymbolsProviderImpl.java | 109a72dad5e9944db89395e8d0cc791904c6166862c0c910b79e02abf499be7d | 2017-09-12 |
| DecimalFormatSymbolsProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/DecimalFormatSymbolsProviderImpl.java | 0570128fbc0ad581241e37bbb8e1865f193f64a33ded390592d2c0a74178cafa | 2017-09-12 |
| FallbackLocaleProviderAdapter.java | src/java.base/share/classes/sun/util/locale/provider/FallbackLocaleProviderAdapter.java | e16d0cc8b3812262de949f28d45c4c31f74bace1824cbdd4a5721d18c33c69da | 2017-09-12 |
| HostLocaleProviderAdapter.java | src/java.base/share/classes/sun/util/locale/provider/HostLocaleProviderAdapter.java | f3d56eccd3deae58fa256962ef8ca994647df26743799352d661f151c0bf45ee | 2020-05-21 |
| JRELocaleConstants.java | src/java.base/share/classes/sun/util/locale/provider/JRELocaleConstants.java | 16c6dcf2cfc69f778d1bcf92e8d7be812945f82f4fa5a9c8a1a7587bdb477bc9 | 2017-09-12 |
| JRELocaleProviderAdapter.java | src/java.base/share/classes/sun/util/locale/provider/JRELocaleProviderAdapter.java | a1031ba02483df15ee09e625a3b50dd7a8cd3b9d146fbc2f0b6f7a11c992217a | 2021-06-02 |
| JavaTimeDateTimePatternImpl.java | src/java.base/share/classes/sun/util/locale/provider/JavaTimeDateTimePatternImpl.java | 6cc1cab21f22c147fadb7909e8467ae5680b205b89f8395dd11c9907f51e38fe | 2017-09-12 |
| LocaleDataMetaInfo-XLocales.java.template | src/java.base/share/classes/sun/util/locale/provider/LocaleDataMetaInfo-XLocales.java.template | 848ece92931bcc7626c80eb1ebeb7cf6c7a53f70d6326876f303664ab77191a8 | 2017-09-12 |
| LocaleDataMetaInfo.java | src/java.base/share/classes/sun/util/locale/provider/LocaleDataMetaInfo.java | 9a6cf95bedd3ad1d1a9a7607372b7ab7ea8693df4e3c5c7cb548d3ca672329b8 | 2018-04-30 |
| LocaleNameProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/LocaleNameProviderImpl.java | e464bb355ef655778d49260d20bca7d8b9ec439d9c4d75a0f4a89dfa6f2e8634 | 2017-12-12 |
| LocaleProviderAdapter.java | src/java.base/share/classes/sun/util/locale/provider/LocaleProviderAdapter.java | 2517817989af4539ec3ded31ed0e202ec53c93a1734c5ad64b08f03d26d04917 | 2020-05-21 |
| LocaleResources.java | src/java.base/share/classes/sun/util/locale/provider/LocaleResources.java | f21043189cc8ea00c9abf10d73e9a7e73a84a6ff2437f56b2c172a00e7c148ea | 2020-11-16 |
| LocaleServiceProviderPool.java | src/java.base/share/classes/sun/util/locale/provider/LocaleServiceProviderPool.java | da6ee6b6de5fa2df3ea8d69e528b86f65f1719c1efab436b6cf34408fab3d976 | 2020-05-21 |
| NumberFormatProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/NumberFormatProviderImpl.java | f34fd5f7a2940d609c29782abf74bdafa339a7dedb771616ae43ec82a81616ad | 2020-11-16 |
| ResourceBundleBasedAdapter.java | src/java.base/share/classes/sun/util/locale/provider/ResourceBundleBasedAdapter.java | cfd10084a16298ce3d60c89db3b507e4644974e6beaf49f2272cf34af9e27349 | 2017-09-12 |
| SPILocaleProviderAdapter.java | src/java.base/share/classes/sun/util/locale/provider/SPILocaleProviderAdapter.java | d3201ebc2cbd48ff08bd8904b123f5bb6390ff058f217f0a6a1351317415219a | 2021-06-02 |
| TimeZoneNameProviderImpl.java | src/java.base/share/classes/sun/util/locale/provider/TimeZoneNameProviderImpl.java | 2ecf2cfd1b974a091488e9ba5f70782902d74f6624359a7ff90cb4834c5f5c88 | 2018-04-26 |
| TimeZoneNameUtility.java | src/java.base/share/classes/sun/util/locale/provider/TimeZoneNameUtility.java | 3bbd02877334e20489592594334690dc4d05a23555810b72fb1a3cef18ecb097 | 2018-04-26 |
| BreakIteratorResourceBundle.java | src/java.base/share/classes/sun/util/resources/BreakIteratorResourceBundle.java | af0ac8a6bc3bc96b0053535b87db05f1bcf9af84c6e18a35e265eaefb1861d9a | 2021-06-02 |
| Bundles.java | src/java.base/share/classes/sun/util/resources/Bundles.java | 8b8ff66d088b929d3fec50ca590cf030e4923e9a905064e668c75e03df5f04ca | 2021-06-02 |
| CalendarDataProvider.java | src/java.base/share/classes/sun/util/resources/CalendarDataProvider.java | b6c563b8808189212caf8eda89694e49f0ea81219c1aab2f87a48fe3b7af6c64 | 2017-09-12 |
| CurrencyNamesProvider.java | src/java.base/share/classes/sun/util/resources/CurrencyNamesProvider.java | 446d08287a25e9f1560773f3e2619794085778ac3cd5ba6bde97b668e68649b5 | 2017-09-12 |
| LocaleData.java | src/java.base/share/classes/sun/util/resources/LocaleData.java | cf222ce393340172f6a28ad13924b050ffbddf981466294980dd88d1cad86c89 | 2021-06-02 |
| LocaleDataProvider.java | src/java.base/share/classes/sun/util/resources/LocaleDataProvider.java | 8c5be76fac26c62c5613309787c143d2da7eeada846263dff300a4f903fecc01 | 2017-09-12 |
| LocaleNamesBundle.java | src/java.base/share/classes/sun/util/resources/LocaleNamesBundle.java | 8e492e1410afed32235e2fb42d135c8c2776083b5c68a2b6bc72b1d604dc3eac | 2017-09-12 |
| LocaleNamesProvider.java | src/java.base/share/classes/sun/util/resources/LocaleNamesProvider.java | 56852226938037b70bc35f162de3ddc0920f83a0261e2da67ff88a22275543e1 | 2017-09-12 |
| OpenListResourceBundle.java | src/java.base/share/classes/sun/util/resources/OpenListResourceBundle.java | 43bb3c590f94e35c6de0d175da6a9d7994ec2ab74a67320d80a794c7bf5ea710 | 2017-09-12 |
| ParallelListResourceBundle.java | src/java.base/share/classes/sun/util/resources/ParallelListResourceBundle.java | 964353aa79cc0b1cb8abf6b21e940d5ee461cedcd987cf093dd0c1730c47f9a2 | 2017-09-12 |
| TimeZoneNames.java | src/java.base/share/classes/sun/util/resources/TimeZoneNames.java | 33a4ee3c6f2e841941fb8775990355cf1677d8af8c94ec326f3979c195017cc5 | 2023-04-17 |
| TimeZoneNamesBundle.java | src/java.base/share/classes/sun/util/resources/TimeZoneNamesBundle.java | 18c35d9329d8ac9ff575fa55887ae6e1bb380f0d08b1db6d3295bef33c4df880 | 2018-04-26 |
| TimeZoneNamesProvider.java | src/java.base/share/classes/sun/util/resources/TimeZoneNamesProvider.java | 85108c6e52ce5a74c1e4fa69829f03b3d0b1c3e3779ef81bd7fece923424a82b | 2017-09-12 |
| TimeZoneNames_en.java | src/java.base/share/classes/sun/util/resources/TimeZoneNames_en.java | 39e6036fdc56354e824e1d8c92d13d5d2940f40cb36bb601ee14a20665e5b81d | 2017-09-12 |
| CalendarProvider.java | src/java.base/share/classes/sun/util/spi/CalendarProvider.java | 7d27823c2bcaf8ab9962dcf237f6d148a3273d95e776bb0ad7f2988a41e83788 | 2017-09-12 |
| LocaleDataProvider.java | src/jdk.localedata/share/classes/sun/util/resources/provider/LocaleDataProvider.java | bc20e1e12174d313318ee8fb389bec37d71c0a73f2aa08ef8590febb906b40c3 | 2021-05-26 |
| SupplementaryLocaleDataProvider.java | src/jdk.localedata/share/classes/sun/util/resources/provider/SupplementaryLocaleDataProvider.java | e9939a9102b2ff051bfab33f75a5de4e800a10c13f6d60fcfcb40279437f6a23 | 2021-05-26 |
| RuleBasedBreakIterator.java | src/java.base/share/classes/sun/text/RuleBasedBreakIterator.java | 9b01b18dcf9b2e7abc85d69f21a32d1ef05de6d1dc2186fb018926a8290b72af | 2017-09-12 |
| DictionaryBasedBreakIterator.java | src/java.base/share/classes/sun/text/DictionaryBasedBreakIterator.java | d42b71f980a229a95f4199ee3f499851f47b093b8f3ad1beab04cf5b0ed6d29c | 2020-04-15 |
| BreakDictionary.java | src/java.base/share/classes/sun/text/BreakDictionary.java | 261220363f9d74c8995347bc559551c0770bf9fa2d6fa3224a72dd95b91d9610 | 2020-03-28 |
| CompactByteArray.java | src/java.base/share/classes/sun/text/CompactByteArray.java | 2e77196b9fb84de8b28ab68f6da4f4f310f87d63312c08744cfce1bb22dfea91 | 2020-03-28 |
| SupplementaryCharacterData.java | src/java.base/share/classes/sun/text/SupplementaryCharacterData.java | 5d9dfd2ebdf3e550e954c4f7357e418adcf3dc8b0768e5d82d4dcb157fac1f61 | 2017-09-12 |
| ResourceBundleEnumeration.java | src/java.base/share/classes/sun/util/ResourceBundleEnumeration.java | 99d40b095f2141c8e218e689024ed2072efd0455b15730295c2a90953f6cddba | 2017-09-12 |
| CharacterCategory.java | make/jdk/src/classes/build/tools/generatebreakiteratordata/CharacterCategory.java | 54079f8dc6d9a10c29237363385beffd3d11bc9e45da7b572bc9024db1520c4b | 2017-09-12 |
| CharSet.java | make/jdk/src/classes/build/tools/generatebreakiteratordata/CharSet.java | 30c9b1e0ba3b3d5549dd712d498c92548efd20e1a7c632d241a445c14474809e | 2017-09-12 |
| DictionaryBasedBreakIteratorBuilder.java | make/jdk/src/classes/build/tools/generatebreakiteratordata/DictionaryBasedBreakIteratorBuilder.java | 8123f4ba16facebfa6b791e6ae0d54500fab8f30d793b962d223105ac2073846 | 2017-09-12 |
| GenerateBreakIteratorData.java | make/jdk/src/classes/build/tools/generatebreakiteratordata/GenerateBreakIteratorData.java | 21ce626f0ef04732259a4f781153df653e306c1558c3f6c56c21daf29b3f5c71 | 2020-03-23 |
| RuleBasedBreakIteratorBuilder.java | make/jdk/src/classes/build/tools/generatebreakiteratordata/RuleBasedBreakIteratorBuilder.java | 24d01abf0b53b68f42fa580baef4104a9292638e85aaedc80fb63b90691c5043 | 2020-03-23 |
| SupplementaryCharacterData.java (build.tools.generatebreakiteratordata variant) | make/jdk/src/classes/build/tools/generatebreakiteratordata/SupplementaryCharacterData.java | 2cbd0aa6db1c2ba6178792835d74a10b6fbd8bdc2144527570aa31f09293b609 | 2017-09-12 |
| UnicodeData.txt | make/data/unicodedata/UnicodeData.txt | bdbffbbfc8ad4d3a6d01b5891510458f3d36f7170422af4ea2bed3211a73e8bb | 2020-05-13 |
| VERSION (unicodedata) | make/data/unicodedata/VERSION | 50b85a67451145545a65cea370dab8d3444fbfe07e9c34cef560c5b7da9d3eef | 2020-05-13 |

`RuleBasedBreakIterator`/`DictionaryBasedBreakIterator` (plus their support
classes `CompactByteArray`/`SupplementaryCharacterData`, and
`DictionaryBasedBreakIterator`'s own dependency `BreakDictionary`) are the
`BreakIterator` implementations `BreakIteratorProviderImpl` instantiates by
class name (from `BreakIteratorInfo`'s `BreakIteratorClasses` entry).
`sun.security.util.Debug` is a dependency of `GetPropertyAction` (one of its
overloads uses it for a debug-flag check).
`ResourceBundleEnumeration` is a small `Enumeration` helper used by the
`OpenListResourceBundle`/`ParallelListResourceBundle` resource bundle base
classes. `GenerateBreakIteratorData` (plus `CharacterCategory`, `CharSet`,
`RuleBasedBreakIteratorBuilder`, `DictionaryBasedBreakIteratorBuilder`, and
its own `SupplementaryCharacterData` variant under
`build.tools.generatebreakiteratordata`) is the JDK's own build-time tool
that compiles `BreakIteratorRules`/`BreakIteratorInfo` (already listed above)
plus `UnicodeData.txt` (the Unicode Character Database, redistributed by
OpenJDK under the Unicode, Inc. License Agreement — see
`src/java.base/share/legal/unicode.md` in the OpenJDK source tree) into the
binary `*BreakIteratorData` files that `RuleBasedBreakIterator`/
`DictionaryBasedBreakIterator` read at runtime, the same "build generates a
data file" pattern as `tzdb.dat` above.

### Bulk-copied folders

These folders were copied in full, file-for-file, with no changes. Rather than
listing all files individually, each folder is recorded as a single row with a
combined hash: the SHA-256 of the sorted, concatenated per-file SHA-256 hashes
of every `*.java` file directly inside that folder (computed as
`find <folder> -name '*.java' | sort | xargs shasum -a 256 | shasum -a 256`).
"Last Modified" is the most recent commit date across all files in the folder.

| Folder | Path in OpenJDK | File Count | Folder Hash (SHA-256 of sorted per-file SHA-256s) | Last Modified in OpenJDK |
|---|---|---|---|---|
| sun.text.resources.ext (legacy JRE locale text resources: FormatData, CollationData, BreakIteratorInfo/Rules, JavaTimeSupplementary for ~15 non-English locales) | src/jdk.localedata/share/classes/sun/text/resources/ext/ | 260 | a55e322784e6a1f3063e1226c169919c71a95b384559e1980472f95d2f92a8af | 2021-05-26 |
| sun.util.resources.ext (legacy JRE locale util resource *classes*: TimeZoneNames for ~15 non-English locales, plus the two hand-written `CurrencyNames_zh_HK`/`CurrencyNames_zh_SG`/`LocaleNames_zh_HK` `.java` bundles) | src/jdk.localedata/share/classes/sun/util/resources/ext/ (`*.java`) | 18 | a9e149c8fb75383a404921fcf814db77366b519f3a966c02bb8d0fdf8d71946e | 2023-01-09 |
| sun.util.resources.ext (legacy JRE locale util resource *data*: the full `CalendarData_*` (55), `CurrencyNames_*` (115), and `LocaleNames_*` (54) `.properties` files) | src/jdk.localedata/share/classes/sun/util/resources/ext/ (`*.properties`) | 224 | 1583352ff878ea8779e6c6e1bef2fd25bb2d9882b22cef9d6c910d227ba2b310 | 2026-09-01 |
| sun.util.resources (base JRE locale util resource *data*: `CalendarData`, `CalendarData_en`, `CurrencyNames`, `CurrencyNames_en_US`, `LocaleNames`, `LocaleNames_en` `.properties`) | src/java.base/share/classes/sun/util/resources/ (`*.properties`) | 6 | 9b46ff98370a5dcf40aeccfba7eac3627abfa9faaa6c26f184256258bd45ce11 | 2026-09-01 |
| tzdata (raw IANA time-zone database text files consumed by `TzdbZoneRulesCompiler` to build `tzdb.dat`; public-domain IANA data, redistributed by OpenJDK under its own GPLv2+Classpath-exception file headers) | make/data/tzdata/ (africa, antarctica, asia, australasia, backward, etcetera, europe, gmt, jdk11_backward, northamerica, southamerica, VERSION) | 12 | df9e6a727ef0d4d58c38b6a88fa4f9a32d856140e4b0651aeba27df7428d6258 | 2026-05-28 |

All copied files (individually listed + the bulk folders, 614 files total)
were placed verbatim under
`src/main/thirdparty/com/appiancorp/jre17/compact/thirdparty/`, mirroring
their original OpenJDK relative path exactly, with one exception: the four
interim `java.time.zone` classes `TzdbZoneRulesCompiler` needs (`Ser`,
`ZoneOffsetTransition`, `ZoneOffsetTransitionRule`, `ZoneRules`) are placed
under `build/tools/tzdb/` alongside it rather than a `java/time/zone/` path,
matching where OpenJDK's own `make/CopyInterimTZDB.gmk` copies them for its
build -- their content, including the unchanged `package java.time.zone;`
declaration, is still byte-for-byte identical to the OpenJDK source; only
the file's *location* differs, not its content. No package renaming, reformatting,
or code changes were made in this commit; renaming into
`com.appiancorp.jre17.compact.thirdparty` and removing JDK-internal-only
dependencies happens in a subsequent commit.

### Locale `.properties` resource bundles

The `CalendarData_*`, `CurrencyNames_*`, and `LocaleNames_*` bundles above are
copied as `.properties` data files (they carry no `package` declaration, so no
repackaging is applied to their contents). OpenJDK does not ship these as
`.properties` at runtime: its build compiles each one into a `ListResourceBundle`
subclass (see `make/modules/jdk.localedata/Gensrc.gmk` and
`make/modules/java.base/Gensrc.gmk`, `SetupCompileProperties` with
`CLASS := sun.util.resources.LocaleNamesBundle`), because the runtime locale-data
`ResourceBundleProvider` loads bundles by class name (`Class.forName`) rather than
reading `.properties`. This project mirrors that step: the `generateLocaleResourceBundles`
Gradle task (a Kotlin reimplementation of
`make/jdk/src/classes/build/tools/compileproperties/CompileProperties.java`)
translates every copied `.properties` file into a `public final` `ListResourceBundle`
subclass under the repackaged
`com.appiancorp.jre17.compact.thirdparty.sun.util.resources[.ext]` package,
extending the repackaged `sun.util.resources.LocaleNamesBundle`, into a generated
source directory that is compiled into the jar. The metadata generator
(`generateLocaleDataMetaInfo`) scans both `*.java` and `*.properties` files when
computing the per-category `LocaleDataMetaInfo` locale lists, matching
`GensrcLocaleData.gmk`.
