import java.io.File
import java.util.regex.Pattern

val path = "../widgets/src/main/res/drawable"

fun replace(pattern: Pattern, input: String, prefix: String = " "): String {
    var res = input
    var matcher = pattern.matcher(res)
    while (matcher.find()) {
        val match = matcher.group()
        val offset = match.length - 2
        matcher.start()
        println("was : $res")
        res = res.replaceRange(matcher.start() + offset, matcher.end(), "${prefix}0${match.substring(offset, match.length)}")
        println("now : $res\n")
        matcher = pattern.matcher(res)
    }
    return res
}

fun File.children(): Sequence<File> {
    return walkTopDown()
        .filter { this.path != it.path }
}

val pattern = Pattern.compile("[^\\d]\\.\\d")
val pattern1 = Pattern.compile("\\.\\d*\\.\\d")

File(path).children()
    .filter { it.isFile }
    .forEach { file ->
        println(file)
        try {
            val lines = file.readLines()
                .map { line ->
                    var res = line
                    res = replace(pattern, res, "")
                    res = replace(pattern1, res, " ")
                    res
                }

            file.writeText(
                lines.joinToString("\n")
            )
        } catch (e: Exception) {
        }
    }