package family.geraghty.ed.kopher

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SelectorStringParserTest {
    private val directoryListingJson = """
        [
          {
            "item_type": "0",
            "user_name": "About internet Gopher",
            "selector_string": "Stuff:About us",
            "real_path": "Stuff/About us",
            "host": "test.kopher.lol",
            "port": 70
          },
          {
            "item_type": "1",
            "user_name": "Around University of Minnesota",
            "selector_string": "Z,5692,AUM",
            "host": "underdog.micro.umn.edu",
            "port": 70
          },
          {
            "item_type": "1",
            "user_name": "Microcomputer News & Prices",
            "selector_string": "Prices/",
            "host": "pserver.bookstore.umn.edu",
            "port": 70
          },
          {
            "item_type": "1",
            "user_name": "Courses, Schedules, Calendars",
            "selector_string": "",
            "host": "events.ais.umn.edu",
            "port": 9120
          },
          {
            "item_type": "1",
            "user_name": "Student-Staff Directories",
            "selector_string": "",
            "host": "uinfo.ais.umn.edu",
            "port": 70
          },
          {
            "item_type": "1",
            "user_name": "Departmental Publications",
            "selector_string": "Stuff:DP:",
            "real_path": "Stuff/DP/",
            "host": "test.kopher.lol",
            "port": 70
          }
        ]
    """.trimIndent()

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
     *    1Around University of Minnesota Z,5692,AUM  underdog.micro.umn.edu   70
     *    1Microcomputer News & Prices    Prices/    pserver.bookstore.umn.edu  70
     *    1Courses, Schedules, Calendars      events.ais.umn.edu  9120
     *    1Student-Staff Directories      uinfo.ais.umn.edu   70
     *    1Departmental Publications  Stuff:DP:   test.kopher.lol   70
     *    . {Period on a line by itself}
     */
    @Test
    fun `Sends an empty line meaning list what you have`() {
        val parser = SelectorStringParser(directoryListingJson)
        val result = parser.parse("\r\n")

        assertEquals(
            "0About internet Gopher\tStuff:About us\ttest.kopher.lol\t70\r\n" +
            "1Around University of Minnesota\tZ,5692,AUM\tunderdog.micro.umn.edu\t70\r\n" +
            "1Microcomputer News & Prices\tPrices/\tpserver.bookstore.umn.edu\t70\r\n" +
            "1Courses, Schedules, Calendars\t\tevents.ais.umn.edu\t9120\r\n" +
            "1Student-Staff Directories\t\tuinfo.ais.umn.edu\t70\r\n" +
            "1Departmental Publications\tStuff:DP:\ttest.kopher.lol\t70\r\n" +
            ".",
            result
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
        val parser = SelectorStringParser(directoryListingJson)
        val randomStringWhichIsTooLong =
            (1..256).map{
                (0..1).random()
            }
            .joinToString("")

        val result = parser.parse(randomStringWhichIsTooLong)

        assertEquals(
            "3The Selector string should be no longer than 255 characters.\r\n" +
            ".",
            result
        )
    }

    /**
     *
     */
    @Test
    fun `Retrieve the 'About Us' text file`() {
        val parser = SelectorStringParser(directoryListingJson)
        val selectorString = "Stuff:About us"

        val result = parser.parse(selectorString)

        assertEquals(
            """
                WE ARE ANOMALOUS
                WE ARE A REGION
                FORGIVE AND FORGET
                EXPECTO PATRONUM
                .
            """.trimIndent(),
            result
        )
    }
}
