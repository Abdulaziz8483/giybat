package dasturlashuz.giybat.repository;

import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.enums.ProfileStatus;
import dasturlashuz.giybat.mapper.profile.ProfileShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>, JpaSpecificationExecutor<ProfileEntity> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);
    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer profileId);
    boolean existsByUsernameAndVisibleTrue(String username);

    @Query(value = "SELECT p.name AS name, " +
            "p.user_name AS username, " +
            "p.attach_id AS photoId, " +
            "STRING_AGG(pr.roles, ',') AS roles " +  // Probellar to'g'ri qo'shildi
            "FROM profiles AS p " +  // profiles jadvali qo'shtirnoqsiz
            "JOIN profile_role pr ON pr.profile_id = p.id " +
            "WHERE p.id = ?1 " +
            "GROUP BY p.id, p.name, p.user_name, p.attach_id",
            nativeQuery = true)
    ProfileShortInfoMapper getProfileShortInfoMapper(Integer profileId);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status=?2 where id=?1")
    void changeStatus(Integer profileId, ProfileStatus status);


}
