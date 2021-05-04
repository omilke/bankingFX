package de.omilke.bankingfx.report.savings;

import de.omilke.banking.account.entity.Entry;
import de.omilke.banking.account.entity.EntryRepository;
import de.omilke.banking.persistence.PersistenceServiceProvider;
import de.omilke.bankingfx.report.savings.model.Category;
import de.saxsys.mvvmfx.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SavingsModel implements ViewModel {

    private EntryRepository er = PersistenceServiceProvider.INSTANCE.getPersistenceService().getEntryRepository();

    private List<Category> categories = new ArrayList<>();

    public List<Category> getCategories() {
        return categories;
    }

    public void initialize() {

        prepareModel();
    }

    private void prepareModel() {

        for (Entry current : er.findAllEntries()) {
            if (current.isSaving()) {
                var category = getFromProperty(current.getCategory());
                category.addEntry(de.omilke.bankingfx.report.savings.model.Entry.fromStorageModel(current));
            }
        }

        categories.sort(Comparator.comparing(Category::getSum).thenComparing(Category::getLowerCaseName));
    }

    private Category getFromProperty(String name) {

        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }

        Category newCategory = new Category(name);
        categories.add(newCategory);

        return newCategory;
    }

}
