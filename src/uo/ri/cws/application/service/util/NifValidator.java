package uo.ri.cws.application.service.util;

/**
 * Checks the format of a NIF: eight digits followed by the control letter
 * that corresponds to them
 */
public class NifValidator {

    private static final String CONTROL_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final int DIGITS = 8;

    private NifValidator() {
    }

    /**
     * @param nif
     * @return true if the nif has eight digits and its control letter is the
     *         right one, false otherwise (null included)
     */
    public static boolean isValid(String nif) {
        if (nif == null || nif.length() != DIGITS + 1) {
            return false;
        }
        String number = nif.substring(0, DIGITS);
        for (int i = 0; i < DIGITS; i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        char expected = CONTROL_LETTERS.charAt(Integer.parseInt(number) % CONTROL_LETTERS.length());
        return Character.toUpperCase(nif.charAt(DIGITS)) == expected;
    }
}
