package bll

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import models.PetCategory
import models.PetModel
import models.PetTag
import org.assertj.core.api.Assertions
import org.testng.annotations.AfterClass
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PetInventoryTest {
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

    @AfterClass
    fun teardown() {
        // This will stop the webserver
        wireMockServer.stop()
    }

    @Test
    fun testAddPetToStore() {
        // TODO
    }

    @Test
    fun  `Get existing pet`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/pet/1"))
            .willReturn(
                WireMock.aResponse()
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
        val pet = PetInventory().getPetFromStore(1)
        Assertions.assertThat(pet).isEqualTo(expectedPet)
    }

    @Test(
        expectedExceptions = [Exception::class],
        expectedExceptionsMessageRegExp = "Unknown exception occurred while fetching your pet"
    )
    fun  `Get pet which returns an invalid JSON response`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/pet/25"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody("{ Some: Invalid, json }}")
                ))

        // Show stub configuration in logs, so you can easily verify it
        wireMockServer.stubMappings.forEach(::println)

        // Actual test
        PetInventory().getPetFromStore(25)
    }

    @Test
    fun  `Get non-existing pet`() {
        // Setup stub for this test scenario
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/pet/0"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(404)
                    .withBody("Pet not found")
            ))

        // Show stub configuration in logs, so you can easily verify it
        wireMockServer.stubMappings.forEach(::println)

        // Actual test
        val pet = PetInventory().getPetFromStore(0)
        Assertions.assertThat(pet).isNull()
    }
}