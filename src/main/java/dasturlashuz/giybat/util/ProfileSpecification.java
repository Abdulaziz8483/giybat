package dasturlashuz.giybat.util;

import dasturlashuz.giybat.dto.post.PostCreateDTO;
import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.PostEntity;
import dasturlashuz.giybat.entity.ProfileEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {
    public static Specification<ProfileEntity> filterProfiles(ProfileCreateDTO.ProfileFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDTO.name() != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), "%" + filterDTO.name() + "%" ));
            }
            if (filterDTO.username() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user_name"), "%" + filterDTO.username() + "%" ));
            }
            if (filterDTO.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDTO.status()));
            }
            if (filterDTO.visible() != null) {
                predicates.add(criteriaBuilder.equal(root.get("visible"), filterDTO.visible()));
            }
            if (filterDTO.role() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), filterDTO.role()));
            }
            if (filterDTO.fromCreatedAt() != null && filterDTO.toCreatedAt() != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), filterDTO.fromCreatedAt(), filterDTO.toCreatedAt()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
