package vn.vistark.pharmass.core.constants

class Gender(val code: Int, val name: String) {
    companion object {
        val all: Array<Gender>
            get() = arrayOf(
                Gender(-1, "Chưa chọn giới tính"),
                Gender(1, "Nam"),
                Gender(2, "Nữ"),
                Gender(3, "Bỏ qua")
            )

        fun find(code: Int): String {
            return all.find { it.code == code }?.name ?: all.first().name
        }
    }
}