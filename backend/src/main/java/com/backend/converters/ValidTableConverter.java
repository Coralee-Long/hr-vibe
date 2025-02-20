package com.backend.converters;

import com.backend.enums.ValidTable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ValidTableConverter implements Converter<String, ValidTable> {
   @Override
   public ValidTable convert(String source) {
      return ValidTable.fromString(source);
   }
}