package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.attach.AttachResponse;
import dasturlashuz.giybat.entity.AttachEntity;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.mapper.attach.AttachMapper;
import dasturlashuz.giybat.repository.AttachRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachService {
    private final AttachRepository attachRepository;

    @Value("${attach.upload.folder}")
    private String folderName;

    @Value("${attach.ulr}")
    private String attachUrl;

    @Qualifier("attachMapper")
    private final AttachMapper attachMapper;

    @Transactional
    public AttachResponse.AttachUrl upload(MultipartFile file) {
        String pathFolder = generateDataBaseFolder();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        String fullFilePath = saveAttach(file, pathFolder, key, extension);


        AttachEntity attach = createAttachEntity(file, key, extension, pathFolder);

        return new AttachResponse.AttachUrl(openUrl(attach.getId()));
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

    public List<AttachResponse> getAll() {
        List<AttachResponse> responses = new ArrayList<>();
        for (AttachEntity entity : attachRepository.findAll()) {
            responses.add(attachMapper.attachEntityToAttachResponse(entity));
        }
        return responses;
    }

    public String delete(String attachId) {
        Optional<AttachEntity> optionalAttach = attachRepository.findById(attachId);
        if (optionalAttach.isEmpty()) throw new AppBadException("Attach not found");
        attachRepository.delete(optionalAttach.get());
        return "Deleted attach from database";
    }

    public String openUrl(String fileName) {
        if(fileName == null){
            return null;
        }

        if (isExist(fileName)) {
            String url = attachUrl + "/open/" + fileName;
            return url;
        }
        return null;
    }


    private Boolean isExist(String attachId) {
        if (attachId == null) return false;

        boolean exist = attachRepository.existsByIdAndVisibleTrue(attachId);
        return exist;
    }

    public ResponseEntity<Resource> open(String attachId) {
        AttachEntity entity = getEntity(attachId);
        String path = folderName + "/" + entity.getPath() +"/"+ entity.getId();
        Path filePath = Paths.get(path).normalize();
        Resource resource = null;
        try{
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()){
                throw new AppBadException("File not found" +  entity.getId());
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public AttachEntity getEntity(String attachId) {
        Optional<AttachEntity> optionalAttach = attachRepository.findById(attachId);
        if (optionalAttach.isEmpty()) throw new AppBadException("Attach not found");
        return optionalAttach.get();
    }
}
