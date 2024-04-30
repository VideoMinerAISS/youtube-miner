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
    public Channel findOne(@PathVariable String id,
                           @RequestParam(value="maxVideos", defaultValue ="10") String maxVideos,
                           @RequestParam(value="maxComments", defaultValue ="10") String maxComments)
    {
        return youtubeTranslatedService.getYoutubeChannel(id, Integer.parseInt(maxVideos), Integer.parseInt(maxComments));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel postOne(@PathVariable String id,
                           @RequestParam(value="maxVideos", defaultValue ="10") String maxVideos,
                           @RequestParam(value="maxComments", defaultValue ="10") String maxComments)
    {
        Channel channel = youtubeTranslatedService.getYoutubeChannel(id, Integer.parseInt(maxVideos), Integer.parseInt(maxComments));
        videoMinerService.createChannel(channel);
        return channel;
    }
}
