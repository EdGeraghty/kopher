package family.geraghty.ed.kopher

import java.io.File
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path

class ClientHandler(private val client: Socket, private val baseDirectory: String) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val dataDir = Files.walk(Path(baseDirectory))

    fun run() {
        client.use { client ->
            val parser = SelectorStringParser(dataDir)
            write(parser.parse(reader.nextLine()))
            client.close()
        }
    }

    private fun write(message: String) {
        writer.write((message).toByteArray(Charset.defaultCharset()))
    }
}
