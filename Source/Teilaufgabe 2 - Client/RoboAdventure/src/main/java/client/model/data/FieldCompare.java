package client.model.data;

import java.util.Comparator;

public class FieldCompare implements Comparator<Field> {
    @Override
    public int compare(Field fieldFirst, Field fieldSecond) {
        if (fieldFirst.getPositionY() < fieldSecond.getPositionY()) {
            return -1;
        } else if (fieldFirst.getPositionY() > fieldSecond.getPositionY()) {
            return 1;
        }

        if (fieldFirst.getPositionX() < fieldSecond.getPositionX()) {
            return -1;
        } else if (fieldFirst.getPositionX() > fieldSecond.getPositionX()) {
            return 1;
        }


        return 0;
    }
}
