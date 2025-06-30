package it.trenical.frontend.cli.exceptions;

public class BadCommandSyntaxException extends Exception {
    public BadCommandSyntaxException(String syntax) {
        super("Usage: " + syntax);
    }
}
