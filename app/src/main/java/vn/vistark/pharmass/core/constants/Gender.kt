package vn.vistark.pharmass.core.constants

class Gender(val code: Int, val name: String) {
    companion object {
        val SKIP = -1
        val MALE = 1
        val FEMALE = 2
        val all: Array<Gender>
            get() = arrayOf(
                Gender(SKIP, "Bỏ qua giới tính"),
                Gender(MALE, "Nam"),
                Gender(FEMALE, "Nữ")
            )

        fun find(code: Int): String {
            return all.find { it.code == code }?.name ?: all.first().name
        }
    }
}