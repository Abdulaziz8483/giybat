package dasturlashuz.giybat.repository;


import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, String>, JpaSpecificationExecutor<PostEntity> {

    List<PostEntity> findByProfileId(Integer profileId);
    Page<PostEntity> findByProfileId(Integer profileId, Pageable pageable);
    Integer countByProfileId(Integer profileId);
}
