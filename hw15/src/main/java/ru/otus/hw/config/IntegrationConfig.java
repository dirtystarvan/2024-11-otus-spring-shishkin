package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.CaseWorm;
import ru.otus.hw.model.Color;
import ru.otus.hw.model.Worm;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.random.RandomGenerator;

@Configuration
public class IntegrationConfig {
    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    @Bean
    public MessageChannelSpec<?, ?> inputChannel() {
        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> outputChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow metamorphosisFlow() {
        return IntegrationFlow.from("inputChannel")
                .split()
                .channel(MessageChannels.executor(Executors.newFixedThreadPool(5)))
                .<Worm, CaseWorm>transform(worm -> new CaseWorm(
                        worm.getWeight() * 2,
                        worm.getLength(),
                        Color.values()[new Random().nextInt(Color.values().length)])
                )
                .<CaseWorm, Butterfly>transform(caseWorm -> new Butterfly(
                        randomGenerator.nextInt(1, 10),
                        randomGenerator.nextInt(10, 100),
                        Color.values()[new Random().nextInt(Color.values().length)])
                )
                .<Butterfly>log(LoggingHandler.Level.INFO, "Butterfly flew away", Message::getPayload)
                .aggregate()
                .channel("outputChannel")
                .get();
    }
}
