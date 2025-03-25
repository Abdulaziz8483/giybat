package dasturlashuz.giybat.repository;

import dasturlashuz.giybat.entity.ProfileRoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity,Integer> {

    @Transactional
    @Modifying
    void deleteByProfileId(Integer profileId);

    @Query(value = "select STRING_AGG(pr.roles, ',') roles " +
            "from profile_role pr " +
            "where pr.profile_id = ?1 " +
            "group by pr.roles", nativeQuery = true)
    List<String> getRolesByProfileId(Integer profileId);

}
