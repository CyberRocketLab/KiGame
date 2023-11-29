package exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Notification {
    private final List<String> errors = new ArrayList<>();

    public void addError(String message) {
        errors.add(message);
    }

    public String getErrors() {
        return String.join(", ", errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }


}
