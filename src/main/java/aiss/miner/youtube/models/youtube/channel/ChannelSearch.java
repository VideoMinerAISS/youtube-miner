
package aiss.miner.youtube.models.youtube.channel;

import java.util.List;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelSearch {

    @JsonProperty("items")
    private List<YoutubeChannel> items;

    @JsonProperty("items")
    public List<YoutubeChannel> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<YoutubeChannel> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChannelSearch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("items");
        sb.append('=');
        sb.append(((this.items == null)?"<null>":this.items));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
