package aiss.miner.youtube.models.video;
import aiss.miner.youtube.models.youtube.caption.YoutubeCaption;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

/**
 * @author Juan C. Alonso
 */
@Entity
@Table(name = "YoutubeCaption")
public class Caption {

    @Id
    @JsonProperty("id")
    private String id;

    @Column(name="name", nullable=false, length=10000)
    @JsonProperty("name")
    private String name;

    @Column(name="language", nullable=false, length=100)
    @JsonProperty("language")
    private String language;


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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Caption() {}

    public Caption(YoutubeCaption youtubeCaption){
        this.id = youtubeCaption.getId();
        this.name = youtubeCaption.getSnippet().getName();
        this.language = youtubeCaption.getSnippet().getLanguage();
    }

    public Caption(String id, String name, String language) {
        this.id = id;
        this.name = name;
        this.language = language;
    }

    @Override
    public String toString() {
        return "YoutubeCaption{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
