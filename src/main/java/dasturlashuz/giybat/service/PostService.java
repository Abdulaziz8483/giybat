package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.entity.PostEntity;
import dasturlashuz.giybat.enums.PostStatus;
import dasturlashuz.giybat.mapper.post.PostMapper;
import dasturlashuz.giybat.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import static dasturlashuz.giybat.util.session.SpringSecurityUtil.getCurrentUserId;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AttachService attachService;
    @Qualifier("postMapper")
    private final PostMapper postMapper;

    public PostCreateDTO.PostResponse create(PostCreateDTO dto) {
        PostEntity post = new PostEntity();

        post.setProfileId(getCurrentUserId());
        post.setTitle(dto.title());
        post.setContent(dto.content());

        post.setCreatedAt(LocalDateTime.now());
        post.setStatus(PostStatus.PENDING);
        post.setVisible(true);
        post.setAttachId(dto.photoId());
        postRepository.save(post);

        return new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(dto.photoId()), dto.photoId(), post.getCreatedAt());
    }
}
