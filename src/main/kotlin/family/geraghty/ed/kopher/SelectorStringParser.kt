package family.geraghty.ed.kopher

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileReader
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.name
import kotlin.io.path.pathString

class SelectorStringParser(dataDirectory: Stream<Path>) {
    private val data = dataDirectory.toList()
    private val dirEntities = deserializeToDirEntities()

    /**
     * Parse the [selectorString] and output the result.
     *
     * If an exception is thrown, output with the correct `ItemType`
     */
    fun parse(selectorString: String): String {
        return try {
            parseWithThrows(selectorString)
        } catch (e: Exception) {
            "${ItemType.ERROR}${e.message}\r\n."
        }
    }

    private fun parseWithThrows(userSelectorString: String): String {
        val selector = userSelectorString.substringBefore("\t")

        if (selector.length > 255) {
            throw Exception("The Selector string should be no longer than 255 characters.")
        }

        return if (selector == "\r\n") {
            listWhatYouHave()
        } else {
            val dirEntity = dirEntities.first { it.selectorString == selector }

            when (dirEntity.itemType) {
                ItemType.FILE -> outputTextFile(dirEntity)
                ItemType.DOS_BINARY_ARCHIVE, ItemType.BINARY_FILE -> outputBinaryFile(dirEntity)
                ItemType.DIRECTORY -> TODO()
                ItemType.BINHEXED_MACINTOSH_FILE -> TODO()
                ItemType.UNIX_UUENCODED_FILE -> TODO()
                ItemType.TEXT_BASED_TELNET_SESSION -> TODO()
                ItemType.TEXT_BASED_TN3270_SESSION -> TODO()
                ItemType.GIF_FILE -> TODO()
                ItemType.IMAGE -> TODO()
                // Don't think I'll ever implement these
                ItemType.CSO_BOOK_SERVER, ItemType.INDEX_SEARCH_SERVER, ItemType.REDUNDANT_SERVER -> TODO()
                // Only here for completeness
                ItemType.ERROR -> TODO()
            }
        }
    }

    /**
     *
     */
    private fun outputTextFile(dirEntity: DirEntity): String {
        if (dirEntity.itemType != ItemType.FILE) { // In theory, we should never get here
            throw Exception("$dirEntity is not a text file")
        }

        var returnVal = ""

        FileReader(getFile(dirEntity))
            .forEachLine {
                if (it.startsWith(".")) {
                    returnVal += "."
                }
                returnVal += "${it}\r\n"
            }

        return "$returnVal."
    }

    private fun getFile(dirEntity: DirEntity): File =
        data.first { it.endsWith(dirEntity.realPath!!) }.toFile()

    private fun outputBinaryFile(dirEntity: DirEntity): String {
        return getFile(dirEntity)
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
    }

    private fun deserializeToDirEntities(): List<DirEntity> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(
            data.first { file ->
                "listing.json" == file.name
            }
                .toFile()
        )
    }

    private fun listWhatYouHave(): String {
        var returnVar = ""

        dirEntities.forEach { dirEntity -> returnVar += "$dirEntity\r\n" }

        return "$returnVar."
    }
}
