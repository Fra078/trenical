package it.trenical.frontend.cli;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Cli {
    private final String title;
    private final Set<Command> commands = new TreeSet<>();
    private boolean active = false;
    private final Scanner scanner = new Scanner(System.in);

    public Cli(String title, Command...commands) {
        this.title = title;
        registerCommands(new ExitCommand(this::exit), new HelpCommand(this::printHints));
        registerCommands(commands);
    }

    protected void registerCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public void start() {
        active = true;
        System.out.println(title);
        System.out.println("Type help to see available commands.");
        c: while (active) {
            System.out.println();
            System.out.print("> ");
            String line = scanner.nextLine();
            if (!line.isBlank()){
                for (Command command : commands)
                    if (command.tryExecute(line.trim())) continue c;
                System.out.println("Comando non riconosciuto!");
            }
        }
    }

    private void printHints(){
        commands.stream()
                .sorted((c1, c2)-> c1.getName().compareToIgnoreCase(c2.getDescription()))
                .forEach(System.out::println);
    }

    protected void exit() {
        active = false;
    }
}
