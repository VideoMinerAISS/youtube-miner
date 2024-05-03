package aiss.miner.youtube.service;

import aiss.miner.youtube.exception.ResourceNotFound;
import aiss.miner.youtube.models.youtube.caption.YoutubeCaption;
import aiss.miner.youtube.models.youtube.channel.YoutubeChannel;
import aiss.miner.youtube.models.youtube.comment.YoutubeComment;
import aiss.miner.youtube.models.youtube.videoSnippet.VideoSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class YoutubeServiceTest {


    @Autowired
    YoutubeService service;

    @Test
    @DisplayName("Get channel by Id")
    void getChannelById() {
        YoutubeChannel channel = service.getChannelById("UCF3Ez6QwZwwr_E7RZGJMW0A",10,10);
        System.out.println(channel);
        System.out.println(channel.getVideos());
        System.out.println(channel.getVideos().get(0).getComments());
        System.out.println(channel.getVideos().get(0).getCaptions());
    }

    @Test
    @DisplayName("Get channel videos by Id")
    void searchChannelVideos() {
        List<VideoSnippet> videos = service.searchChannelVideos("UCF3Ez6QwZwwr_E7RZGJMW0A",
                10, 10);
        System.out.println(videos);
        System.out.println(videos.get(0).getComments());
        System.out.println(videos.get(0).getCaptions());

    }

    @Test
    @DisplayName("Get video comments")
    void getVideoComments(){
        List<YoutubeComment> comments = service.getVideoComments("RWB-LKkedgA", 100);
        System.out.println(comments);
    }

    @Test
    @DisplayName("Get a video captions")
    void getVideoCaptions(){
        List<YoutubeCaption> captions = service.getVideoCaptions("RWB-LKkedgA");
        System.out.println(captions);
    }


    @Test
    @DisplayName("Get channel by fake Id")
    void getChannelByIdFail() {
        try{
            YoutubeChannel channel = service.getChannelById("UCF3Ez6QwZwwr_&E7RZGJMW0A",10,10);
            System.out.println(channel);
            System.out.println(channel.getVideos());
            System.out.println(channel.getVideos().get(0).getComments());
            System.out.println(channel.getVideos().get(0).getCaptions());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Get comments from a video with comments disabled")
    void getVideoWithCommentsDisabled(){

        List<YoutubeComment> comments = service.getVideoComments("1q9j7vzv5Bg", 10);
        System.out.println(comments);

    }

    @Test
    @DisplayName("Get the captions from a video that doesn't exists")
    void getNonExistentVideoCaptions(){
        try{
            List<YoutubeCaption> captions = service.getVideoCaptions("VideoInexistenTest");
            System.out.println(captions);
        } catch (ResourceNotFound e) {
            System.out.println(e.getMessage());
        }


    }

}