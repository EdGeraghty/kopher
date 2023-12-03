package family.geraghty.ed.kopher

import java.io.File
import java.nio.file.Files

data class DirEntity(val item_type: Char,
                     val user_name: String,
                     var selector_string: String,
                     var host: String,
                     var real_path: String?,
                     var port: Int = 70,
                    ) {

    override fun toString(): String {
        return "$item_type$user_name\t$selector_string\t$host\t$port"
    }
}
