package de.omilke.bankingfx.report.categories.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Olli on 23.05.2017.
 */
public class CategoryOverTimeElementTest {

    @Test
    public void averageIsProperlyCalculated() {

        ObservableList<BigDecimal> firstElementValues = FXCollections.observableArrayList();
        firstElementValues.add(new BigDecimal(1));
        firstElementValues.add(new BigDecimal(3));
        firstElementValues.add(new BigDecimal(6));

        //10.0 / 3 = 3.3333 : round down, 2 digits
        CategoryOverTimeElement first = new CategoryOverTimeElement("first", firstElementValues);
        assertThat(first.getAverage()).isEqualByComparingTo(new BigDecimal("3.33"));


        ObservableList<BigDecimal> secondElementValues = FXCollections.observableArrayList();
        secondElementValues.add(new BigDecimal(1));
        secondElementValues.add(new BigDecimal(3));
        secondElementValues.add(new BigDecimal("6.005"));

        //10.005 / 3 = 3.335 : round up
        CategoryOverTimeElement second = new CategoryOverTimeElement("second", secondElementValues);
        assertThat(second.getAverage()).isEqualByComparingTo(new BigDecimal("3.34"));
    }

    @Test
    public void sumIsProperlyCalculated() {

        ObservableList<BigDecimal> firstElementValues = FXCollections.observableArrayList();
        firstElementValues.add(new BigDecimal(1));
        firstElementValues.add(new BigDecimal(3));
        firstElementValues.add(new BigDecimal(6));

        CategoryOverTimeElement first = new CategoryOverTimeElement("first", firstElementValues);
        assertThat(first.getSum()).isEqualByComparingTo(new BigDecimal("10"));


        ObservableList<BigDecimal> secondElementValues = FXCollections.observableArrayList();
        secondElementValues.add(new BigDecimal(1));
        secondElementValues.add(new BigDecimal(3));
        secondElementValues.add(new BigDecimal("6.005"));

        CategoryOverTimeElement second = new CategoryOverTimeElement("second", secondElementValues);
        assertThat(second.getSum()).isEqualByComparingTo(new BigDecimal("10.005"));
    }

    @Test
    public void buildSumModelYieldsCorrectValues() {

        List<CategoryOverTimeElement> categorySums = new ArrayList<>();

        ObservableList<BigDecimal> firstValuesOverTime = FXCollections.observableArrayList();
        firstValuesOverTime.add(new BigDecimal(1));
        firstValuesOverTime.add(new BigDecimal(2));
        firstValuesOverTime.add(new BigDecimal(4));

        categorySums.add(new CategoryOverTimeElement("first category", firstValuesOverTime));

        ObservableList<BigDecimal> secondValuesOverTime = FXCollections.observableArrayList();
        secondValuesOverTime.add(new BigDecimal(0));
        secondValuesOverTime.add(new BigDecimal(-1));
        secondValuesOverTime.add(new BigDecimal(-2));

        categorySums.add(new CategoryOverTimeElement("second", secondValuesOverTime));

        //there does not need to be an expenditure for a category in a month
        ObservableList<BigDecimal> thirdElementValue = FXCollections.observableArrayList();
        thirdElementValue.add(null);
        thirdElementValue.add(new BigDecimal(100));
        thirdElementValue.add(new BigDecimal("0.5"));

        categorySums.add(new CategoryOverTimeElement("second", thirdElementValue));

        CategoryOverTimeElement sumOverTime = CategoryOverTimeElement.buildSumOverTimeElement(categorySums);

        assertThat(sumOverTime.getCategoryName()).isEqualTo("Sum");
        assertThat(sumOverTime.getMonthValues().get(0)).isEqualByComparingTo(new BigDecimal(1));
        assertThat(sumOverTime.getMonthValues().get(1)).isEqualByComparingTo(new BigDecimal(101));
        assertThat(sumOverTime.getMonthValues().get(2)).isEqualByComparingTo(new BigDecimal("2.5"));

        //the sum over these values, i. e. the total sum of all month values over the whole time and its average
        assertThat(sumOverTime.getSum()).isEqualByComparingTo(new BigDecimal("104.5"));
        assertThat(sumOverTime.getAverage()).isEqualByComparingTo(new BigDecimal("34.83"));

    }

}