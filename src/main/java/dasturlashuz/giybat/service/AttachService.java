package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.attach.AttachResponse;
import dasturlashuz.giybat.entity.AttachEntity;
import dasturlashuz.giybat.mapper.attach.AttachMapper;
import dasturlashuz.giybat.repository.AttachRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachService {
    private final AttachRepository attachRepository;

    @Value("${attach.upload.folder}")
    private String folderName;

    @Qualifier("attachMapper")
    private final AttachMapper attachMapper;

    @Transactional
    public AttachResponse upload(MultipartFile file) {
        String pathFolder = generateDataBaseFolder();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        String fullFilePath = saveAttach(file, pathFolder, key, extension);


        AttachEntity attach = createAttachEntity(file, key, extension, pathFolder);

        return attachMapper.attachEntityToAttachResponse(attach);
    }

    private AttachEntity createAttachEntity(MultipartFile file, String key, String extension, String pathFolder) {
        AttachEntity entity = new AttachEntity();
        entity.setId(key + "." + extension);
        entity.setPath(pathFolder);
        entity.setSize(file.getSize());
        entity.setType(file.getContentType());
        entity.setOriginName(file.getOriginalFilename());
        entity.setExtension(extension);
        entity.setVisible(true);
        entity.setCreatedAt(LocalDateTime.now());

        return attachRepository.save(entity);
    }

    private String saveAttach(MultipartFile file, String pathFolder, String key, String extension) {
        Path uploadDir = Path.of(folderName);
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fullFileName = key + "." + extension;
            Path fullPath = Paths.get(folderName + "/" + pathFolder + "/" + fullFileName);
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, file.getBytes());

            return fullPath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to save video file",e);
        }
    }


    private String generateDataBaseFolder() {
        Calendar cal = Calendar.getInstance();
        String folder = cal.get(Calendar.YEAR) + "/" +
                (cal.get(Calendar.MONTH) + 1) + "/" +
                cal.get(Calendar.DATE);

        return folder;
    }

    private String getExtension(String filename) {

        int lastIndex = filename.lastIndexOf(".");

        return filename.substring(lastIndex + 1);
    }
}
