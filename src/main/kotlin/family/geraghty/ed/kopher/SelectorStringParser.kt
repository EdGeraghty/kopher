package family.geraghty.ed.kopher

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class SelectorStringParser (private val directoryListingJson: String) {
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
        throw Exception("To be implemented")
    }

    private fun listWhatYouHave(): String {
        val mapper = jacksonObjectMapper()
        val dirEntitiesList: List<DirEntity> = mapper.readValue(directoryListingJson)

        var returnVar = ""

        for (dirEntity in dirEntitiesList) {
            returnVar += dirEntity.toString()
            returnVar += "\r\n"
        }

        returnVar += "."

        return returnVar
    }
}