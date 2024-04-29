package aiss.miner.youtube.service;

import aiss.miner.youtube.models.youtube.comment.YoutubeComment;
import aiss.miner.youtube.models.youtube.comment.CommentSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    private final String token = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc"; //Emilio

    private final String token2 = "AIzaSyAJJdRtvi7Jc_8nKFZoLXwHhVF7WhCKnX4"; //Pepe

    public List<YoutubeComment> getVideoComments(String videoId, Integer maxComments) {
        //AÑADIR PAGINADO POR SI MAX COMENTARIOS MAYOR A 100.

        String uri = String
                .format("https://www.googleapis.com/youtube/v3/commentThreads?part=id,snippet&key=%s&videoId=%s&maxResults=%d",
                        token,videoId,Math.min(100,maxComments));
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
                            token,videoId,Math.min(50,nResults), commentSearch.getNextPageToken());
            ResponseEntity<CommentSearch> commentResponse = restTemplate
                    .exchange(uriNext, HttpMethod.GET, request, CommentSearch.class);
            commentSearch = commentResponse.getBody();
            comments.addAll(commentSearch.getItems());
        }
        return comments;
    }




}
