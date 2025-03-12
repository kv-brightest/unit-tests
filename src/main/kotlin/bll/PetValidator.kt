package bll

import exceptions.InvalidIdException
import exceptions.InvalidNameException
import exceptions.InvalidStatusException
import models.PetModel

class PetValidator {

    fun validatePet(pet: PetModel) {
        validateName(pet)
        validateId(pet)
        validateStatus(pet)

    }

    internal fun validateName(pet: PetModel) {
        if(pet.name.isBlank()) {
            throw InvalidNameException("The name of the pet can't be empty")
        } else if(pet.name.length < 3) {
            throw InvalidNameException("The name of the pet should contain at least 3 characters")
        } else if(pet.name.contains(Regex("[0-9]"))) {
            throw InvalidNameException("The name of the pet can't contain numbers")
        }
    }


    internal fun validateId(pet: PetModel) {
        if(pet.id < 0) {
            throw InvalidIdException("The pet ID can't be negative")
        }
    }

    internal fun validateStatus(pet: PetModel) {
        if(pet.status !in listOf("Available", "Reserved")) {
            throw InvalidStatusException("The pet status can only be 'Available' or 'Reserved'")
        }
    }
}