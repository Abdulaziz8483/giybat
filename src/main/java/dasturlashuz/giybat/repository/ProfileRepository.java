package dasturlashuz.giybat.repository;

import dasturlashuz.giybat.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);

}
