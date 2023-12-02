package family.geraghty.ed.kopher

class SelectorStringParser (var baseDir: String) {
    fun parse(selectorString: String) : String {
        return try {
            parseWithThrows(selectorString)
        } catch (e: Exception) {
            "3" + e.message + "\r\n" +
            "."
        }
    }

    private fun parseWithThrows(selectorString: String) : String {
        return "TODO, lol"
    }
}