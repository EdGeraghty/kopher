package family.geraghty.ed.kopher

data class DirEntity(val itemType: Char,
                     val userName: String,
                     var selectorString: String,
                     var host: String,
                     var realPath: String?,
                     var port: Int = 70,
) {

    override fun toString(): String {
        return "$itemType$userName\t$selectorString\t$host\t$port"
    }
}
