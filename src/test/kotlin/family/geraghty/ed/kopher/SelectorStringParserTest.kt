package family.geraghty.ed.kopher

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SelectorStringParserTest {

    /**
     * Test adapted from https://datatracker.ietf.org/doc/html/rfc1436#section-2
     *
     *    Below is a simple example of a client/server interaction; more
     *    complex interactions are dealt with later.
     *
     *    [...]
     *    Client: <CR><LF> {Sends an empty line: Meaning "list what you have"}
     *
     *    Server: {Sends a series of lines, each ending with CR LF}
     *    0About internet Gopher  Stuff:About us  rawBits.micro.umn.edu   70
     *    1Around University of Minnesota Z,5692,AUM  underdog.micro.umn.edu   70
     *    1Microcomputer News & Prices    Prices/    pserver.bookstore.umn.edu  70
     *    1Courses, Schedules, Calendars      events.ais.umn.edu  9120
     *    1Student-Staff Directories      uinfo.ais.umn.edu   70
     *    1Departmental Publications  Stuff:DP:   rawBits.micro.umn.edu   70
     *    . {Period on a line by itself}
     */
    @Test
    fun `Sends an empty line meaning list what you have`() {
        val parser = SelectorStringParser("src/test/resources")
        val result = parser.parse("\r\n")

        assertEquals(
            "0About internet Gopher Stuff:About us  rawBits.micro.umn.edu   70\r\n" +
            "1Around University of Minnesota    Z,5692,AUM underdog.micro.umn.edu  70\r\n" +
            "1Microcomputer News & Prices   Prices/ pserver.bookstore.umn.edu   70\r\n" +
            "1Courses, Schedules, Calendars     events.ais.umn.edu 9120\r\n" +
            "1Student-Staff Directories     uinfo.ais.umn.edu  70\r\n" +
            "1Departmental Publications Stuff:DP:   rawBits.micro.umn.edu   70\r\n" +
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
        val parser = SelectorStringParser("src/test/resources")
        val randomStringWhichIsTooLong =
            (1..256).map{
                (0..1).random()
            }
            .joinToString("")

        val result = parser.parse(randomStringWhichIsTooLong)

        assertEquals(
            result,
            "3The Selector string should be no longer than 255 characters.\r\n" +
            "."
        )
    }
}
