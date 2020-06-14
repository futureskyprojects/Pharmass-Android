package vn.vistark.pharmass.core.constants

data class PharmacyStaffPostion(val code: String, val name: String) {
    companion object {
        val NO_POSITION = "NO_POSITION"
        val SELLER = "SELLER"
        val MANAGER = "MANAGER"
        val SHIPPER = "SHIPPER"

        val all: Array<PharmacyStaffPostion>
            get() {
                return arrayOf(
                    PharmacyStaffPostion(NO_POSITION, "Chưa xếp vị trí"),
                    PharmacyStaffPostion(SELLER, "Bán hàng"),
                    PharmacyStaffPostion(MANAGER, "Quản lý"),
                    PharmacyStaffPostion(SHIPPER, "Giao hàng")
                )
            }

        fun get(code: String = ""): Array<PharmacyStaffPostion> {
            if (code.isEmpty()) {
                all.find { it.code == code }.apply {
                    return if (this == null)
                        emptyArray()
                    else
                        arrayOf(this)
                }
            } else return all
        }

    }
}