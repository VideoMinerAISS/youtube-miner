package aiss.miner.youtube.models.video;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import javax.annotation.processing.Generated;

/**
 * @author Juan C. Alonso
 */
@Entity
@Table(name = "Caption")
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

    public Caption(String id, String name, String language) {
        this.id = id;
        this.name = name;
        this.language = language;
    }

    @Override
    public String toString() {
        return "Caption{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
