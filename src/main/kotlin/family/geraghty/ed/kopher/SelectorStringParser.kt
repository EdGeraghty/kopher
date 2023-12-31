package family.geraghty.ed.kopher

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileReader

class SelectorStringParser(private val baseDirectory: String, private val directoryListingJson: String) {

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

    private fun parseWithThrows(selectorString: String): String {
        if (selectorString.length > 255) {
            throw Exception("The Selector string should be no longer than 255 characters.")
        }

        return if (selector == "\r\n") {
            listWhatYouHave()
        } else {
            val selector = selectorString.substringBefore("\t")
            val dirEntity = deserializeToDirEntities().first { it.selectorString == selector }

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

        FileReader(baseDirectory + dirEntity.realPath!!)
            .forEachLine {
                if (it.startsWith(".")) {
                    returnVal += "."
                }
                returnVal += "${it}\r\n"
            }

        return "$returnVal."
    }

    private fun outputBinaryFile(dirEntity: DirEntity): String {
        return File(baseDirectory + dirEntity.realPath!!)
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
    }

    private fun deserializeToDirEntities(): List<DirEntity> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(directoryListingJson)
    }

    private fun listWhatYouHave(): String {
        var returnVar = ""

        for (dirEntity in deserializeToDirEntities()) {
            returnVar += dirEntity.toString()
            returnVar += "\r\n"
        }

        return "$returnVar."
    }
}
