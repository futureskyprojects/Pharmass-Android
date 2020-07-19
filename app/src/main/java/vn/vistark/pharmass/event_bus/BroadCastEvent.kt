package vn.vistark.pharmass.event_bus

interface BroadCastEvent {
    public fun receivedString(key: String, value: String)
}