//package de.omilke.banking.persistence.inmemory;
//
//import de.omilke.banking.account.entity.Category;
//
//import java.util.HashMap;
//import java.util.Map;
//
//class CategoryService {
//
//    private static final Map<String, Category> categories = new HashMap<>();
//
//    public Category findCategory(final String categoryName) {
//
//        return categories.computeIfAbsent(categoryName, Category::new);
//
//    }
//
//}
