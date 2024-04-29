package aiss.miner.youtube.models.video;
import aiss.miner.youtube.models.youtube.comment.YoutubeComment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * @author Juan C. Alonso
 */
@Entity
@Table(name = "YoutubeComment")
public class Comment {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    @Column(columnDefinition="TEXT")
    private String text;

    @JsonProperty("createdOn")
    private String createdOn;

    @JsonProperty("author")
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "YoutubeComment author cannot be null")
    private User author;

    public Comment(){}

    public Comment(YoutubeComment youtubeComment){
        this.id = youtubeComment.getCommentSnippet().getTopLevelComment().getId();
        this.text = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getTextOriginal();
        this.createdOn = youtubeComment.getCommentSnippet().getTopLevelComment().getSnippet().getPublishedAt();
        this.author =new User(youtubeComment);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "YoutubeComment{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", author=" + author +
                '}';
    }


}
