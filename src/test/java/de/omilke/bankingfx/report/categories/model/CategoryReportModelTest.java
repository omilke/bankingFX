package de.omilke.bankingfx.report.categories.model;


import de.omilke.banking.account.entity.Entry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryReportModelTest {

    @Test
    public void testModelDataIsProperlyCreated() {

        //this test shall prove that each 'Entry' is considered in the creation of the ModelData, especially with respect to its months and category.
        //this test is also to prove that the synthetic sum ModelElement is append in the end, independently of category names. However, the proper calculation is verified in the CategoryOverTimeElementTest and therefore not checked in detail in this test.

        List<Entry> entries = new ArrayList<>();

        entries.add(entryOf(LocalDate.of(2017, Month.MAY, 30), new BigDecimal("123"), "Gehalt"));
        entries.add(entryOf(LocalDate.of(2017, Month.MAY, 21), new BigDecimal("-100"), "Auto"));
        entries.add(entryOf(LocalDate.of(2017, Month.MAY, 25), new BigDecimal("-12"), "Essen"));
        entries.add(entryOf(LocalDate.of(2016, Month.DECEMBER, 24), new BigDecimal("-42"), "Essen"));
        entries.add(entryOf(LocalDate.of(2016, Month.DECEMBER, 24), new BigDecimal("41"), "Z-Last"));

        CategoryReportModel result = new CategoryReportModel(entries);

        assertThat(result.getYearMonthColumns()).isNotNull();
        assertThat(result.getYearMonthColumns()).hasSize(2);
        assertThat(result.getYearMonthColumns()).containsExactly(YearMonth.of(2016, Month.DECEMBER), YearMonth.of(2017, Month.MAY));

        assertThat(result.getCategoriesWithExpenditure()).isNotNull();
        assertThat(result.getCategoriesWithExpenditure()).hasSize(4);
        assertThat(result.getCategoriesWithExpenditure()).containsExactly("Auto", "Essen", "Gehalt", "Z-Last");

        List<CategoryOverTimeElement> resultModelData = result.getModelData();

        //3 category + synthetic Sum category
        assertThat(resultModelData).hasSize(5);

        CategoryOverTimeElement firstElement = resultModelData.get(0);
        assertThat(firstElement.getCategoryName()).isEqualTo("Auto");

        //no value for Auto in December
        assertThat(firstElement.getMonthValues().get(0)).isNull();
        //one value in May
        assertThat(firstElement.getMonthValues().get(1)).isEqualByComparingTo(new BigDecimal("-100"));

        CategoryOverTimeElement secondElement = resultModelData.get(1);
        assertThat(secondElement.getCategoryName()).isEqualTo("Essen");

        //one value in December
        assertThat(secondElement.getMonthValues().get(0)).isEqualByComparingTo(new BigDecimal("-42"));
        //one value in May
        assertThat(secondElement.getMonthValues().get(1)).isEqualByComparingTo(new BigDecimal("-12"));

        CategoryOverTimeElement thirdElement = resultModelData.get(2);
        assertThat(thirdElement.getCategoryName()).isEqualTo("Gehalt");

        //no  value in December
        assertThat(thirdElement.getMonthValues().get(0)).isNull();
        //one value in May
        assertThat(thirdElement.getMonthValues().get(1)).isEqualByComparingTo(new BigDecimal("123"));

        //not the last element, even though it has the "highest" name
        CategoryOverTimeElement fourthElement = resultModelData.get(3);
        assertThat(fourthElement.getCategoryName()).isEqualTo("Z-Last");

        //one  value in December
        assertThat(fourthElement.getMonthValues().get(0)).isEqualByComparingTo(new BigDecimal("41"));
        //no value in May
        assertThat(fourthElement.getMonthValues().get(1)).isNull();


        //this is always the last element, even if there are categories with "less" name
        CategoryOverTimeElement fifthElement = resultModelData.get(4);
        assertThat(fifthElement.getCategoryName()).isEqualTo("Sum");

        assertThat(fifthElement.getMonthValues().get(0)).isEqualByComparingTo(new BigDecimal("-1"));
        assertThat(fifthElement.getMonthValues().get(1)).isEqualByComparingTo(new BigDecimal("11"));

    }

    @Test
    public void testOrderOfCategories() {

        List<Entry> entries = new ArrayList<>();

        entries.add(entryOf("Z-Last"));
        entries.add(entryOf("Autos-is-to-be-greater"));
        entries.add(entryOf("Auto:Versicherung"));
        entries.add(entryOf("Auto:Tanken"));
        entries.add(entryOf("Auto"));
        entries.add(entryOf(""));

        CategoryReportModel model = new CategoryReportModel(entries);

        SortedSet<String> result = model.getCategoriesWithExpenditure();

        assertThat(result).containsExactly("",
                "Auto",
                "Auto:Tanken",
                "Auto:Versicherung",
                "Autos-is-to-be-greater",
                "Z-Last");

    }

    private Entry entryOf(LocalDate date, BigDecimal value, String categoryName) {

        return new Entry(value, date, "", categoryName);
    }

    private Entry entryOf(String categoryName) {

        return entryOf(LocalDate.MIN, BigDecimal.ONE, categoryName);
    }

}