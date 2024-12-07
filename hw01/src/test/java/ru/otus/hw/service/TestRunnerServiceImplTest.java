package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestRunnerServiceImplTest {

    @Mock
    private TestService testService;

    private TestRunnerServiceImpl testRunnerService;

    @BeforeEach
    void setUp() {
        testRunnerService = new TestRunnerServiceImpl(testService);
    }

    @Test
    void testRunnerExecuteTestServiceOnce() {
        testRunnerService.run();

        verify(testService, times(1)).executeTest();
    }

}