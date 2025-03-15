package bll

import dal.PetStoreService
import exceptions.NoSuchPetException
import models.PetModel

class PetInventory {
    private val petStoreService: PetStoreService by lazy { PetStoreService() }

    fun addPetToStore(petModel: PetModel): PetModel {
        TODO("Not yet implemented")
    }

    /**
     * Get a pet from the pet store
     * Returns null when the pet doesn't exist
     * @param petId ID of the pet
     */
    fun getPetFromStore(petId: Int) : PetModel? {
        return try {
            petStoreService.getPet(petId)
        } catch (noSuchPetException: NoSuchPetException) {
            null
        } catch (exception: Exception) {
            throw Exception("Unknown exception occurred while fetching your pet", exception)
        }
    }
}
