package dasturlashuz.giybat.service;


import dasturlashuz.giybat.dto.RegistrationDTO;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.enums.GeneralStatus;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String Registration(RegistrationDTO dto) {


        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());

        if (optional.isPresent()) {
            ProfileEntity profile = optional.get();
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRepository.delete(profile);
                //send sms /// email
            } else {
                throw new AppBadException("User already exists");
            }
        }

        ProfileEntity profile = new ProfileEntity();
        profile.setUsername(dto.getUsername());
        profile.setName(dto.getName());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setStatus(GeneralStatus.IN_REGISTRATION);
        profile.setLocalDateTime(LocalDateTime.now());
        profile.setVisible(true);
        profileRepository.save(profile);//save

        return "success";
    }
}
