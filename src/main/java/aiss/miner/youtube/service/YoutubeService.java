package aiss.miner.youtube.service;

import aiss.miner.youtube.exception.ResourceNotFound;
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
import org.springframework.web.client.HttpClientErrorException;
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

        if(channelResponse.getBody().getItems() == null || channelResponse.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new ResourceNotFound("Channel");  //Pongo or por si acaso pero haciendo pruebas, aunque no exista la id no me devuelve 404
        }
        YoutubeChannel channel = channelResponse.getBody().getItems().get(0);
        List<VideoSnippet> videos = searchChannelVideos(channel.getId(),maxVideos, maxComments);
        channel.setVideos(new ArrayList<>(videos));

        return channel;
    }

    public List<VideoSnippet> searchChannelVideos(String channelId, Integer maxVideos, Integer maxComments){
        String uri = String
                .format("https://www.googleapis.com/youtube/v3/search?key=%s&part=snippet&type=video&channelId=%s&maxResults=%d",
                        token,channelId,Math.min(50,maxVideos));
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<VideoSnippet> request = new HttpEntity<>(null,headers);
        ResponseEntity<VideoSnippetSearch> videoResponse =  restTemplate.exchange(uri, HttpMethod.GET,request,
                VideoSnippetSearch.class);

        if(videoResponse.getBody().getItems().isEmpty()){return new ArrayList<>();}

        List<VideoSnippet> videos = videoResponse.getBody().getItems();
        VideoSnippetSearch videoSearch = videoResponse.getBody();

        Integer videosRest = maxVideos-50;

        if(videosRest>0) videos.addAll(nextPagesVideos(channelId, videoSearch, videosRest, request));

        Function<VideoSnippet,List<YoutubeComment>> getComments = video ->
                getVideoComments(video.getId().getVideoId(), maxComments);

        Function<VideoSnippet,List<YoutubeCaption>> getCaptions = video ->
                getVideoCaptions(video.getId().getVideoId());
        if(!videos.isEmpty()) {
            videos.forEach(video -> video.setComments(new ArrayList<>(getComments.apply(video))));
            videos.forEach(video -> video.setCaptions(new ArrayList<>(getCaptions.apply(video))));
        }
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
        List<YoutubeComment> comments = new ArrayList<>();
        String uri = String
                .format("https://www.googleapis.com/youtube/v3/commentThreads?part=id,snippet&key=%s&videoId=%s&maxResults=%d",
                        token,videoId,Math.min(100,maxComments));
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<CommentSearch> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<CommentSearch> commentResponse = restTemplate
                    .exchange(uri, HttpMethod.GET, request, CommentSearch.class);
            //Si no tiene comentarios, devuelve lista vacía
            if(commentResponse.getBody().getItems()!=null){
                comments = commentResponse.getBody().getItems();
                CommentSearch commentSearch = commentResponse.getBody();
                Integer commentRest = maxComments - 100;
                if (commentRest > 0) comments.addAll(getNextPagesComm(videoId, commentSearch, commentRest, request));
            }
        } catch (HttpClientErrorException.Forbidden | HttpClientErrorException.NotFound e){
            if(e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) throw new ResourceNotFound("Video");

        }
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
        List<YoutubeCaption> captions = new ArrayList<>();
        String uri = String
                .format("https://www.googleapis.com/youtube/v3/captions?part=id,snippet&key=%s&videoId=%s",
                        token, videoId);
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<CaptionSearch> request = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<CaptionSearch> captionResponse = restTemplate.exchange(uri, HttpMethod.GET, request, CaptionSearch.class);
            if(captionResponse.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND) || captionResponse.getBody().getItems()!=null)
                captions = captionResponse.getBody().getItems();

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFound("Video");
        }

        return captions;
    }

}
