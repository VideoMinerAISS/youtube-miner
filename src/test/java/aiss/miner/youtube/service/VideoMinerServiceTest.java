package aiss.miner.youtube.service;

import aiss.miner.youtube.models.video.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoMinerServiceTest {

    @Autowired
    VideoMinerService videoMinerService;
    @Test
    @DisplayName("Create Channel on VideoMiner")
    void createChannel() {
        Channel channel = new Channel();
        channel.setId("1");
        channel.setCreatedTime("2024-22");
        channel.setName("Juan");
        channel.setVideos(new ArrayList<>());
        channel.setDescription("Un canal muy chulo");
        videoMinerService.createChannel(channel);
    }
}