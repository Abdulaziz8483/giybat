package dasturlashuz.giybat.repository;

import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);
    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer profileId);
    boolean existsByUsernameAndVisibleTrue(String username);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status=?2 where id=?1")
    void changeStatus(Integer profileId, ProfileStatus status);
}
