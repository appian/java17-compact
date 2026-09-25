import java.util.Properties

plugins {
    `java-library`
    `maven-publish`
}

group = "com.appiancorp.jre17.compact"
description = "Library which contains Utilities for JRE17 compatibility"
version = providers.gradleProperty("publishVersion").getOrElse("1.0.0")

repositories {
    mavenCentral()
}

val thirdpartyDir = layout.projectDirectory.dir("src/main/thirdparty/com/appiancorp/jre17/compact/thirdparty")
val localeDataMetaInfoJavaDir = layout.buildDirectory.dir("generated/sources/localeDataMetaInfo/java").get().asFile
val localeResourceBundlesJavaDir = layout.buildDirectory.dir("generated/sources/localeResourceBundles/java").get().asFile
val localeDataMetaInfoResourcesDir = layout.buildDirectory.dir("generated/resources/localeDataMetaInfo").get().asFile
val tzdbResourcesDir = layout.buildDirectory.dir("generated/resources/tzdb").get().asFile
val breakIteratorDataResourcesDir = layout.buildDirectory.dir("generated/resources/breakIteratorData").get().asFile

val templateFile = thirdpartyDir.file("sun/util/locale/provider/LocaleDataMetaInfo-XLocales.java.template").asFile
val textResourcesDir = thirdpartyDir.dir("sun/text/resources").asFile
val textResourcesExtDir = thirdpartyDir.dir("sun/text/resources/ext").asFile
val utilResourcesDir = thirdpartyDir.dir("sun/util/resources").asFile
val utilResourcesExtDir = thirdpartyDir.dir("sun/util/resources/ext").asFile
val tzdataDir = thirdpartyDir.dir("tzdata").asFile
val unicodeDataFile = thirdpartyDir.file("unicodedata/UnicodeData.txt").asFile

sourceSets {
    main {
        java.srcDir("src/main/thirdparty")
        java.srcDir(localeDataMetaInfoJavaDir)
        java.srcDir(localeResourceBundlesJavaDir)
        resources.srcDir(localeDataMetaInfoResourcesDir)
        resources.srcDir(tzdbResourcesDir)
        resources.srcDir(breakIteratorDataResourcesDir)
    }
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// Mirrors make/modules/java.base/gensrc/GensrcLocaleData.gmk: scans for
// resource-bundle filenames, substitutes them into the OpenJDK
// LocaleDataMetaInfo-XLocales.java.template to produce Base/NonBase
// LocaleDataMetaInfo.java, and registers them + LocaleData's resource bundle
// providers as META-INF/services entries.
// Aliases avoid shadowing: inside the block below, `templateFile` etc. would
// otherwise resolve to the task's own property of the same name.
val genTemplateFile = templateFile
val genTextResourcesDir = textResourcesDir
val genTextResourcesExtDir = textResourcesExtDir
val genUtilResourcesDir = utilResourcesDir
val genUtilResourcesExtDir = utilResourcesExtDir
val generateLocaleDataMetaInfo = tasks.register<GenerateLocaleDataMetaInfoTask>("generateLocaleDataMetaInfo") {
    templateFile.set(genTemplateFile)
    textResourcesDir.set(genTextResourcesDir)
    textResourcesExtDir.set(genTextResourcesExtDir)
    utilResourcesDir.set(genUtilResourcesDir)
    utilResourcesExtDir.set(genUtilResourcesExtDir)
    javaOutDir.set(localeDataMetaInfoJavaDir)
    resourcesOutDir.set(localeDataMetaInfoResourcesDir)
}

// Mirrors make/modules/jdk.localedata/Gensrc.gmk + make/modules/java.base/Gensrc.gmk
// (SetupCompileProperties with CLASS := sun.util.resources.LocaleNamesBundle):
// the OpenJDK build compiles every sun/util/resources[/ext] *.properties file
// (CurrencyNames, LocaleNames, CalendarData) into a ListResourceBundle subclass
// so the LocaleData ResourceBundle providers -- which load bundles by class name
// (Class.forName) rather than reading .properties at runtime -- can find them.
val generateLocaleResourceBundles = tasks.register<GenerateLocaleResourceBundlesTask>("generateLocaleResourceBundles") {
    baseDir.set(genUtilResourcesDir)
    extDir.set(genUtilResourcesExtDir)
    outDir.set(localeResourceBundlesJavaDir)
}

// Mirrors make/modules/java.base/gendata/GendataTZDB.gmk: runs
// TzdbZoneRulesCompiler against the bundled IANA tzdata text files to
// produce tzdb.dat, placed where ZoneInfoFile.class.getResourceAsStream
// ("tzdb.dat") will find it.
val tzdbDatOutDir = File(tzdbResourcesDir, "com/appiancorp/jre17/compact/thirdparty/sun/util/calendar")
val generateTzdbDat = tasks.register<JavaExec>("generateTzdbDat") {
    dependsOn(tasks.named("compileJava"))
    inputs.dir(tzdataDir)
    outputs.dir(tzdbDatOutDir)
    classpath = sourceSets.main.get().output.classesDirs
    mainClass.set("com.appiancorp.jre17.compact.thirdparty.build.tools.tzdb.TzdbZoneRulesCompiler")
    args = listOf("-srcdir", tzdataDir.absolutePath, "-dstfile", File(tzdbDatOutDir, "tzdb.dat").absolutePath)
}

// Mirrors make/modules/java.base/gendata/GendataBreakIterator.gmk: runs
// GenerateBreakIteratorData against UnicodeData.txt to produce the binary
// *BreakIteratorData files RuleBasedBreakIterator/DictionaryBasedBreakIterator
// read at runtime -- once for the base locale-independent data, once for
// Thai (the only locale needing its own dictionary-based break data).
val breakIteratorBaseOutDir = File(breakIteratorDataResourcesDir, "com/appiancorp/jre17/compact/thirdparty/sun/text/resources")
val generateBreakIteratorDataBase = tasks.register<JavaExec>("generateBreakIteratorDataBase") {
    dependsOn(tasks.named("compileJava"))
    inputs.file(unicodeDataFile)
    outputs.dir(breakIteratorBaseOutDir)
    classpath = sourceSets.main.get().output.classesDirs
    mainClass.set("com.appiancorp.jre17.compact.thirdparty.build.tools.generatebreakiteratordata.GenerateBreakIteratorData")
    args = listOf("-o", breakIteratorBaseOutDir.absolutePath, "-spec", unicodeDataFile.absolutePath)
}

val breakIteratorThOutDir = File(breakIteratorDataResourcesDir, "com/appiancorp/jre17/compact/thirdparty/sun/text/resources/ext")
val generateBreakIteratorDataTh = tasks.register<JavaExec>("generateBreakIteratorDataTh") {
    dependsOn(tasks.named("compileJava"))
    inputs.file(unicodeDataFile)
    outputs.dir(breakIteratorThOutDir)
    classpath = sourceSets.main.get().output.classesDirs
    mainClass.set("com.appiancorp.jre17.compact.thirdparty.build.tools.generatebreakiteratordata.GenerateBreakIteratorData")
    args = listOf("-o", breakIteratorThOutDir.absolutePath, "-spec", unicodeDataFile.absolutePath, "-language", "th")
}

tasks.named("compileJava") {
    dependsOn(generateLocaleDataMetaInfo)
    dependsOn(generateLocaleResourceBundles)
}

tasks.named("processResources") {
    dependsOn(generateLocaleDataMetaInfo, generateTzdbDat, generateBreakIteratorDataBase, generateBreakIteratorDataTh)
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("com.appiancorp.jre17.compact")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "locale-provider"
        }
    }
    repositories {
        maven {
            url = uri(
                providers.gradleProperty("mavenRepoUrl")
                    .orElse(providers.environmentVariable("MAVEN_REPO_URL"))
                    .getOrElse(layout.buildDirectory.dir("unset-maven-repo-url").get().asFile.toURI().toString())
            )
            credentials {
                username = providers.gradleProperty("mavenRepoUsername")
                    .orElse(providers.environmentVariable("MAVEN_REPO_USERNAME"))
                    .orNull
                password = providers.gradleProperty("mavenRepoPassword")
                    .orElse(providers.environmentVariable("MAVEN_REPO_PASSWORD"))
                    .orNull
            }
        }
    }
}

tasks.named<Test>("test") {
    dependsOn(generateTzdbDat, generateBreakIteratorDataBase, generateBreakIteratorDataTh)
    useJUnitPlatform()
}

abstract class GenerateLocaleDataMetaInfoTask : DefaultTask() {
    @get:InputFile
    abstract val templateFile: RegularFileProperty

    @get:InputDirectory
    abstract val textResourcesDir: DirectoryProperty

    @get:InputDirectory
    abstract val textResourcesExtDir: DirectoryProperty

    @get:InputDirectory
    abstract val utilResourcesDir: DirectoryProperty

    @get:InputDirectory
    abstract val utilResourcesExtDir: DirectoryProperty

    @get:OutputDirectory
    abstract val javaOutDir: DirectoryProperty

    @get:OutputDirectory
    abstract val resourcesOutDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val categories = listOf(
            "FormatData", "CollationData", "BreakIteratorInfo", "BreakIteratorRules",
            "TimeZoneNames", "LocaleNames", "CurrencyNames", "CalendarData"
        )
        val baseLocales = setOf("en", "en-US")

        fun scanLocales(dir: File, category: String): Set<String> {
            val prefix = "${category}_"
            if (!dir.exists()) return emptySet()
            // Mirrors GensrcLocaleData.gmk, which scans both *.java and
            // *.properties resource-bundle files for each category.
            return dir.listFiles { f -> f.isFile && f.name.startsWith(prefix) &&
                    (f.name.endsWith(".java") || f.name.endsWith(".properties")) }
                ?.map { it.name.removePrefix(prefix).removeSuffix(".java").removeSuffix(".properties").replace('_', '-') }
                ?.toSet() ?: emptySet()
        }

        val textDir = textResourcesDir.get().asFile
        val textExtDir = textResourcesExtDir.get().asFile
        val utilDir = utilResourcesDir.get().asFile
        val utilExtDir = utilResourcesExtDir.get().asFile

        val allBaseLocales = sortedSetOf<String>()
        val allNonBaseLocales = sortedSetOf<String>()
        val perCategoryBase = mutableMapOf<String, Set<String>>()
        val perCategoryNonBase = mutableMapOf<String, Set<String>>()

        for (category in categories) {
            val baseFound = scanLocales(textDir, category) + scanLocales(utilDir, category)
            val extFound = scanLocales(textExtDir, category) + scanLocales(utilExtDir, category)
            val allFound = baseFound + extFound

            val base = allFound.filter { it in baseLocales }.toSet()
            val nonBase = allFound.filterNot { it in baseLocales }.toMutableSet()

            // Special handling for Chinese locales to include implicit scripts
            if ("zh-CN" in nonBase) nonBase.add("zh-Hans-CN")
            if ("zh-SG" in nonBase) nonBase.add("zh-Hans-SG")
            if ("zh-HK" in nonBase) nonBase.add("zh-Hant-HK")
            if ("zh-MO" in nonBase) nonBase.add("zh-Hant-MO")
            if ("zh-TW" in nonBase) nonBase.add("zh-Hant-TW")

            // Adding implicit locales nb, nn-NO, nb-NO
            nonBase.add("nb")
            nonBase.add("nn-NO")
            nonBase.add("nb-NO")

            perCategoryBase[category] = base
            perCategoryNonBase[category] = nonBase
            allBaseLocales.addAll(base)
            allNonBaseLocales.addAll(nonBase)
        }

        // Mirrors GensrcLocaleData.gmk `ALL_NON_BASE_LOCALES := ja-JP-JP th-TH-TH`:
        // these two special variant locales (Japanese imperial calendar and Thai
        // Buddhist/Thai-digits) are always advertised in the NonBase AvailableLocales
        // list even though they have no dedicated resource-bundle files.
        allNonBaseLocales.add("ja-JP-JP")
        allNonBaseLocales.add("th-TH-TH")

        val templateText = templateFile.get().asFile.readText()

        fun substitute(lang: String, pkg: String, perCategory: Map<String, Set<String>>, allLocales: Set<String>): String {
            var text = templateText.replace("#warn This file is preprocessed before being compiled",
                "// -- This file was mechanically generated: Do not edit! -- //")
            text = text.replace("#Lang#", lang)
            text = text.replace("#Package#", pkg)
            for (category in categories) {
                val locales = perCategory[category].orEmpty()
                val value = if (locales.isEmpty()) "" else " " + locales.sorted().joinToString(" ")
                text = text.replace("#${category}_Locales#", value)
            }
            val availableValue = if (allLocales.isEmpty()) "" else allLocales.sorted().joinToString(" ")
            text = text.replace("#AvailableLocales_Locales#", availableValue)
            return text
        }

        val baseText = substitute("Base", "com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider", perCategoryBase, allBaseLocales)
        val nonBaseText = substitute("NonBase", "com.appiancorp.jre17.compact.thirdparty.sun.util.resources.provider", perCategoryNonBase, allNonBaseLocales)

        val javaOut = javaOutDir.get().asFile
        val resourcesOut = resourcesOutDir.get().asFile

        val baseOutDir = File(javaOut, "com/appiancorp/jre17/compact/thirdparty/sun/util/locale/provider")
        baseOutDir.mkdirs()
        File(baseOutDir, "BaseLocaleDataMetaInfo.java").writeText(baseText)

        val nonBaseOutDir = File(javaOut, "com/appiancorp/jre17/compact/thirdparty/sun/util/resources/provider")
        nonBaseOutDir.mkdirs()
        File(nonBaseOutDir, "NonBaseLocaleDataMetaInfo.java").writeText(nonBaseText)

        val servicesDir = File(resourcesOut, "META-INF/services")
        servicesDir.mkdirs()
        File(servicesDir, "com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleDataMetaInfo")
            .writeText("com.appiancorp.jre17.compact.thirdparty.sun.util.resources.provider.NonBaseLocaleDataMetaInfo\n")
        File(servicesDir, "com.appiancorp.jre17.compact.thirdparty.sun.util.resources.LocaleData\$CommonResourceBundleProvider")
            .writeText("com.appiancorp.jre17.compact.thirdparty.sun.util.resources.provider.LocaleDataProvider\n")
        File(servicesDir, "com.appiancorp.jre17.compact.thirdparty.sun.util.resources.LocaleData\$SupplementaryResourceBundleProvider")
            .writeText("com.appiancorp.jre17.compact.thirdparty.sun.util.resources.provider.SupplementaryLocaleDataProvider\n")
    }
}


// Replicates make/jdk/src/classes/build/tools/compileproperties/CompileProperties:
// translates a .properties resource bundle into a ListResourceBundle subclass,
// extending the repackaged sun.util.resources.LocaleNamesBundle, with keys sorted
// and values escaped to ASCII Java string literals. Emits into a generated source
// dir that is added to the main Java source set.
abstract class GenerateLocaleResourceBundlesTask : DefaultTask() {
    @get:InputDirectory
    abstract val baseDir: DirectoryProperty

    @get:InputDirectory
    abstract val extDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    private val pkgRoot = "com.appiancorp.jre17.compact.thirdparty.sun.util.resources"
    private val superClass = "com.appiancorp.jre17.compact.thirdparty.sun.util.resources.LocaleNamesBundle"

    @TaskAction
    fun generate() {
        val out = outDir.get().asFile
        out.deleteRecursively()
        out.mkdirs()
        var count = 0
        count += gen(baseDir.get().asFile, pkgRoot, out)
        count += gen(extDir.get().asFile, "$pkgRoot.ext", out)
        logger.lifecycle("Generated $count locale resource bundle classes from .properties")
    }

    private fun gen(dir: File, pkg: String, out: File): Int {
        if (!dir.exists()) return 0
        // Skip any .properties whose ListResourceBundle .java is already checked in
        // (e.g. CurrencyNames_zh_HK), mirroring the makefile's filter-out of _zh_HK.
        val existingJava = dir.listFiles { f -> f.name.endsWith(".java") }
            ?.map { it.name.removeSuffix(".java") }?.toSet() ?: emptySet()
        val pkgDir = File(out, pkg.replace('.', '/'))
        pkgDir.mkdirs()
        var n = 0
        dir.listFiles { f -> f.isFile && f.name.endsWith(".properties") }
            ?.sortedBy { it.name }
            ?.forEach { pf ->
                val className = pf.name.removeSuffix(".properties")
                if (className in existingJava) return@forEach
                val props = Properties()
                pf.inputStream().use { props.load(it) }
                val keys = props.stringPropertyNames().sorted()
                val sb = StringBuilder()
                sb.append("package ").append(pkg).append(";\n\n")
                sb.append("public final class ").append(className)
                    .append(" extends ").append(superClass).append(" {\n")
                sb.append("    protected final Object[][] getContents() {\n")
                sb.append("        return new Object[][] {\n")
                for (k in keys) {
                    sb.append("            { \"").append(escape(k)).append("\", \"")
                        .append(escape(props.getProperty(k))).append("\" },\n")
                }
                sb.append("        };\n    }\n}\n")
                File(pkgDir, "$className.java").writeText(sb.toString(), Charsets.ISO_8859_1)
                n++
            }
        return n
    }

    // Mirrors CompileProperties.escape: escape for a Java string literal and
    // force non-ASCII characters to \\uXXXX so the source is pure ASCII.
    private fun escape(s: String): String {
        val out = StringBuilder(s.length * 2)
        for (c in s) {
            when (c) {
                '\\' -> out.append("\\\\")
                '\t' -> out.append("\\t")
                '\n' -> out.append("\\n")
                '\r' -> out.append("\\r")
                '\u000C' -> out.append("\\f")
                '"' -> out.append("\\\"")
                else -> if (c.code < 0x0020 || c.code > 0x007e) {
                    out.append("\\u")
                    out.append("0123456789ABCDEF"[(c.code shr 12) and 0xF])
                    out.append("0123456789ABCDEF"[(c.code shr 8) and 0xF])
                    out.append("0123456789ABCDEF"[(c.code shr 4) and 0xF])
                    out.append("0123456789ABCDEF"[c.code and 0xF])
                } else {
                    out.append(c)
                }
            }
        }
        return out.toString()
    }
}
