package com.app.jueee.concurrency.chapter03.command;

public class ErrorCommand extends Command{

    public ErrorCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        return "Unknown command: "+command[0];
    }

    
    
}
