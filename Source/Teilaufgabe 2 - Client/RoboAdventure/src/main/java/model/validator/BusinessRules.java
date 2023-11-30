package model.validator;

import model.data.Field;

import java.util.List;

public interface BusinessRules {
    boolean validateBusinessRules(List<Field> map);

    boolean isAllowedAmountOfWaterOnEdges(Field[][] matrix);

    int getROWS();

    int getCOLUMNS();
}
