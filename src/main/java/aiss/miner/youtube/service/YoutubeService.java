package aiss.miner.youtube.service;

import aiss.miner.youtube.models.youtube.caption.CaptionSearch;
import aiss.miner.youtube.models.youtube.caption.YoutubeCaption;
import aiss.miner.youtube.models.youtube.channel.YoutubeChannel;
import aiss.miner.youtube.models.youtube.channel.ChannelSearch;
import aiss.miner.youtube.models.youtube.comment.CommentSearch;
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
public class YoutubeService {
    @Autowired
    RestTemplate restTemplate;

    private final String token2 = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc"; //Emilio

    private final String token = "AIzaSyAJJdRtvi7Jc_8nKFZoLXwHhVF7WhCKnX4"; //Pepe

    public YoutubeChannel getChannelById(String id){
        return getChannelById(id, 10, 10);
    }

    public YoutubeChannel getChannelById(String id, Integer maxVideos, Integer maxComments){
        String uri = String.format("https://www.googleapis.com/youtube/v3/channels?part=snippet&key=%s&id=%s",
                token,id);
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<ChannelSearch> request = new HttpEntity<>(null, headers);
        ResponseEntity<ChannelSearch> channelResponse = restTemplate.exchange(uri, HttpMethod.GET,request,ChannelSearch.class);

        if(channelResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
                channelResponse.getBody() == null || channelResponse.getBody().getItems().isEmpty()){
            return null; // PODRIAR CAUSAR EXCEPCION PERO MEJOR EN EL CONTROLLER
        }
        YoutubeChannel channel = channelResponse.getBody().getItems().get(0);
        List<VideoSnippet> videos = searchChannelVideos(channel.getId(),maxVideos, maxComments);
        channel.setVideos(new ArrayList<>(videos));

        return channel;
    }

    public List<VideoSnippet> searchChannelVideos(String channelId, Integer maxVideos, Integer maxComments){
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

        if(videosRest>0) videos.addAll(nextPagesVideos(channelId, videoSearch, videosRest, request));

        Function<VideoSnippet,List<YoutubeComment>> getComments = video ->
                getVideoComments(video.getId().getVideoId(), maxComments);

        Function<VideoSnippet,List<YoutubeCaption>> getCaptions = video ->
                getVideoCaptions(video.getId().getVideoId());

        videos.forEach(video -> video.setComments(new ArrayList<>(getComments.apply(video))));
        videos.forEach(video -> video.setCaptions(new ArrayList<>(getCaptions.apply(video))));

        return videos;
    }

    private List<VideoSnippet>  nextPagesVideos(String channelId, VideoSnippetSearch videoSearch, Integer videosRest,
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

    public List<YoutubeComment> getVideoComments(String videoId, Integer maxComments) {

        String uri = String
                .format("https://www.googleapis.com/youtube/v3/commentThreads?part=id,snippet&key=%s&videoId=%s&maxResults=%d",
                        token2,videoId,Math.min(100,maxComments));
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<CommentSearch> request = new HttpEntity<>(null, headers);
        ResponseEntity<CommentSearch> commentResponse = restTemplate
                .exchange(uri, HttpMethod.GET, request, CommentSearch.class);
        if(commentResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
                commentResponse.getBody() == null || commentResponse.getBody().getItems().isEmpty()){
            return null; //Quizas se usen para tratar excepciones
        }
        List<YoutubeComment> comments = commentResponse.getBody().getItems();
        CommentSearch commentSearch = commentResponse.getBody();
        Integer commentRest = maxComments-100;
        if(commentRest>0) comments.addAll(getNextPagesComm(videoId,commentSearch,commentRest,request));

        return comments;

    }

    private List<YoutubeComment> getNextPagesComm(String videoId, CommentSearch commentSearch, Integer commentRest, HttpEntity<CommentSearch> request){
        List<YoutubeComment> comments = new ArrayList<>();
        while(commentSearch.getNextPageToken() != null && !commentSearch.getNextPageToken().isEmpty() && commentRest>0){
            Integer nResults = Math.min(100, commentRest);
            commentRest-=100;
            String uriNext = String
                    .format("https://www.googleapis.com/youtube/v3/commentThreads?part=id,snippet&key=%s&videoId=%s&maxResults=%d&pageToken=%s",
                            token2,videoId,Math.min(50,nResults), commentSearch.getNextPageToken());
            ResponseEntity<CommentSearch> commentResponse = restTemplate
                    .exchange(uriNext, HttpMethod.GET, request, CommentSearch.class);
            commentSearch = commentResponse.getBody();
            if(commentSearch!=null) comments.addAll(commentSearch.getItems());
            else break;
        }
        return comments;
    }

    public List<YoutubeCaption> getVideoCaptions(String videoId){
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
        List<YoutubeCaption> captions = captionResponse.getBody().getItems();
        return captions;
    }

}
