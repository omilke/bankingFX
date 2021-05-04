package de.omilke.bankingfx.main.sequenceeditor

class InvalidEntryOrderException : Exception {

    constructor(orderIndex: Int) : super(formatMessage(orderIndex))
    constructor(orderIndex: String?) : super(formatMessage(orderIndex))

}

fun formatMessage(orderIndex: Any?): String {
    return String.format("Invalid Order Index: %s", orderIndex)
}
