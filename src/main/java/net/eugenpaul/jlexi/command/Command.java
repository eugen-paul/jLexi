package net.eugenpaul.jlexi.command;

public interface Command {
    public boolean isEmpty();

    public void execute();

    public void unexecute();

    public boolean reversible();
}
