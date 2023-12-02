package family.geraghty.ed.kopher

/**
 * Taken from https://datatracker.ietf.org/doc/html/rfc1436#section-3.8
 */
enum class ItemType(val itemChar: Char) {
    FILE('0'),
    DIRECTORY('1'),
    CSO_PHONEBOOK_SERVER('2'),
    ERROR('3'),
    BINHEXED_MACINTOSH_FILE('4'),
    DOS_BINARY_ARCHIVE('5'),
    UNIX_UUENCODED_FILE('6'),
    INDEX_SEARCH_SERVER('7'),
    TEXT_BASED_TELNET_SESSION('8'),
    BINARY_FILE('9'),
    REDUNDANT_SERVER('+'),
    TEXT_BASED_TN3270_SESSION('T'),
    GIF_FORMAT_GRAPHICS_FILE('g'),
    SOME_KIND_OF_IMAGE('I'),
}