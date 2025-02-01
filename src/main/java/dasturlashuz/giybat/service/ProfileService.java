package dasturlashuz.giybat.service;


import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileEntity getbyId(Integer id) {
      return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
          throw new AppBadException("Profile not found");
      });
    }
}
