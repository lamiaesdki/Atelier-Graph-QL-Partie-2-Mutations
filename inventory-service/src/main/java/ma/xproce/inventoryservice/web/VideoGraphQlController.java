package ma.xproce.inventoryservice.web;

import ma.xproce.inventoryservice.dao.entities.Creator;
import ma.xproce.inventoryservice.dao.entities.Video;
import ma.xproce.inventoryservice.dao.interfaces.CreatorRepository;
import ma.xproce.inventoryservice.dao.interfaces.VideoRepository;
import ma.xproce.inventoryservice.dtos.CreatorRequest;
import ma.xproce.inventoryservice.dtos.VideoRequest;
import ma.xproce.inventoryservice.mappers.CreatorMapper;
import ma.xproce.inventoryservice.mappers.VideoMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class VideoGraphQlController {

    private final CreatorRepository creatorRepository;
    private final VideoRepository videoRepository;
    private final CreatorMapper creatorMapper;
    private final VideoMapper videoMapper;

    public VideoGraphQlController(CreatorRepository creatorRepository,
                                  VideoRepository videoRepository,
                                  CreatorMapper creatorMapper,
                                  VideoMapper videoMapper) {
        this.creatorRepository = creatorRepository;
        this.videoRepository = videoRepository;
        this.creatorMapper = creatorMapper;
        this.videoMapper = videoMapper;
    }




    @MutationMapping
    public Video saveVideo(@Argument VideoRequest videoRequest) {
        // Find the creator by email, or create a new one if not found
        Creator creator = creatorRepository.findByEmail(videoRequest.getCreator().getEmail())
                .orElseGet(() -> creatorRepository.save(
                        Creator.builder()
                                .name(videoRequest.getCreator().getName())
                                .email(videoRequest.getCreator().getEmail())
                                .build()
                ));

        // Parse the date (assumes datePublication is in "dd/MM/yyyy" format)
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(videoRequest.getDatePublication());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and save the video (we ignore description as it is not part of the Video entity)
        Video video = Video.builder()
                .name(videoRequest.getName())
                .url(videoRequest.getUrl())
                .datePublication(date)  // Set the parsed date
                .creator(creator)
                .build();

        return videoRepository.save(video);
    }


    @MutationMapping
    public Creator saveCreator(@Argument CreatorRequest creatorRequest) {
        // Map DTO to entity and save
        Creator creator = creatorMapper.fromCreatorRequestToCreator(creatorRequest);
        return creatorRepository.save(creator);
    }
    @QueryMapping
    public List<Video> videoList() {
        return videoRepository.findAll(); // This will fetch all videos, including creators if fetch is EAGER
    }
}
