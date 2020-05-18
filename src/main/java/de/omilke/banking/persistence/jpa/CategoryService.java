//package de.omilke.banking.persistence.jpa;
//
//import de.omilke.banking.account.entity.Category;
//import de.omilke.banking.account.entity.CategoryRepository;
//import de.omilke.banking.storage.Jpa;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import javax.persistence.TypedQuery;
//import java.util.List;
//
//public class CategoryService implements CategoryRepository {
//
//    private final EntityManager em = Jpa.INSTANCE.getEntityManager();
//
//    @Override
//    public Category findCategory(final String categoryName) {
//
//        final EntityTransaction transaction = this.em.getTransaction();
//        transaction.begin();
//
//        final TypedQuery<Category> query = this.em.createNamedQuery(Category.SELECT_BY_NAME, Category.class);
//        query.setParameter("name", categoryName);
//
//        final List<Category> resultList = query.getResultList();
//
//        final Category result;
//        if (resultList.isEmpty()) {
//            result = new Category(categoryName);
//            this.em.persist(result);
//        } else {
//            result = resultList.get(0);
//        }
//
//        transaction.commit();
//
//        return result;
//    }
//
//    @Override
//    public List<Category> findAllCategories() {
//
//        final TypedQuery<Category> query = this.em.createNamedQuery(Category.SELECT_ALL, Category.class);
//
//        final List<Category> resultList = query.getResultList();
//
//        return resultList;
//    }
//}
