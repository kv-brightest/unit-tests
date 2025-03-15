package dal

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import exceptions.NoSuchPetException
import models.PetCategory
import models.PetModel
import models.PetTag
import org.assertj.core.api.Assertions
import org.testng.annotations.AfterClass
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PetStoreServiceTest {
    private val wireMockServer: WireMockServer = WireMockServer()

    @BeforeClass
    fun setup() {
        // This will start a webserver on localhost:8080
        // In the config.txt file in the test-resources we point to this endpoint
        wireMockServer.start()
    }

    @AfterMethod
    fun clearStubs() {
        // Clear stubs after each test
        wireMockServer.stubMappings.clear()
    }

    @Test
    fun  `Get existing pet`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(get(urlEqualTo("/pet/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("{\n" +
                            "  \"id\": 1,\n" +
                            "  \"category\": {\n" +
                            "    \"id\": 0,\n" +
                            "    \"name\": \"dog\"\n" +
                            "  },\n" +
                            "  \"name\": \"doggie\",\n" +
                            "  \"photoUrls\": [\n" +
                            "    \"url1\"\n" +
                            "  ],\n" +
                            "  \"tags\": [\n" +
                            "    {\n" +
                            "      \"id\": 0,\n" +
                            "      \"name\": \"tag0\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"status\": \"available\"\n" +
                            "}")
            ))

        // Show stub configuration in logs, so you can easily verify it
        wireMockServer.stubMappings.forEach(::println)

        // Prepare expected result
        val expectedPet = PetModel(
            id = 1,
            category = PetCategory(0, "dog"),
            name = "doggie",
            photoUrls = listOf("url1"),
            tags = listOf(PetTag(0, "tag0")),
            status = "available"
        )

        // Actual test
        val pet = PetStoreService().getPet(1)
        Assertions.assertThat(pet).isEqualTo(expectedPet)
    }

    @Test(expectedExceptions = [NoSuchPetException::class], expectedExceptionsMessageRegExp = "Error: Pet not found")
    fun  `Get non-existing pet`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(get(urlEqualTo("/pet/37"))
            .willReturn(
                aResponse()
                    .withStatus(404)
                    .withBody("Pet not found")
            ))

        // Show stub configuration in logs, so you can easily verify it
        wireMockServer.stubMappings.forEach(::println)

        // Actual test
        PetStoreService().getPet(37)
    }

    @Test(expectedExceptions = [NoSuchPetException::class], expectedExceptionsMessageRegExp = "Error: Invalid ID supplied")
    fun  `Get pet with invalid ID`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(get(urlEqualTo("/pet/-1"))
            .willReturn(
                aResponse()
                    .withStatus(400)
                    .withBody("Invalid ID supplied")
            ))

        // Show stub configuration in logs, so you can easily verify it
        wireMockServer.stubMappings.forEach(::println)

        // Actual test
        PetStoreService().getPet(-1)
    }

    @Test(
        expectedExceptions = [NoSuchPetException::class],
        expectedExceptionsMessageRegExp = "Unknown error occurred: Internal server error"
    )
    fun  `Get pet with unknown error`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(get(urlEqualTo("/pet/0"))
            .willReturn(
                aResponse()
                    .withStatus(500)
                    .withBody("Internal server error")
            ))

        // Show stub configuration in logs, so you can easily verify it
        wireMockServer.stubMappings.forEach(::println)

        // Actual test
        PetStoreService().getPet(0)
    }

    @Test
    fun testAddPet() {
        // TODO
    }

    @Test
    fun testUpdatePet() {
        // TODO
    }

    @Test
    fun testDeletePet() {
        // TODO
    }

    @AfterClass
    fun teardown() {
        // This will stop the webserver
        wireMockServer.stop()
    }
}