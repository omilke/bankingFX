package de.omilke.banking.persistence.mapdb
//
//import de.omilke.banking.account.entity.Entry
//import de.omilke.banking.persistence.EntrySerializer
//import org.mapdb.DataInput2
//import org.mapdb.DataOutput2
//import org.mapdb.Serializer
//
//class EntrySerializerForMapDb: Serializer<Entry> {
//
//    override fun serialize(out: DataOutput2, value: Entry) {
//
//        out.writeUTF(EntrySerializer.toPlain(value))
//    }
//
//    override fun deserialize(input: DataInput2, available: Int): Entry {
//        val plain = input.readUTF()
//
//        return EntrySerializer.toEntry(plain)
//    }
//}