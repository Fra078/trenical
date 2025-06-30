package it.trenical.frontend.cli;


public class ExitCommand extends Command {

    private final Runnable exitAction;

    public ExitCommand(Runnable exitAction) {
        super("exit", "Chiude questo menu");
        this.exitAction = exitAction;
    }

    @Override
    protected void action(String[] args) {
        exitAction.run();
    }
}
