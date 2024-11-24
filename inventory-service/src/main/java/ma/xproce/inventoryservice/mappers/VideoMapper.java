package ma.xproce.inventoryservice.mappers;

import ma.xproce.inventoryservice.dao.entities.Creator;
import ma.xproce.inventoryservice.dao.entities.Video;
import ma.xproce.inventoryservice.dtos.CreatorRequest;
import ma.xproce.inventoryservice.dtos.VideoRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class VideoMapper {

    private final ModelMapper modelMapper;

    public VideoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Video fromVideoRequestToVideo(VideoRequest videoRequest) {
        if (videoRequest == null) {
            return null;
        }

        // Parse the date from String to Date (assume the format is "dd/MM/yyyy")
        Date parsedDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            parsedDate = sdf.parse(videoRequest.getDatePublication());
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (you may throw an exception or return null here based on your needs)
        }

        return Video.builder()
                .name(videoRequest.getName())
                .url(videoRequest.getUrl())
                .description(videoRequest.getDescription())  // Description is optional
                .datePublication(parsedDate)  // Now assigning the parsed Date
                .creator(null)  // Creator will be set in the controller, don't need to set it here
                .build();
    }

    public VideoRequest fromVideoToVideoRequest(Video video) {
        return modelMapper.map(video, VideoRequest.class);
    }
}
