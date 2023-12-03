package family.geraghty.ed.kopher

class SelectorStringParser (private var baseDir: String) {
    fun parse(selectorString: String) : String {
        return try {
            parseWithThrows(selectorString)
        } catch (e: Exception) {
            "3$e.message\r\n" +
            "."
        }
    }

    private fun parseWithThrows(selectorString: String) : String {
        if (selectorString.length > 255) {
            throw Exception("The Selector string should be no longer than 255 characters.")
        }

        throw Exception("To be implemented")
    }
}