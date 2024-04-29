package aiss.miner.youtube.models.video;

import aiss.miner.youtube.models.youtube.channel.YoutubeChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Juan C. Alonso
 */
@Entity
@Table(name = "YoutubeChannel")
public class Channel {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @NotEmpty(message = "YoutubeChannel name cannot be empty")
    private String name;

    @JsonProperty("description")
    @Column(columnDefinition="TEXT")
    private String description;

    @JsonProperty("createdTime")
    @NotEmpty(message = "YoutubeChannel creation time cannot be empty")
    private String createdTime;

    @JsonProperty("videos")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "channelId")
    @NotNull(message = "YoutubeChannel videos cannot be null")
    private List<Video> videos;

    public Channel() {

    }

    public Channel(YoutubeChannel youtubeChannel){
        this.id = youtubeChannel.getId();
        this.name = youtubeChannel.getSnippet().getTitle();
        this.description = youtubeChannel.getSnippet().getDescription();
        this.createdTime = youtubeChannel.getSnippet().getPublishedAt();
        this.videos = youtubeChannel.getVideos().stream().map(Video::new).collect(Collectors.toList());
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "YoutubeChannel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", videos=" + videos +
                '}';
    }
}
