package family.geraghty.ed.kopher

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileReader

class SelectorStringParser (private val baseDirectory: String, private val directoryListingJson: String) {
    fun parse(selectorString: String) : String {
        return try {
            parseWithThrows(selectorString)
        } catch (e: Exception) {
            "3${e.message}\r\n" +
            "."
        }
    }

    private fun parseWithThrows(selectorString: String) : String {
        if (selectorString.length > 255) {
            throw Exception("The Selector string should be no longer than 255 characters.")
        }

        var output = ""

        if (selectorString == "\r\n") {
            output = listWhatYouHave()
        }
        else {
            val selector = selectorString.substringBefore("\t")
            val dirEntity = deserializeToDirEntities().first { it.selector_string == selector }

            when (dirEntity.item_type) {
                '0' -> output = outputTextFile(dirEntity)
            }
        }

        if (output.isNotEmpty()) {
            return "$output\r\n" +
                   "."
        }

        throw Exception("To be implemented")
    }

    private fun outputTextFile(dirEntity: DirEntity): String {
        return FileReader(dirEntity.real_path!!).readText()
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

        return returnVar.removeSuffix("\r\n") //That's pretty hacky.
    }
}