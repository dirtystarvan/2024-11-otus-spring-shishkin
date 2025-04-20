package ru.otus.hw.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Worm;

import java.util.List;

@MessagingGateway
public interface EntryGateway {

    @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
    List<Butterfly> process(List<Worm> worms);

}
