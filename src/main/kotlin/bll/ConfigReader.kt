package bll

object ConfigReader {
    /**
     * Read a config file from the resource folder
     * @param configFileName Name of the file
     */
    fun readConfigFromResource(configFileName: String): String {
        return object {}.javaClass
            .getResourceAsStream(configFileName)
            ?.bufferedReader()
            ?.readText()
            ?.trim()
            ?: throw IllegalArgumentException("Config file does not exists")
    }
}
