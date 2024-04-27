package aiss.miner.youtube.service;

import aiss.miner.youtube.models.youtube.caption.Caption;
import aiss.miner.youtube.models.youtube.caption.CaptionSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CaptionService {

    @Autowired
    RestTemplate restTemplate;

    private final String token = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc"; //Emilio

    private final String token2 = "AIzaSyAJJdRtvi7Jc_8nKFZoLXwHhVF7WhCKnX4"; //Pepe
    public List<Caption> getVideoCaptions(String videoId){
        String uri = String
                .format("https://www.googleapis.com/youtube/v3/captions?part=id,snippet&key=%s&videoId=%s",
                        token2, videoId);
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<CaptionSearch> request = new HttpEntity<>(null, headers);

        ResponseEntity<CaptionSearch> captionResponse = restTemplate.exchange(uri, HttpMethod.GET, request, CaptionSearch.class);

        if(captionResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
                captionResponse.getBody() == null || captionResponse.getBody().getItems().isEmpty()){
            return null;
        }
        List<Caption> captions = captionResponse.getBody().getItems();
        return captions;
    }

}
