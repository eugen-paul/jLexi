package net.eugenpaul.jlexi.data.command;

public interface Command {
    public void execute();

    public void unexecute();

    public boolean reversible();
}
