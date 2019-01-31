package com.app.jueee.concurrency.chapter03.command;

import com.app.jueee.concurrency.chapter03.command.Command;

public class QueryCommand extends Command {

    public QueryCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        WDIDAO dao = WDIDAO.getDAO();
        if (command.length == 3) {
            return dao.query(command[1], command[2]);
        } else if (command.length == 4) {
            try {
                return dao.query(command[1], command[2], Short.parseShort(command[3]));
            } catch (Exception e) {
                return "ERROR;Bad Command";
            }
        } else {
            return "ERROR;Bad Command";
        }
    }

}
