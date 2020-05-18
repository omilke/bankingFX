package de.omilke.bankingfx.report.categories.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Created by Olli on 23.05.2017.
 */
public class CategoryOverTimeElement {

    private final StringProperty categoryName;

    private final ListProperty<BigDecimal> monthValues;
    private final boolean aggregateRow;

    private final ObjectProperty<BigDecimal> average;
    private final ObjectProperty<BigDecimal> sum;

    public CategoryOverTimeElement(String categoryName, ObservableList<BigDecimal> monthValues) {

        this(categoryName, monthValues, false);
    }

    private CategoryOverTimeElement(String categoryName, ObservableList<BigDecimal> monthValues, boolean aggregateRow) {

        this.categoryName = new SimpleStringProperty(this, null, categoryName);
        this.monthValues = new SimpleListProperty<>(this, "values", monthValues);
        this.aggregateRow = aggregateRow;

        BigDecimal summedAmount = getSum(monthValues);
        sum = new SimpleObjectProperty<>(this, null, summedAmount);

        //average based on the number of months spanned. That number includes months without expenditures but
        // excludes months that have no expenditures at all (those month would be skipped as a column)
        average = new SimpleObjectProperty<>(this, null, getAverage(summedAmount, monthValues.size()));
    }

    private static BigDecimal getSum(ObservableList<BigDecimal> values) {

        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getAverage(BigDecimal sum, int size) {

        return sum.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP);
    }

    public String getCategoryName() {

        return categoryName.get();
    }

    public StringProperty categoryNameProperty() {

        return categoryName;
    }

    public ObservableList<BigDecimal> getMonthValues() {

        return monthValues.get();
    }

    public BigDecimal getAverage() {

        return average.get();
    }

    public ObjectProperty<BigDecimal> averageProperty() {

        return average;
    }

    public BigDecimal getSum() {

        return sum.get();
    }

    public ObjectProperty<BigDecimal> sumProperty() {

        return sum;
    }

    public boolean isAggregateRow() {

        return aggregateRow;
    }

    public static CategoryOverTimeElement buildSumOverTimeElement(List<CategoryOverTimeElement> categorySums) {

        ObservableList<BigDecimal> monthSumsOverTime = FXCollections.observableArrayList();

        //build sum by month (i. e. per element of value) over all model elements
        for (CategoryOverTimeElement categoryOverTime : categorySums) {

            ObservableList<BigDecimal> monthValues = categoryOverTime.getMonthValues();
            for (int monthIndex = 0; monthIndex < monthValues.size(); monthIndex++) {

                //if a month-column is missing, add it
                if (monthSumsOverTime.size() < monthIndex + 1) {
                    monthSumsOverTime.add(BigDecimal.ZERO);
                }

                BigDecimal presentlySummedValue = monthSumsOverTime.get(monthIndex);
                BigDecimal categoryMonthValue = monthValues.get(monthIndex);

                if (categoryMonthValue != null) {

                    //replace old sum with the current value added on top if there is a sum for this category and month
                    monthSumsOverTime.set(monthIndex, presentlySummedValue.add(categoryMonthValue));
                }
            }
        }

        return new CategoryOverTimeElement("Sum", monthSumsOverTime, true);
    }

}
