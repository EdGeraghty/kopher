package family.geraghty.ed.kopher

import java.io.File
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(private val client: Socket, private val baseDirectory: String) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()

    private val directoryListingJson = File("$baseDirectory/DirectoryListing.json").readText()

    fun run() {
        client.use { client ->
            val parser = SelectorStringParser(baseDirectory, directoryListingJson)
            write(parser.parse(reader.nextLine()))
            client.close()
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}
