package aiss.miner.youtube.service;


import aiss.miner.youtube.models.youtube.caption.YoutubeCaption;
import aiss.miner.youtube.models.youtube.comment.YoutubeComment;
import aiss.miner.youtube.models.youtube.videoSnippet.VideoSnippet;
import aiss.miner.youtube.models.youtube.videoSnippet.VideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class VideosService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommentService commentService;

    @Autowired
    CaptionService captionService;

    private final String token = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc";

    private final String token2 = "AIzaSyAJJdRtvi7Jc_8nKFZoLXwHhVF7WhCKnX4";

    public List<VideoSnippet> searchChannelVideos(String channelId, Integer maxVideos, Integer maxComments){
        //MODIFICAR CON PAGINADO POR SI NUM VIDEOS MAYOR A 50.
        String uri = String
                .format("https://www.googleapis.com/youtube/v3/search?key=%s&part=snippet&type=video&channelId=%s&maxResults=%d",
                token2,channelId,Math.min(50,maxVideos));
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
        VideoSnippetSearch videoSearch = videoResponse.getBody();

        Integer videosRest = maxVideos-50;

        if(videosRest>0) videos.addAll(nextPagesV(channelId, videoSearch, videosRest, request));

        Function<VideoSnippet,List<YoutubeComment>> getComments = video ->
                commentService.getVideoComments(video.getId().getVideoId(), maxComments);

        Function<VideoSnippet,List<YoutubeCaption>> getCaptions = video ->
                captionService.getVideoCaptions(video.getId().getVideoId());

        videos.forEach(video -> video.setComments(new ArrayList<>(getComments.apply(video))));
        videos.forEach(video -> video.setCaptions(new ArrayList<>(getCaptions.apply(video))));

        return videos;
    }

    private List<VideoSnippet>  nextPagesV(String channelId, VideoSnippetSearch videoSearch, Integer videosRest,
            HttpEntity<VideoSnippet> request){
        List<VideoSnippet> videos = new ArrayList<>();
        while (videoSearch.getNextPageToken() != null && !videoSearch.getNextPageToken().isEmpty() && videosRest>0) {
            Integer nResults = Math.min(50, videosRest);
            videosRest-=50;
            String uriNext = String
                    .format("https://www.googleapis.com/youtube/v3/search?key=%s&part=snippet&type=video&channelId=%s&maxResults=%d&pageToken=%s",
                            token2,channelId,nResults,videoSearch.getNextPageToken());
            ResponseEntity<VideoSnippetSearch> videoResponseNext =  restTemplate.exchange(uriNext, HttpMethod.GET,request,
                    VideoSnippetSearch.class);
            videoSearch = videoResponseNext.getBody();
            videos.addAll(videoSearch.getItems());
        }
        return videos;
    }
}
