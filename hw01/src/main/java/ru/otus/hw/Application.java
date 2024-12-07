package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    private static final String CONTEXT_FILE_PATH = "spring-context.xml";

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_FILE_PATH);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}