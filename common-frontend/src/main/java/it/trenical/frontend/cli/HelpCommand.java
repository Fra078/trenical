package it.trenical.frontend.cli;

public class HelpCommand extends Command {

    private final Runnable helpAction;

    public HelpCommand(Runnable helpAction) {
        super("help", "Stampa la lista dei comandi disponibili");
        this.helpAction = helpAction;
    }

    @Override
    protected void action(String[] args) {
        helpAction.run();
    }
}
