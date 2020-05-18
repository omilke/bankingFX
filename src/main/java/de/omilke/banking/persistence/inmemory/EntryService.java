package de.omilke.banking.persistence.inmemory;

import de.omilke.banking.account.entity.Entry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class EntryService {

    private final class EntryComparator implements Comparator<Entry> {

        @Override
        public int compare(final Entry o1, final Entry o2) {

            return o2.getEntryDate().compareTo(o1.getEntryDate());
        }
    }

    private static final List<Entry> entries = new ArrayList<>();

    public void save(final Entry entry) {

        entries.add(entry);
    }

    public Collection<Entry> findAllEntries() {

        final ArrayList<Entry> result = new ArrayList<>(entries);

        result.sort(new EntryComparator());

        return result;
    }

    public List<Entry> findAllEntriesUpTo(final LocalDate value) {

        final LocalDate nextDay = value.plusDays(1);
        return entries
                .stream()
                .filter(current -> current.getEntryDate().isBefore(nextDay))
                .sorted(new EntryComparator())
                .collect(Collectors.toList());

    }

}
