package aiss.miner.youtube.service;

import aiss.miner.youtube.models.youtube.caption.Caption;
import aiss.miner.youtube.models.youtube.channel.Channel;
import aiss.miner.youtube.models.youtube.comment.Comment;
import aiss.miner.youtube.models.youtube.videoSnippet.VideoSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class ChannelServiceTest {


    @Autowired
    ChannelService service;

    @Autowired
    VideosService videosService;

    @Autowired
    CommentService commentService;

    @Autowired
    CaptionService captionService;

    @Test
    @DisplayName("Get channel by Id")
    void getChannelById() {
        Channel channel = service.getChannelById("UCF3Ez6QwZwwr_E7RZGJMW0A");
        System.out.println(channel);
        System.out.println(channel.getVideos());
        System.out.println(channel.getVideos().get(0).getComments());
        System.out.println(channel.getVideos().get(0).getCaptions());
    }

    @Test
    @DisplayName("Get channel videos by Id")
    void searchChannelVideos() {
        List<VideoSnippet> videos =videosService.searchChannelVideos("UCF3Ez6QwZwwr_E7RZGJMW0A",
                10, 10);
        System.out.println(videos.size());
        System.out.println(videos);
        System.out.println(videos.get(0).getComments().size());
        System.out.println(videos.get(0).getComments());
        System.out.println(videos.get(0).getCaptions().size());
        System.out.println(videos.get(0).getCaptions());

    }

    @Test
    @DisplayName("Get video comments")
    void getVideoComments(){
        List<Comment> comments = commentService.getVideoComments("RWB-LKkedgA", 150);
        System.out.println(comments);
        System.out.println(comments.size());
    }

    @Test
    @DisplayName("Get a video captions")
    void getVideoCaptions(){
        List<Caption> captions = captionService.getVideoCaptions("RWB-LKkedgA");
        System.out.println(captions);
    }

}