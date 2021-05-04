package de.omilke.bankingfx.report.categories.model

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by Olli on 23.05.2017.
 */
class CategoryOverTimeElement {

    private val categoryName: StringProperty
    private val monthValues: ListProperty<BigDecimal?>
    private val average: ObjectProperty<BigDecimal>
    private val sum: ObjectProperty<BigDecimal>

    val isAggregateRow: Boolean

    //TODO: nullable necessary?
    //TODO: why OberserableList?
    constructor(categoryName: String, monthValues: ObservableList<BigDecimal?>) :
            this(categoryName, monthValues, false)

    private constructor(categoryName: String, monthValues: ObservableList<BigDecimal?>, aggregateRow: Boolean) {

        this.categoryName = SimpleStringProperty(this, null, categoryName)
        this.monthValues = SimpleListProperty(this, "values", monthValues)

        val monthSum = getSum(monthValues)
        this.sum = SimpleObjectProperty(this, null, monthSum)
        this.average = SimpleObjectProperty(this, null, getAverage(monthSum, monthValues.size))

        isAggregateRow = aggregateRow
    }

    fun getCategoryName(): String {
        return categoryName.get()
    }

    fun categoryNameProperty(): StringProperty {
        return categoryName
    }

    fun getMonthValues(): ObservableList<BigDecimal?> {
        return monthValues.get()
    }

    fun getAverage(): BigDecimal {
        return average.get()
    }

    fun averageProperty(): ObjectProperty<BigDecimal> {
        return average
    }

    fun getSum(): BigDecimal {
        return sum.get()
    }

    fun sumProperty(): ObjectProperty<BigDecimal> {
        return sum
    }

    companion object {

        private fun getSum(values: ObservableList<BigDecimal?>): BigDecimal {
            return values
                    .filterNotNull()
                    .fold(BigDecimal.ZERO, BigDecimal::add)
        }

        private fun getAverage(sum: BigDecimal, size: Int): BigDecimal {
            return sum.divide(BigDecimal(size), 2, RoundingMode.HALF_UP)
        }

        fun buildSumOverTimeElement(categorySums: List<CategoryOverTimeElement>): CategoryOverTimeElement {

            val monthSumsOverTime = FXCollections.observableArrayList<BigDecimal>()

            //build sum by month (i. e. per element of value) over all model elements
            for (categoryOverTime in categorySums) {

                val monthValues = categoryOverTime.getMonthValues()
                for (monthIndex in monthValues.indices) {

                    //if a month-column is missing, add it
                    if (monthSumsOverTime.size < monthIndex + 1) {
                        monthSumsOverTime.add(BigDecimal.ZERO)
                    }

                    val presentlySummedValue = monthSumsOverTime[monthIndex]
                    val categoryMonthValue = monthValues[monthIndex]

                    if (categoryMonthValue != null) {

                        //replace old sum with the current value added on top if there is a sum for this category and month
                        monthSumsOverTime[monthIndex] = presentlySummedValue.add(categoryMonthValue)
                    }
                }
            }

            return CategoryOverTimeElement("Sum", monthSumsOverTime, true)
        }
    }

}