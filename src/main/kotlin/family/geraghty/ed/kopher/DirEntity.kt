package family.geraghty.ed.kopher

/**
 * Taken from https://datatracker.ietf.org/doc/html/rfc1436#section-3.8
 */
enum class ItemType(private val itemType: Char) {
    FILE('0'),
    DIRECTORY('1'),
    CSO_BOOK_SERVER('2'),
    ERROR('3'), // We don't actually use this, just for completeness
    BINHEXED_MACINTOSH_FILE('4'),
    DOS_BINARY_ARCHIVE('5'),
    UNIX_UUENCODED_FILE('6'),
    INDEX_SEARCH_SERVER('7'),
    TEXT_BASED_TELNET_SESSION('8'),
    BINARY_FILE('9'),
    REDUNDANT_SERVER('+'),
    TEXT_BASED_TN3270_SESSION('T'),
    GIF_FILE('g'),
    IMAGE('I');

    override fun toString(): String {
        return itemType.toString()
    }
}

data class DirEntity(
    val itemType: ItemType,
    val userName: String,
    var selectorString: String,
    var host: String,
    var realPath: String?,
    var port: Int = 70
) {

    override fun toString(): String {
        return "$itemType$userName\t$selectorString\t$host\t$port"
    }
}
