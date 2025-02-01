package dasturlashuz.giybat.service;


import dasturlashuz.giybat.entity.ProfileRoleEntity;
import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileRoleService {

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void createProfileRole(Integer profileId , ProfileRole profileRole) {
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(profileId);
        entity.setRoles(profileRole);
        entity.setCreatedDate(LocalDateTime.now());
        profileRoleRepository.save(entity);
    }

    public void delete(Integer profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
}
