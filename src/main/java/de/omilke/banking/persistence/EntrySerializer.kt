package de.omilke.banking.persistence

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.EntrySequence
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object EntrySerializer {

    private const val SEPARATOR: String = "|||"
    private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun toEntry(plain: String): Entry {

        val list = plain.split(SEPARATOR)

        return Entry(
                amount = list[0].toBigDecimal(),
                entryDate = LocalDate.from(FORMATTER.parse(list[1])),
                comment = list[2],
                category = list[3],
                isSaving = list[4].toBoolean(),
                sequence = EntrySequence.valueOf(list[5]),
                orderIndex = list[6].toInt(),
                uuid = UUID.fromString(list[7])
        )
    }

    fun toPlain(entry: Entry): String {

        val sb = StringBuilder()

        sb.append(entry.amount.toEngineeringString()).append(SEPARATOR)
        sb.append(entry.entryDate.format(FORMATTER)).append(SEPARATOR)
        sb.append(entry.comment).append(SEPARATOR)
        sb.append(entry.category).append(SEPARATOR)
        sb.append(entry.isSaving).append(SEPARATOR)
        sb.append(entry.sequence.name).append(SEPARATOR)
        sb.append(entry.orderIndex).append(SEPARATOR)
        sb.append(entry.uuid)

        return sb.toString()
    }

}