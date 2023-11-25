package family.geraghty.ed.kopher

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(private val client: Socket) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()

    fun run() {
        client.use { client ->
            val selectorString = reader.nextLine()

            // The Selector string should be no longer than 255 characters.
            if (selectorString.length > 255) {
                client.close()
            }

            val dirEntity = selectorString.split('\t')
            write("Hello") // TODO
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}
