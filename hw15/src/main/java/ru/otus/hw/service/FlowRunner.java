package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlowRunner implements CommandLineRunner {

    private final MetamorphosisService metamorphosisService;

    @Override
    public void run(String... args) {
        metamorphosisService.startMetamorphosis();
    }

}
