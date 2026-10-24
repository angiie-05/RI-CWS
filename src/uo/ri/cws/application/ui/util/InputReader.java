package uo.ri.cws.application.ui.util;

import uo.ri.cws.application.service.util.NifValidator;
import uo.ri.util.console.Console;
import uo.ri.util.exception.UserInteractionChecks;
import uo.ri.util.exception.UserInteractionException;

/**
 * Reads data typed by the user and checks its format before it reaches the
 * service layer. A wrong value raises a UserInteractionException, that the
 * menu shows to the user without stopping the application.
 */
public class InputReader {

    private InputReader() {
    }

    /**
     * @param prompt message shown to the user
     * @return the typed value, that cannot be blank
     */
    public static String readNotBlank(String prompt) throws UserInteractionException {
        String value = Console.readString(prompt);
        UserInteractionChecks.isTrue(!value.isBlank(), prompt + " cannot be blank");
        return value;
    }

    /**
     * @param prompt message shown to the user
     * @return the typed nif, that must have eight digits and the right
     *         control letter
     */
    public static String readNif(String prompt) throws UserInteractionException {
        String nif = readNotBlank(prompt);
        UserInteractionChecks.isTrue(NifValidator.isValid(nif), nif + " is not a valid nif");
        return nif;
    }

    /**
     * @param prompt message shown to the user
     * @return the typed nif, or null if the user leaves it blank
     */
    public static String readOptionalNif(String prompt) throws UserInteractionException {
        String nif = Console.readString(prompt);
        if (nif.isBlank()) {
            return null;
        }
        UserInteractionChecks.isTrue(NifValidator.isValid(nif), nif + " is not a valid nif");
        return nif;
    }
}
