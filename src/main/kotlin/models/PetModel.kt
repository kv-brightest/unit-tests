package models

data class PetModel(
    val id: Int,
    val category: PetCategory,
    val name: String,
    val photoUrls: List<String>,
    val tags: List<PetTag>,
    val status: String
)

data class PetCategory(val id: Int, val name: String)
data class PetTag(val id: Int, val name: String)
