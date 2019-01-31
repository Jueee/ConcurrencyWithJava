package com.app.jueee.concurrency.chapter03.command;

import com.app.jueee.concurrency.chapter03.command.Command;

public class ReportCommand extends Command {

    public ReportCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        WDIDAO dao = WDIDAO.getDAO();
        return dao.report(command[0]);
    }

}
