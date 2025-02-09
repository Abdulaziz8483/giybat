package dasturlashuz.giybat.mapper.profile;

import java.util.List;
import java.util.Optional;

public interface ProfileShortInfoMapper {
    String getName();
    String getUsername();
    List<String> getRoles();
    Optional<String> getPhotoId();


}
