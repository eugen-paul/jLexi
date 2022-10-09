package net.eugenpaul.jlexi.command;

public interface Command<D> {
    public boolean isEmpty();

    public void execute();

    public void unexecute();

    public boolean reversible();

    public D getData();
}
