package ma.xproce.inventoryservice.web;

import ma.xproce.inventoryservice.dao.entities.Creator;
import ma.xproce.inventoryservice.dao.entities.Video;
import ma.xproce.inventoryservice.dao.interfaces.CreatorRepository;
import ma.xproce.inventoryservice.dao.interfaces.VideoRepository;
import ma.xproce.inventoryservice.dtos.CreatorRequest;
import ma.xproce.inventoryservice.dtos.VideoRequest;
import ma.xproce.inventoryservice.exceptions.CreatorNotFoundException;
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
    private CreatorRepository creatorRepository;
    private VideoRepository videoRepository;
    private CreatorMapper creatorMapper;
    private VideoMapper videoMapper;



    VideoGraphQlController(CreatorRepository creatorRepository, VideoRepository videoRepository, CreatorMapper creatorMapper, VideoMapper videoMapper) {
        this.creatorRepository = creatorRepository;
        this.videoRepository = videoRepository;
        this.creatorMapper=creatorMapper;
        this.videoMapper = videoMapper;
    }

    @QueryMapping
    public List<Video> videoList() {
        return videoRepository.findAll();
    }

    @QueryMapping
    public Creator creatorById(@Argument Long id) {
        return creatorRepository.findById(id)
                .orElseThrow(() -> new CreatorNotFoundException(id));
    }

    @MutationMapping
    public Creator saveCreator(@Argument CreatorRequest creator) {
        Creator creator1 = Creator.builder().name(creator.getName()).email(creator.getEmail()).build();
        return creatorRepository.save(creator1);
//        return creatorRepository.save(creatorMapper.toCreator(creator)) ;
    }

    @MutationMapping
    public Video saveVideo(@Argument VideoRequest video) {
        // Check if the creator exists; if not, create and save them
        Creator creator = creatorRepository.findByEmail(video.getCreator().getEmail())
                .orElseGet(() -> {
                    // If creator doesn't exist, create and save the new creator
                    return creatorRepository.save(Creator.builder()
                            .name(video.getCreator().getName())
                            .email(video.getCreator().getEmail())
                            .build());
                });

        // Map the VideoRequest to a Video entity and associate the creator
        Video videoDAO = Video.builder()
                .name(video.getName())
                .url(video.getUrl())
                .creator(creator) // Associate the creator
                .description(video.getDescription())
                .datePublication(video.getDatePublication())
                .build();

        // Save and return the video
        return videoRepository.save(videoDAO);
    }
}
