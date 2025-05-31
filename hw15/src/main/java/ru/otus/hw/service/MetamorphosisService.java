package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.integration.EntryGateway;
import ru.otus.hw.model.Color;
import ru.otus.hw.model.Worm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetamorphosisService {

    private final EntryGateway entryGateway;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public void startMetamorphosis() {
        var worms = generateWorms();

        var butterflies = entryGateway.process(worms);

        log.info("Finish metamorphosis. Butterflies: {}", butterflies.size());
    }

    private List<Worm> generateWorms() {
        int wormCount = randomGenerator.nextInt(10, 100);
        log.info("Start generate {} worms.", wormCount);

        return ForkJoinPool.commonPool().invoke(new GenerateWormsTask(wormCount));
    }

    private Worm generateSingleWorm() {
        int weight = randomGenerator.nextInt(5, 10);
        int length = randomGenerator.nextInt(20, 30);
        Color color = Color.values()[new Random().nextInt(Color.values().length)];

        return new Worm(weight, length, color);
    }

    private class GenerateWormsTask extends RecursiveTask<List<Worm>> {
        private final int wormsCounter;

        public GenerateWormsTask(int wormsCounter) {
            super();
            this.wormsCounter = wormsCounter;
        }

        @Override
        protected List<Worm> compute() {
            if (wormsCounter <= 2) {
                log.info("Generate worms: {}", wormsCounter);
                return Stream.generate(MetamorphosisService.this::generateSingleWorm)
                        .limit(wormsCounter)
                        .collect(Collectors.toList());
            }

            var firstHalfCounter = divideCounter(wormsCounter);
            var secondHalfCounter = wormsCounter - firstHalfCounter;
            log.info("Generate subtasks: {} -> {}-{}", wormsCounter, firstHalfCounter, secondHalfCounter);

            var firstHalfWormsTask = new GenerateWormsTask(firstHalfCounter);
            var secondHalfWormsTask = new GenerateWormsTask(secondHalfCounter);

            firstHalfWormsTask.fork();
            secondHalfWormsTask.fork();

            var result = new ArrayList<Worm>(wormsCounter);
            result.addAll(firstHalfWormsTask.join());
            result.addAll(secondHalfWormsTask.join());

            return result;
        }

        private int divideCounter(int wormsCounter) {
            return wormsCounter % 2 == 0
                    ? wormsCounter / 2
                    : wormsCounter / 2 + 1;
        }
    }

}
