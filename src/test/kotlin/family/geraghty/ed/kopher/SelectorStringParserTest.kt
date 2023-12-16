package family.geraghty.ed.kopher

import org.junit.jupiter.api.Test
import java.io.FileReader

class SelectorStringParserTest {
    private val directoryListingJson = """
        [
          {
            "itemType": "0",
            "userName": "About internet Gopher",
            "selectorString": "Stuff:About us",
            "realPath": "Stuff/About us",
            "host": "test.kopher.lol",
            "port": 70
          },
          {
            "itemType": "0",
            "userName": "Dot Test",
            "selectorString": "Stuff:Dot Test",
            "realPath": "Stuff/Dot Test",
            "host": "test.kopher.lol",
            "port": 70
          },
          {
            "itemType": "9",
            "userName": "DL my random binary file!",
            "selectorString":"Stuff/random bin",
            "realPath": "Stuff/output-onlinefiletools.bin",
            "host": "test.kopher.lol",
            "port": 70
          },
          {
            "itemType": "1",
            "userName": "Around University of Minnesota",
            "selectorString": "Z,5692,AUM",
            "host": "underdog.micro.umn.edu",
            "port": 70
          },
          {
            "itemType": "1",
            "userName": "Microcomputer News & Prices",
            "selectorString": "Prices/",
            "host": "pserver.bookstore.umn.edu",
            "port": 70
          },
          {
            "itemType": "1",
            "userName": "Courses, Schedules, Calendars",
            "selectorString": "",
            "host": "events.ais.umn.edu",
            "port": 9120
          },
          {
            "itemType": "1",
            "userName": "Student-Staff Directories",
            "selectorString": "",
            "host": "uinfo.ais.umn.edu",
            "port": 70
          },
          {
            "itemType": "1",
            "userName": "Departmental Publications",
            "selectorString": "Stuff:DP:",
            "realPath": "Stuff/DP/",
            "host": "test.kopher.lol",
            "port": 70
          }
        ]
    """
    private var baseDir = "src/test/resources/"
    private val parser = SelectorStringParser(baseDir, directoryListingJson)

    /**
     * Overridden assertEquals which takes any [expected] String, trims indents, and enforces `\r\n` line breaks. It
     * then compares against [actual] using `kotlin.test.assertEquals`, with an optional [message].
     */
    private fun assertEquals(
        expected: String,
        actual: Any,
        message: String? = null,
    ) {
        return kotlin.test.assertEquals(
            expected
                .trimIndent()
                .replace(
                    Regex("\\r\\n|\\r|\\n"),
                    "\r\n",
                ),
            actual,
            message,
        )
    }

    /**
     * Test adapted from https://datatracker.ietf.org/doc/html/rfc1436#section-2
     *
     *    Below is a simple example of a client/server interaction; more
     *    complex interactions are dealt with later.
     *
     *    [...]
     *
     *    For this test, our server is assumed to be listening at `test.kopher.lol:70`
     *
     *    Client: <CR><LF> {Sends an empty line: Meaning "list what you have"}
     *
     *    Server: {Sends a series of lines, each ending with CR LF}
     *    0About internet Gopher  Stuff:About us  test.kopher.lol   70
     *                      {.....etc.....}
     *    . {Period on a line by itself}
     */
    @Test
    fun `Sends an empty line meaning list what you have`() {
        val actual = parser.parse("\r\n")
        val expected = //Note we're using an escaped string here, as we need to test for tabs
            "0About internet Gopher\tStuff:About us\ttest.kopher.lol\t70\r\n" +
            "0Dot Test\tStuff:Dot Test\ttest.kopher.lol\t70\r\n" +
            "9DL my random binary file!\tStuff/random bin\ttest.kopher.lol\t70\r\n" +
            "1Around University of Minnesota\tZ,5692,AUM\tunderdog.micro.umn.edu\t70\r\n" +
            "1Microcomputer News & Prices\tPrices/\tpserver.bookstore.umn.edu\t70\r\n" +
            "1Courses, Schedules, Calendars\t\tevents.ais.umn.edu\t9120\r\n" +
            "1Student-Staff Directories\t\tuinfo.ais.umn.edu\t70\r\n" +
            "1Departmental Publications\tStuff:DP:\ttest.kopher.lol\t70\r\n" +
            "."

        assertEquals(
            expected,
            actual,
        )
    }

    /**
     * Test adapted from https://datatracker.ietf.org/doc/html/rfc1436 Appendix Notes, the interaction from https://datatracker.ietf.org/doc/html/rfc1436#section-2
     *
     *    The Selector string should be no longer than 255 characters.
     *
     *    [...]
     *
     *    3 indicates an error
     */
    @Test
    fun `The Selector string should be no longer than 255 characters`() {
        val randomStringWhichIsTooLong =
            (1..256).map{
                (0..1).random()
            }
            .joinToString("")
        val actual = parser.parse(randomStringWhichIsTooLong)
        val expected =
            """
                3The Selector string should be no longer than 255 characters.
                .
            """

        assertEquals(
            expected,
            actual,
        )
    }

    /**
     * Retrieve and check the content of the `About us` by passing its Selector: `Stuff:About us`
     */
    @Test
    fun `Retrieve the 'About Us' text file`() {
        val selectorString = "Stuff:About us"
        val actual = parser.parse(selectorString)
        val expected =
            """
                WE ARE ANOMALOUS
                WE ARE A REGION
                FORGIVE AND FORGET
                EXPECTO PATRONUM
                .
            """

        assertEquals(
            expected,
            actual,
        )
    }

    /**
     * Lines beginning with periods must be prepended with an extra period to ensure that the transmission is not
     * terminated early.
     */
    @Test
    fun `Lines beginning with periods must be prepended with an extra period`() {
        val selectorString = "Stuff:Dot Test"
        val actual = parser.parse(selectorString)
        val expected =
            """
                ....Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn
                .
            """

        assertEquals(
            expected,
            actual,
        )
    }

    /**
     * Binary file Transaction (Type 9 or 5 item)
     *
     * C: Sends Selector String.
     * S: Sends a binary file and closes connection when done.
     *
     * Random binary generated on https://onlinefiletools.com/generate-random-binary-file
     */
    @Test
    fun `Binary file Transaction (Type 9 or 5 item) sends a binary file`() {
        val selectorString = "Stuff/random bin"
        val actual = parser.parse(selectorString)
        val expected = FileReader("src/test/resources/Stuff/output-onlinefiletools.bin").readText()

        assertEquals(
            expected,
            actual,
        )
    }
}
