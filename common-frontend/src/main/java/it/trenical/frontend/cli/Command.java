package it.trenical.frontend.cli;

import io.grpc.StatusRuntimeException;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;

import java.util.Arrays;
import java.util.Objects;

public abstract class Command implements Comparable<Command> {

    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean tryExecute(String input) {
        String[] tokens = input.split(" ");
        String[] name = getName().split(" ");
        if (tokens.length < name.length) return false;
        for (int i = 0; i < name.length; i++)
            if (!tokens[i].equals(name[i])) return false;
        try {
            action(Arrays.copyOfRange(tokens, name.length, tokens.length));
        } catch (BadCommandSyntaxException exc){
            System.out.println(exc.getMessage());
        } catch (StatusRuntimeException exc){
            System.out.println("Errore GRP, status " + exc.getStatus().getCode() + ": " + exc.getStatus().getDescription());
        } catch (NumberFormatException exc){
            System.out.println(getSyntax());
        }
        return true;
    }

    public String getSyntax() {return name;}
    protected abstract void action(String[] args) throws BadCommandSyntaxException;

    @Override
    public int compareTo(Command o) {
        if (this.name.length() > o.name.length()) {
            return -1;
        } else if (this.name.length() < o.name.length()) {
            return 1;
        }
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return getSyntax() + " - " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Command command)) return false;
        return Objects.equals(name, command.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
