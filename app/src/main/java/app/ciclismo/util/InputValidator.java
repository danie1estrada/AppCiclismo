package app.ciclismo.util;

import android.widget.EditText;

import java.util.regex.Pattern;

public class InputValidator {

    public static final String FIELD_NOT_EMPTY_ERROR = "Este campo no puede estar vacío";
    public static final String EMAIL_NOT_VALID_ERROR = "Formato de email no válido";

    private static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String TEXT_ONLY_PATTERN = "A-Za-z ñÑáÁéÉíÍóÓúÚ";
    private static final String NUMBERS_ONLY_PATTERN = "\\d*";

    public static boolean validateFieldNotEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError(FIELD_NOT_EMPTY_ERROR);
            return false;
        }
        return true;
    }

    public static boolean validateEmail(EditText editText) {
        return validatePattern(editText, EMAIL_PATTERN, EMAIL_NOT_VALID_ERROR);
    }

    public static boolean validateMinLength(EditText editText, int minLength) {
        if (editText.getText().toString().length() < minLength) {
            editText.setError("Debe contener al menos " + minLength + " caracteres");
            return false;
        }
        return true;
    }

    public static boolean validatePattern(EditText editText, String regex, String message) {
        String input = editText.getText().toString();
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(input).matches()) {
            editText.setError(message);
            return false;
        }
        return true;
    }
}
