package ru.otus.hw.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.otus.hw.integration.EntryGateway;
import ru.otus.hw.model.Color;
import ru.otus.hw.model.Worm;
import ru.otus.hw.service.FlowRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IntegrationTest {

    @Autowired
    private EntryGateway entryGateway;

    @MockBean
    private FlowRunner flowRunner;


    @Test
    void integrationTest() {
        var redWorm = new Worm(1, 1, Color.RED);
        var greenWorm = new Worm(2, 2, Color.GREEN);
        var blueWorm = new Worm(3, 3, Color.BLUE);
        var worms = List.of(redWorm, greenWorm, blueWorm);

        var butterflies = entryGateway.process(worms);

        assertEquals(3, butterflies.size());
    }

}