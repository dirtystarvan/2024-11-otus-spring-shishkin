package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class MigrationCommands {

    private final Job jpaToMongoMigration;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Start migration", key = "sm")
    public void startMigration() throws Exception {
        jobLauncher.run(jpaToMongoMigration, new JobParameters());
    }

}
