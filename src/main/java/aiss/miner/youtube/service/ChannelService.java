package aiss.miner.youtube.service;

import aiss.miner.youtube.models.youtube.channel.Channel;
import aiss.miner.youtube.models.youtube.channel.ChannelSearch;
import aiss.miner.youtube.models.youtube.videoSnippet.VideoSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VideosService videosService;

    private final String token = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc";

    public Channel getChannelById(String id){
        return getChannelById(id, 10, 10);
    }

    public Channel getChannelById(String id, Integer maxComments, Integer maxVideos){
        String uri = String.format("https://www.googleapis.com/youtube/v3/channels?part=snippet&key=%s&id=%s",
                token,id);
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<ChannelSearch> request = new HttpEntity<>(null, headers);
        ResponseEntity<ChannelSearch> channelResponse = restTemplate.exchange(uri, HttpMethod.GET,request,ChannelSearch.class);

        if(channelResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
                channelResponse.getBody() == null || channelResponse.getBody().getItems().isEmpty()){
            return null; // PODRIAR CAUSAR EXCEPCION PERO MEJOR EN EL CONTROLLER O LUEGO AL RETORNAR LO HARE
        }
        Channel channel = channelResponse.getBody().getItems().get(0);
        List<VideoSnippet> videos = videosService.searchChannelVideos(channel.getId(),maxVideos, maxComments);
        channel.setVideos(new ArrayList<>(videos));

        return channel;
    }



}
