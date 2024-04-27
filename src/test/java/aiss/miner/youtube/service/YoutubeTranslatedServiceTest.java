package aiss.miner.youtube.service;

import aiss.miner.youtube.models.video.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YoutubeTranslatedServiceTest {


    @Autowired
    YoutubeTranslatedService youtubeTranslatedService;

    @ParameterizedTest
    @DisplayName("Get Translated Youtube Channel from id")
    @ValueSource(strings = {"UCF3Ez6QwZwwr_E7RZGJMW0A"})
    void getYoutubeChannel(String channelId) {
        Channel channel = youtubeTranslatedService.getYoutubeChannel(channelId);
        System.out.println(channel);
    }
}