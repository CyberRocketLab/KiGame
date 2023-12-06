package client.model.data;

import client.exceptions.FieldException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    public void givenNullParameters_whenInitializing_shouldThrowException() {
        assertThrows(FieldException.class, () -> {
            Field field = new Field(
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null);
        });
    }

}