package aiss.miner.youtube.models.video;

import aiss.miner.youtube.models.youtube.comment.YoutubeComment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Entity
public class User {

    /*
    * In order to avoid making the model unnecessarily complex, we establish a one-to-one relationship between YoutubeComment and
    * User (instead of many-to-one). This causes an exception if we try to add a YoutubeComment to the DataBase that has been
    * created by a User that already has a YoutubeComment in a previously stored Video. To avoid this exception, we automatically
    * assign an id to each new User with AutoIncrement.
     */

    @JsonProperty("name")
    private String name;

    @JsonProperty("user_link")
    private String user_link;

    @JsonProperty("picture_link")
    private String picture_link;

    public User() {
    }

    public User(YoutubeComment youtubeComment){
        //TODO: Decodificar el id del canal
        this.name = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName();
        this.picture_link = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl();
        this.user_link = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getAuthorChannelUrl();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_link() {
        return user_link;
    }

    public void setUser_link(String user_link) {
        this.user_link = user_link;
    }

    public String getPicture_link() {
        return picture_link;
    }

    public void setPicture_link(String picture_link) {
        this.picture_link = picture_link;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", user_link='" + user_link + '\'' +
                ", picture_link='" + picture_link + '\'' +
                '}';
    }

}
