package aiss.miner.youtube.service;

import aiss.miner.youtube.models.comment.Comment;
import aiss.miner.youtube.models.comment.CommentSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    private final String token = "AIzaSyDeuzP9gYFLoPpNsdfSYAw9OE8z_9_0ndc";
    private final String token2 = "AIzaSyAJJdRtvi7Jc_8nKFZoLXwHhVF7WhCKnX4";

    private final String token3 = "AIzaSyB8ynH8cDaWHsA37QE2Hmq7QEDiI9KbQCs";

    public List<Comment> getVideoComments(String videoId, Integer maxComments) {
        //AÑADIR PAGINADO POR SI MAX COMENTARIOS MAYOR A 100.

        String uri = String
                .format("https://www.googleapis.com/youtube/v3/commentThreads?part=id,snippet&key=%s&videoId=%s&maxResults=%d",
                        token2,videoId,Math.min(50,maxComments));
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);
        HttpEntity<CommentSearch> request = new HttpEntity<>(null, headers);
        ResponseEntity<CommentSearch> commentResponse = restTemplate
                                                        .exchange(uri, HttpMethod.GET, request, CommentSearch.class);
        if(commentResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST) ||
                commentResponse.getBody() == null || commentResponse.getBody().getItems().isEmpty()){
            return null; //Quizas se usen para tratar excepciones
        }
        List<Comment> comments = commentResponse.getBody().getItems();
        CommentSearch commentSearch = commentResponse.getBody();
        Integer commentRest = maxComments-50;


        return comments;

    }


}
