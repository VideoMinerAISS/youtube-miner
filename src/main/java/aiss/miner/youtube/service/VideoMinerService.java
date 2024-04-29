package aiss.miner.youtube.service;

import aiss.miner.youtube.models.video.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class VideoMinerService {
    @Autowired
    RestTemplate restTemplate;

    String baseURI = "http://localhost:8080/videominer";
    public void createChannel(Channel channel)
    {
        HttpEntity<Channel> request = new HttpEntity<>(channel, null);
        String uri = baseURI + "/channels";

        ResponseEntity<Channel> response = restTemplate.exchange(uri, HttpMethod.POST, request, Channel.class);

        System.out.println(response.getBody());
    }
}
