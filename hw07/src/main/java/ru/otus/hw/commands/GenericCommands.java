package ru.otus.hw.commands;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@ShellComponent
public class GenericCommands {
    @ShellMethod(value = "Launch H2 console", key = "h2")
    public void launchConsole() throws SQLException {
        Console.main();
    }
}
