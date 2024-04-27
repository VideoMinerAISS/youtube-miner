package aiss.miner.youtube.controller;


import aiss.miner.youtube.service.VideoMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import aiss.miner.youtube.models.video.*;
@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    VideoMinerService videoMinerService;

    @GetMapping{"/{id}"}
    public Channel findOne(@PathVariable String id)
    {//TODO: create logic
        return  null;
    }

    @PostMapping("/{id}")

    public Channel postOne(@PathVariable String id)
    {//TODO: create logic
        Channel channel = null;
        videoMinerService.createChannel(channel);
        return  null;
    }
}
