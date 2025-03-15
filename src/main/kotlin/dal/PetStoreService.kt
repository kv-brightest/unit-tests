package dal

import bll.ConfigReader
import com.google.gson.Gson
import exceptions.NoSuchPetException
import models.PetModel
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

class PetStoreService {
    // This will read the base url from a file in the resource-folder
    // During units tests it will point to the resource-folder of the test directory
    // This makes it possible to use different urls during unit testing
    private val baseUrl: String by lazy { ConfigReader.readConfigFromResource("/config.txt") }
    private val httpClient: HttpClient by lazy { HttpClient.newBuilder().build() }

    /**
     * Get a pet from the pet store
     * @param petId ID of the pet to fetch
     */
    fun getPet(petId: Int): PetModel {
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI("${baseUrl}/pet/$petId"))
            .GET()
            .build()

        val response: HttpResponse<String> = httpClient.send(request, BodyHandlers.ofString())
        println(response)

        when(response.statusCode()) {
            200 -> return Gson().fromJson(response.body(), PetModel::class.java)
            404, 400 -> throw NoSuchPetException("Error: ${response.body()}")
            else -> throw NoSuchPetException("Unknown error occurred: ${response.body()}")
        }
    }

    /**
     * Add a pet to the pet store
     * @param petModel Info of the new pet
     */
    fun addPet(petModel: PetModel): PetModel {
        TODO("Not yet implemented")
    }

    /**
     * Update a pet in the pet store
     * @param petModel Info of the pet
     */
    fun updatePet(petModel: PetModel): PetModel {
        TODO("Not yet implemented")
    }

    /**
     * Remove a pet from the pet store
     * @param petId ID of the pet which should be deleted
     */
    fun deletePet(petId: Int) {
        TODO("Not yet implemented")
    }
}
