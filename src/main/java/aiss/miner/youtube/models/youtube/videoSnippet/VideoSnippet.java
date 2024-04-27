
package aiss.miner.youtube.models.youtube.videoSnippet;


import aiss.miner.youtube.models.youtube.caption.YoutubeCaption;
import aiss.miner.youtube.models.youtube.comment.YoutubeComment;
import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoSnippet {

    @JsonProperty("id")
    private VideoSnippetId id;
    @JsonProperty("snippet")
    private VideoSnippetDetails snippet;

    // These attributes have been manually added
    @JsonProperty("comments")
    private List<YoutubeComment> comments;

    @JsonProperty("captions")
    private List<YoutubeCaption> captions;

    public VideoSnippet() {
        this.comments = new ArrayList<>();
        this.captions = new ArrayList<>();
    }

    @JsonProperty("comments")
    public List<YoutubeComment> getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(List<YoutubeComment> comments) {
        this.comments = comments;
    }

    @JsonProperty("captions")
    public List<YoutubeCaption> getCaptions() { return captions; }

    @JsonProperty("captions")
    public void setCaptions(List<YoutubeCaption> captions) {
        this.captions = captions;
    }

    @JsonProperty("id")
    public VideoSnippetId getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(VideoSnippetId id) {
        this.id = id;
    }

    @JsonProperty("snippet")
    public VideoSnippetDetails getSnippet() {
        return snippet;
    }

    @JsonProperty("snippet")
    public void setSnippet(VideoSnippetDetails snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VideoSnippet.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("snippet");
        sb.append('=');
        sb.append(((this.snippet == null)?"<null>":this.snippet));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
