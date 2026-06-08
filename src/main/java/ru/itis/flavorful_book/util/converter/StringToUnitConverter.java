package ru.itis.flavorful_book.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.flavorful_book.entity.enums.Unit;

@Component
public class StringToUnitConverter implements Converter<String, Unit> {

    @Override
    public Unit convert(String source) {
        String trimmed = source.trim().toUpperCase();
        try {
            return Unit.valueOf(trimmed);
        } catch (IllegalArgumentException e) {
            for (Unit unit : Unit.values()) {
                if (unit.getUnit().equalsIgnoreCase(source.trim())) {
                    return unit;
                }
            }
            throw new IllegalArgumentException("Неизвестная единица измерения: " + source);
        }
    }
}
