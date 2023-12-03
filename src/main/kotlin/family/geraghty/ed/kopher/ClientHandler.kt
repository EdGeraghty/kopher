package family.geraghty.ed.kopher

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(private val client: Socket, private val baseDir: String) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()

    fun run() {
        client.use { client ->
            val parser = SelectorStringParser(baseDir)
            write(parser.parse(reader.nextLine()))
            client.close()
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}
