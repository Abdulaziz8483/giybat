package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.entity.PostEntity;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.entity.ProfileRoleEntity;
import dasturlashuz.giybat.enums.PostStatus;
import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.mapper.post.PostMapper;
import dasturlashuz.giybat.repository.PostRepository;
import dasturlashuz.giybat.util.PostSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import static dasturlashuz.giybat.util.session.SpringSecurityUtil.getCurrentUserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AttachService attachService;
    @Qualifier("postMapper")
    private final PostMapper postMapper;
    @Lazy
    private final ProfileService profileService;

    public PostCreateDTO.PostResponse create(PostCreateDTO dto) {
        PostEntity post = new PostEntity();

        post.setProfileId(getCurrentUserId());
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setAttachId(dto.photoId());

        post.setCreatedAt(LocalDateTime.now());
        post.setStatus(PostStatus.PENDING);
        post.setVisible(true);

        postRepository.save(post);

        return new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(dto.photoId()), dto.photoId(), post.getCreatedAt());
    }

    public List<PostCreateDTO.PostResponse> getAll() {
        List<PostCreateDTO.PostResponse> postResponses = new ArrayList<>();
        for (PostEntity post : postRepository.findAll()){
            postResponses.add(new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(post.getAttachId()), post.getAttachId(), post.getCreatedAt()));
        }
        return postResponses;
    }

    public PostCreateDTO.PostResponse update(String postId, @Valid PostCreateDTO dto) {
        PostEntity post = checkPost(postId);
        if (!checkAdmin()) {
            if (post.getProfileId() != getCurrentUserId()){
               throw new AppBadException("You do not have permission to update this post");
            }
        }

        post.setProfileId(getCurrentUserId());
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setAttachId(dto.photoId());
        postRepository.save(post);

        return new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(dto.photoId()), dto.photoId(), post.getCreatedAt());
    }

    public String delete(String postId) {
        PostEntity post = checkPost(postId);
        if (!checkAdmin()) {
            if (post.getProfileId() == getCurrentUserId()){
                postRepository.delete(post);
                return "Post deleted";
            }
        }
        throw new AppBadException("For admins only");
    }

    public List<PostCreateDTO.PostResponse> getAllPostsOwner() {
        List<PostCreateDTO.PostResponse> postResponses = new ArrayList<>();
        for (PostEntity post : postRepository.findByProfileId(getCurrentUserId())) {
            postResponses.add(new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(post.getAttachId()), post.getAttachId(), post.getCreatedAt()));
        }
        return postResponses;
    }


    public PageImpl<PostCreateDTO.PostResponse> getAllPostsPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<PostCreateDTO.PostResponse> postResponses = new ArrayList<>();

        Page<PostEntity> postResponsesPage = postRepository.findByProfileId(getCurrentUserId(), pageRequest);
        for (PostEntity post : postResponsesPage) {
            postResponses.add(new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(post.getAttachId()), post.getAttachId(), post.getCreatedAt()));
        }
        return new PageImpl<>(postResponses, pageRequest, postResponsesPage.getTotalElements());
    }


    public PostCreateDTO.PostResponse getById(String postId) {
        PostEntity post = checkPost(postId);
        if (post.getStatus().equals(PostStatus.POSTED) || post.getProfileId() == getCurrentUserId() || checkAdmin()) {
            return new PostCreateDTO.PostResponse(post.getId(), post.getTitle(), post.getContent(), attachService.openUrl(post.getAttachId()), post.getAttachId(), post.getCreatedAt());
        }
        throw new AppBadException("Something went wrong");
    }


    public List<PostCreateDTO.PostFilterResponse> filter(PostCreateDTO.PostFilterDTO dto, int page, int size) {

        Page<PostEntity> postPage = getFilter(dto, page, size);
        List<PostCreateDTO.PostFilterResponse> postResponses = postPage.getContent().stream()
                .map(post -> {
                    ProfileEntity entity = profileService.findByIdForSession(post.getProfileId());
                    return new PostCreateDTO.PostFilterResponse(
                            post.getId(),
                            post.getTitle(),
                            post.getContent(),
                            post.getAttachId(),
                            attachService.openUrl(post.getAttachId()),
                            post.getCreatedAt(),
                            post.getProfileId(),
                            entity.getName(),
                            "PHOTO"
                    );
                })
                .toList();
        return postResponses;
    }



    //Util methods(Yordamchi database ga boglana oladigan methodlar)

    public PostEntity checkPost(String postId) {
        Optional<PostEntity> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new AppBadException("Post not found");
        }
        return optionalPost.get();
    }

    public boolean checkAdmin() {
        Integer currentUserId = getCurrentUserId();
        ProfileEntity currentProfile = profileService.findByIdForSession(currentUserId);
        for (ProfileRoleEntity profileRoleEntity : currentProfile.getRoles()) {
            if (profileRoleEntity.getRole().equals(ProfileRole.ADMIN)){
                return true;
            }
        }
        return false;
    }

    public Page<PostEntity> getFilter(PostCreateDTO.PostFilterDTO dto, int page, int size) {
        Specification<PostEntity> spec = PostSpecification.filterPosts(dto);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PostEntity> all = postRepository.findAll(spec, pageable);
        return all;
    }
}
