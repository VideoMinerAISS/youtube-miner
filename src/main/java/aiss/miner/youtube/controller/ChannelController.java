package aiss.miner.youtube.controller;


import aiss.miner.youtube.service.VideoMinerService;
import aiss.miner.youtube.service.YoutubeTranslatedService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import aiss.miner.youtube.models.video.*;
@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    VideoMinerService videoMinerService;
    @Autowired
    YoutubeTranslatedService youtubeTranslatedService;

    @Operation(
            summary="Retrive a channel",
            description="Get an existing Channel given an id.",
            tags={"channel"}
    )
    @ApiResponses({ @ApiResponse(
            responseCode="200",
            content={@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Channel.class)),
                    mediaType = "application/json"
            )}
    ),
            @ApiResponse(
                    responseCode="404",
                    content={@Content(
                            array = @ArraySchema(schema = @Schema())
                    )}
            ),
            @ApiResponse(
                    responseCode="403",
                    content={@Content(
                            array = @ArraySchema(schema = @Schema())
                    )}
            ),
            @ApiResponse(
                    responseCode="429",
                    content={@Content(
                            array = @ArraySchema(schema = @Schema())
                    )}
            )})
    @GetMapping("/{id}")
    public Channel findOne(@PathVariable String id,
                           @RequestParam(value="maxVideos", defaultValue ="10") @Min(0) @Max(20) String maxVideos,
                           @RequestParam(value="maxComments", defaultValue ="10") @Min(0) @Max(20) String maxComments)
    {
        return youtubeTranslatedService.getYoutubeChannel(id, Integer.parseInt(maxVideos), Integer.parseInt(maxComments));
    }

    @Operation(
            summary="Retrieve a channel and sends it",
            description="Get an existing Channel given an id and send it to VideoMiner.",
            tags={"channel"}
    )
    @ApiResponses({ @ApiResponse(
            responseCode="200",
            content={@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Channel.class)),
                    mediaType = "application/json"
            )}
    ),
            @ApiResponse(
                    responseCode="404",
                    content={@Content(
                            array = @ArraySchema(schema = @Schema())
                    )}
            ),
            @ApiResponse(
                    responseCode="403",
                    content={@Content(
                            array = @ArraySchema(schema = @Schema())
                    )}
            ),
            @ApiResponse(
                    responseCode="429",
                    content={@Content(
                            array = @ArraySchema(schema = @Schema())
                    )}
            )})
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Channel postOne(@PathVariable String id,
                           @RequestParam(value="maxVideos", defaultValue ="10") @Min(0) @Max(20) String maxVideos,
                           @RequestParam(value="maxComments", defaultValue ="10") @Min(0) @Max(20) String maxComments)
    {
        Channel channel = youtubeTranslatedService.getYoutubeChannel(id, Integer.parseInt(maxVideos), Integer.parseInt(maxComments));
        videoMinerService.createChannel(channel);
        return channel;
    }
}
