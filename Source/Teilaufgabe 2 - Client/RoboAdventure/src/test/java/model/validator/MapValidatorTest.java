package model.validator;

import exceptions.MapBusinessRuleException;
import model.data.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MapValidatorTest {
    @Mock
    BusinessRules businessRules;

    @Mock
    MapValidator mapValidator;

    @BeforeEach
    public void init() {
        businessRules = Mockito.mock(BusinessRules.class);

        when(businessRules.getROWS()).thenReturn(5);
        when(businessRules.getCOLUMNS()).thenReturn(10);

        mapValidator = new MapValidator(businessRules);
    }

    @Test
    public void businessRulesFails_whenReturning_thenReturningFalse() {
        Field field = Mockito.mock(Field.class);
        List<Field> map = Arrays.asList(field,field,field,field);

        when(businessRules.validateBusinessRules(map)).thenReturn(false);

        assertFalse(mapValidator.validateMap(map));
    }

    @Test
    public void ListIsEmpty_whenPassing_thenMapBusinessRuleException() {
        List<Field> fieldList = new ArrayList<>();

        assertThrows(MapBusinessRuleException.class, () -> mapValidator.validateMap(fieldList));
    }

    @Test
    public void ListIsNULL_whenPassing_thenMapBusinessRuleException() {
        assertThrows(MapBusinessRuleException.class, () -> mapValidator.validateMap(null));
    }

    @Test
    public void waterOnEdges_whenProcessing_thenReturnFalse() {
        Field field = Mockito.mock(Field.class);

        List<Field> fields = Arrays.asList(field, field);

        Field[][] matrix = new Field[5][10];

        when(businessRules.validateBusinessRules(fields)).thenReturn(true);
        when(businessRules.isAllowedAmountOfWaterOnEdges(matrix)).thenReturn(false);

        assertFalse(mapValidator.validateMap(fields));
    }



}