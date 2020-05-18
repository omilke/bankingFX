package de.omilke.banking.account.entity

/**
 * Criterion for sorting entries. It enables to set some entries to the beginning and some to the end of the month.
 *
 * @author Oliver Milke
 * @since 10.08.2015
 */
enum class EntrySequence {

    FIRST, REGULAR, LAST;

    companion object {

        fun of(s: String?): EntrySequence? {

            return when {
                s == null || s.isEmpty() -> null
                else -> try {
                    valueOf(s)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }

        fun of(sequence: Int?): EntrySequence? {
            return when {
                sequence != null -> values()[sequence]
                else -> null
            }
        }
    }
}