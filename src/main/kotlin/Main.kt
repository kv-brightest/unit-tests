import bll.PetInventory

fun main(args: Array<String>) {

    val petInventory: PetInventory = PetInventory()
    val pet = petInventory.getPetFromStore(99999)

    println("You fetched ${pet?.name} from the store")
}
