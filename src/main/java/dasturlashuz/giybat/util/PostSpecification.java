package dasturlashuz.giybat.util;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.entity.PostEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification {
    public static Specification<PostEntity> filterPosts(PostCreateDTO.PostFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDTO.profileId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("profileId"), filterDTO.profileId()));
            }
            if (filterDTO.publishedDateFrom() != null && filterDTO.publishedDateTo() != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), filterDTO.publishedDateFrom(), filterDTO.publishedDateTo()));
            }
            if (filterDTO.title() != null) {
                predicates.add(criteriaBuilder.equal(root.get("title"), filterDTO.title()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
