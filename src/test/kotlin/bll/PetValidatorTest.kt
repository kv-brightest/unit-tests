package bll

import exceptions.InvalidIdException
import exceptions.InvalidNameException
import exceptions.InvalidStatusException
import models.PetCategory
import models.PetModel
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class PetValidatorTest {
    private val petValidator: PetValidator = PetValidator()

    // Validate Pet

    @Test
    fun `test valid pet`() {
        val validPet = PetModel(
            id = 1,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        // validation should not throw any errors
        petValidator.validatePet(validPet)
    }

    @Test(expectedExceptions = [InvalidNameException::class])
    fun `test incorrect pet name`() {
        val validPet = PetModel(
            id = 1,
            name = "",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        // This will just check if the name validation is called
        // More detailed tests are written for the validateName-method
        // This test should fail with an InvalidNameException
        petValidator.validatePet(validPet)
    }

    @Test(expectedExceptions = [InvalidIdException::class])
    fun `test incorrect pet id`() {
        val validPet = PetModel(
            id = -1,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        // This will just check if the id validation is called
        // More detailed tests are written for the validateId-method
        // This test should fail with an InvalidNameException
        petValidator.validatePet(validPet)
    }

    @Test(expectedExceptions = [InvalidStatusException::class])
    fun `test incorrect pet status`() {
        val validPet = PetModel(
            id = 1,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Unavailable"
        )

        // This will just check if the status validation is called
        // More detailed tests are written for the validateStatus-method
        // This test should fail with an InvalidNameException
        petValidator.validatePet(validPet)
    }

    // Validate name

    @Test
    fun `test valid name`() {
        val validPet = PetModel(
            id = -1,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Unavailable"
        )

        // This method should only validate the name, errors in ID or status should be ignored
        petValidator.validateName(validPet)
    }

    @Test(expectedExceptions = [InvalidNameException::class], expectedExceptionsMessageRegExp = "The name of the pet can't be empty")
    fun `test empty name`() {
        val validPet = PetModel(
            id = 1,
            name = "",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateName(validPet)
    }

    @Test(expectedExceptions = [InvalidNameException::class], expectedExceptionsMessageRegExp = "The name of the pet can't be empty")
    fun `test blank name`() {
        val validPet = PetModel(
            id = 1,
            name = "                     ",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateName(validPet)
    }

    @Test(expectedExceptions = [InvalidNameException::class], expectedExceptionsMessageRegExp = "The name of the pet should contain at least 3 characters")
    fun `test name too short`() {
        val validPet = PetModel(
            id = 1,
            name = "ab",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateName(validPet)
    }

    @Test
    fun `test name exact length`() {
        // Ensure the validation only fails when the name is shorter than 3 characters, and not when exactly 3
        val validPet = PetModel(
            id = 1,
            name = "abc",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateName(validPet)
    }

    @Test(expectedExceptions = [InvalidNameException::class], expectedExceptionsMessageRegExp = "The name of the pet can't contain numbers")
    fun `test name contains numbers`() {
        val validPet = PetModel(
            id = 1,
            name = "N4me",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateName(validPet)
    }

    // This test will fail since I 'forgot' to add a check for special characters
    @Test(expectedExceptions = [InvalidNameException::class], expectedExceptionsMessageRegExp = "The name of the pet can't contain special characters")
    fun `test name contains special characters`() {
        val validPet = PetModel(
            id = 1,
            name = "Doggy!!!",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateName(validPet)
    }

    // Validate ID

    @Test
    fun `test valid id`() {
        val validPet = PetModel(
            id = 1,
            name = "Doggy!!!",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "UnAvailable"
        )

        petValidator.validateId(validPet)
    }

    @Test(expectedExceptions = [InvalidIdException::class], expectedExceptionsMessageRegExp = "The pet ID can't be negative")
    fun `test id can't be negative`() {
        val validPet = PetModel(
            id = -1,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateId(validPet)
    }

    @Test
    fun `test id is exactly 0`() {
        val validPet = PetModel(
            id = 0,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Available"
        )

        petValidator.validateId(validPet)
    }

    // validate status

    @Test(expectedExceptions = [InvalidStatusException::class], expectedExceptionsMessageRegExp = "The pet status can only be 'Available' or 'Reserved'")
    fun `test invalid status`() {
        val validPet = PetModel(
            id = 1,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = "Unavailable"
        )

        petValidator.validateStatus(validPet)
    }

    @DataProvider(name = "valid-status-provider")
    fun getValidStatuses(): Array<Array<Any>> {
        return arrayOf(arrayOf("Available"), arrayOf("Reserved"))
    }

    // This test will be executed multiple times
    // one time for each value in the data provider
    @Test(dataProvider = "valid-status-provider")
    fun `test valid statuses`(status: String) {
        val validPet = PetModel(
            id = 0,
            name = "Rocky",
            category = PetCategory(1, "Dog"),
            photoUrls = listOf(),
            tags = listOf(),
            status = status
        )

        petValidator.validateStatus(validPet)
    }
}
