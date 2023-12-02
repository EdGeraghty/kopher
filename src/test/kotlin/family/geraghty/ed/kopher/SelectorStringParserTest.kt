package family.geraghty.ed.kopher

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Random

class SelectorStringParserTest {

    /**
     * Test adapted from https://datatracker.ietf.org/doc/html/rfc1436#section-2
     *
     *    Below is a simple example of a client/server interaction; more
     *    complex interactions are dealt with later.  Assume that a "well-
     *    known" Gopher server (this may be duplicated, details are discussed
     *    later) listens at a well known port for the campus (much like a
     *    domain-name server).
     *    [...]
     *    In the example below the F character denotes the TAB character.
     *
     *  Client:          {Opens connection to Server}
     *
     *  Server:          {Accepts connection but says nothing}
     *
     *  Client: <CR><LF> {Sends an empty line: Meaning "list what you have"}
     *
     *  Server:          {Sends a series of lines, each ending with CR LF}
     *  0About internet GopherFStuff:About usFrawBits.micro.umn.eduF70
     *  1Around University of MinnesotaFZ,5692,AUMFunderdog.micro.umn.eduF70
     *  1Microcomputer News & PricesFPrices/Fpserver.bookstore.umn.eduF70
     *  1Courses, Schedules, CalendarsFFevents.ais.umn.eduF9120
     *  1Student-Staff DirectoriesFFuinfo.ais.umn.eduF70
     *  1Departmental PublicationsFStuff:DP:FrawBits.micro.umn.eduF70
     *                     {.....etc.....}
     *  .                  {Period on a line by itself}
     *                     {Server closes connection}
     */
    @Test
    fun `Sends an empty line meaning list what you have`() {
        val parser = SelectorStringParser(baseDir="resources/files")
        val result = parser.parse("\r\n")

        assertEquals(
                "0About internet GopherFStuff:About usFrawBits.micro.umn.eduF70\r\n" +
                "1Around University of MinnesotaFZ,5692,AUMFunderdog.micro.umn.eduF70\r\n" +
                "1Microcomputer News & PricesFPrices/Fpserver.bookstore.umn.eduF70\r\n" +
                "1Courses, Schedules, CalendarsFFevents.ais.umn.eduF9120\r\n" +
                "1Student-Staff DirectoriesFFuinfo.ais.umn.eduF70\r\n" +
                "1Departmental PublicationsFStuff:DP:FrawBits.micro.umn.eduF70\r\n" +
                ".",
            result)
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
        val parser = SelectorStringParser(baseDir="resources/files")
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
