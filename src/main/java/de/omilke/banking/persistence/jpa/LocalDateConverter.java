package de.omilke.banking.persistence.jpa;

//import de.omilke.banking.account.entity.LocalDateIntegerConverter;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.time.LocalDate;
//
///**
// * This class fulfills the converter interface as of JPA 2.1 and uses
// * {@link LocalDateIntegerConverter} for the actual conversion.
// *
// * @author omilke <ollimi85@gmail.com>
// */
//@Converter(autoApply = true)
//public class LocalDateConverter implements AttributeConverter<LocalDate, Integer> {
//
//    private final LocalDateIntegerConverter converter = new LocalDateIntegerConverter();
//
//    @Override
//    public Integer convertToDatabaseColumn(final LocalDate attribute) {
//
//        return this.converter.toInteger(attribute);
//    }
//
//    @Override
//    public LocalDate convertToEntityAttribute(final Integer dbData) {
//
//        return this.converter.toLocalDate(dbData);
//    }
//
//}
