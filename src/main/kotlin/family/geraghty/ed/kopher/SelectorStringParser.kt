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

        if (selectorString == "\r\n") {
            return listWhatYouHave()
        }

        val selector = selectorString.substringBefore("\t")

        val dirEntity = deserializeToDirEntities().first { it.selector_string == selector }

        if (dirEntity.real_path.isNullOrEmpty()) {
            throw Exception("Server cannot find the specified file")
        }

        when (dirEntity.item_type){
             '0' -> outputTextFile(dirEntity)
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

        returnVar += "."

        return returnVar
    }
}