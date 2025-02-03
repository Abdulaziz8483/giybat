package dasturlashuz.giybat.repository;


import dasturlashuz.giybat.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, String> {
}
