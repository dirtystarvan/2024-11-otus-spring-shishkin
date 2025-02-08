package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommandService {
    private final TestRunnerService testRunner;

    @ShellMethod(value = "Start test program", key = {"s", "start"})
    public String start() {
        testRunner.run();

        return "Test program completed";
    }

}
