package dasturlashuz.giybat.service;


import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileEntity getbyId(Integer id) {
      return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
          throw new AppBadException("Profile not found");
      });
    }

    public ProfileCreateDTO.ProfileResponse create(ProfileCreateDTO profileDTO) {
        return null;
    }

    public List<ProfileCreateDTO.ProfileResponse> getAll() {
        return null;
    }

    public ProfileCreateDTO.ProfileResponse update(Long profileId, ProfileCreateDTO profileDTO) {
        return null;
    }

    public String delete(Long profileId) {
        return null;
    }
}
