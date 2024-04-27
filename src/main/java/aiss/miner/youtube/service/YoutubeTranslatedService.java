package aiss.miner.youtube.service;


import aiss.miner.youtube.models.video.Channel;
import org.springframework.stereotype.Service;

@Service
public class YoutubeTranslatedService {

    public Channel getYoutubeChannel(String channelId)
    {return getYoutubeChannel(channelId,10,10);}
    public Channel getYoutubeChannel(String channelId, Integer maxChannels, Integer maxComments)
    {
        //TODO: Emilio completa esta clases
        return  null;
    }

}
