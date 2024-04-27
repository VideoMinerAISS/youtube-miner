package aiss.miner.youtube.service;


import aiss.miner.youtube.models.caption.Caption;
import aiss.miner.youtube.models.comment.Comment;
import aiss.miner.youtube.models.videoSnippet.VideoSnippet;
import aiss.miner.youtube.models.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VideosService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommentService commentService;

    @Autowired
    CaptionService captionService;

    private final String token = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc";

    public List<VideoSnippet> searchChannelVideos(String channelId, Integer maxVideos, Integer maxComments){
        //MODIFICAR CON PAGINADO POR SI NUM VIDEOS MAYOR A 500.
        String uri = String
                .format("https://www.googleapis.com/youtube/v3/search?key=%s&part=snippet&type=video&channelId=%s&maxResults=%o",
                token,channelId,maxVideos);
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<VideoSnippet> request = new HttpEntity<>(null,headers);
        ResponseEntity<VideoSnippetSearch> videoResponse =  restTemplate.exchange(uri, HttpMethod.GET,request,
                VideoSnippetSearch.class);

        if(videoResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
            videoResponse.getBody() == null || videoResponse.getBody().getItems().isEmpty()){
            return null;
        }
        List<VideoSnippet> videos = videoResponse.getBody().getItems();
        Function<VideoSnippet,List<Comment>> getComments = video ->
                commentService.getVideoComments(video.getId().getVideoId(), maxComments);

        Function<VideoSnippet,List<Caption>> getCaptions = video ->
                captionService.getVideoCaptions(video.getId().getVideoId());

        videos.forEach(video -> video.setComments(new ArrayList<>(getComments.apply(video))));
        videos.forEach(video -> video.setCaptions(new ArrayList<>(getCaptions.apply(video))));

        return videos;
    }
}
