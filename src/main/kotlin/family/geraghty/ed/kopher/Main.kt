package family.geraghty.ed.kopher

import java.net.ServerSocket
import kotlin.concurrent.thread

fun main() {
    val server = ServerSocket(70) //port 70 is assigned to Internet Gopher by IANA

    while (true) {
        val client = server.accept()
        val baseDirectory = "data"

        thread { ClientHandler(client, baseDirectory).run() }
    }
}
