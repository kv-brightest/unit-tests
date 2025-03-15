package bll

import org.assertj.core.api.Assertions
import org.testng.annotations.Test

class ConfigReaderTest {

    @Test
    fun `read existing config file from resources`() {
        val result = ConfigReader.readConfigFromResource("/testConfigFile.txt")
        Assertions.assertThat(result).isEqualTo("hello world")
    }

    @Test(expectedExceptions = [IllegalArgumentException::class], expectedExceptionsMessageRegExp = "Config file does not exists")
    fun `read non-existing config file from resources`() {
        ConfigReader.readConfigFromResource("/nosuchfile.txt")
    }
}
