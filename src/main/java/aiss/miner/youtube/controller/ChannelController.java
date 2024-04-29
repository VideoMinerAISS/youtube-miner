package aiss.miner.youtube.controller;


import aiss.miner.youtube.service.VideoMinerService;
import aiss.miner.youtube.service.YoutubeTranslatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import aiss.miner.youtube.models.video.*;
@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    VideoMinerService videoMinerService;
    @Autowired
    YoutubeTranslatedService youtubeTranslatedService;

    @GetMapping("/{id}")
    public Channel findOne(@PathVariable String id)
    {
        return youtubeTranslatedService.getYoutubeChannel(id);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel postOne(@PathVariable String id)
    {
System.out.println("AAAAAAA");
        Channel channel = youtubeTranslatedService.getYoutubeChannel(id);
        System.out.println("AAAAAAA");
        videoMinerService.createChannel(channel);
        System.out.println("AAAAAAA");
        return channel;
    }
}
