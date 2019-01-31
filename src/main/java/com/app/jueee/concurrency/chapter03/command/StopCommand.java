package com.app.jueee.concurrency.chapter03.command;

import com.app.jueee.concurrency.chapter03.command.Command;

public class StopCommand extends Command{

    public StopCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        return "Server stopped";
    }

    
    
}
