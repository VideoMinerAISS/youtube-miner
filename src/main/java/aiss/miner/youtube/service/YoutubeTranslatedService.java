package aiss.miner.youtube.service;


import aiss.miner.youtube.models.video.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YoutubeTranslatedService {
    @Autowired
    ChannelService channelService;

    //This is a wrapper for YoutubeService
    public Channel getYoutubeChannel(String channelId)
    {return getYoutubeChannel(channelId,10,10);}
    public Channel getYoutubeChannel(String channelId, Integer maxVideos, Integer maxComments)
    {
        return  new Channel(channelService.getChannelById(channelId,maxVideos,maxComments));
    }

}
