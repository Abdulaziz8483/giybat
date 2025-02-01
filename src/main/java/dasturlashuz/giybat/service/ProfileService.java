package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.StandardResponse;
import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.entity.ProfileRoleEntity;
import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.exceptions.*;
import dasturlashuz.giybat.mapper.profile.ProfileMapper;
import dasturlashuz.giybat.repository.ProfileRepository;
import dasturlashuz.giybat.util.profile.ProfileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dasturlashuz.giybat.util.profile.ProfileUtil.checkPasswordValid;

@Service
@RequiredArgsConstructor
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;

    @Qualifier("profileMapper")
    private final ProfileMapper profileMapper;


    public ProfileEntity getbyId(Integer id) {
      return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
          throw new AppBadException("Profile not found");
      });
    }

    public ProfileCreateDTO.ProfileResponse create(ProfileCreateDTO profileDTO) {
        StandardResponse standardResponse = new StandardResponse("Username kiritilmadi", false);

        if (profileDTO.username() != null){
            standardResponse = checkUsernameExists(profileDTO.username());
            if (!standardResponse.status()) throw new EmailOrPhoneAlreadyExistsException(standardResponse.message());
        }
        //static import
        if (!checkPasswordValid(profileDTO.password()).status()) throw new InvalidPasswordException(checkPasswordValid(profileDTO.password()).message());


        ProfileEntity profile = new ProfileEntity();
        profile.setName(profileDTO.name());
        profile.setUsername(profileDTO.username());
        profile.setStatus(profileDTO.status());
        profileRoleService.createProfileRole(profile.getId(), profileDTO.role());
        profile.setVisible(true);
        profile.setCreatedDate(LocalDateTime.now());
        profile.setPassword(passwordEncoder.encode(profileDTO.password()));
        profileRepository.save(profile);
        return profileMapper.profileToProfileDTO(profile);
    }





    public List<ProfileCreateDTO.ProfileResponse> getAll() {
        List<ProfileCreateDTO.ProfileResponse> profiles = new ArrayList<>();
        for (ProfileEntity profile : profileRepository.findAll()) {
            profiles.add(profileMapper.profileToProfileDTO(profile));
        }
        return profiles;
    }

    public ProfileCreateDTO.ProfileResponse update(Integer profileId, ProfileCreateDTO profileDTO) {
        StandardResponse standardResponse = new StandardResponse("Username kiritilmadi", false);
        Optional<ProfileEntity> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (profileDTO.username() != null){
            standardResponse = checkUsernameExists(profileDTO.username());
            if (!standardResponse.status()) throw new EmailOrPhoneAlreadyExistsException(standardResponse.message());
        }

        //static import
        if (!checkPasswordValid(profileDTO.password()).status()) throw new InvalidPasswordException(checkPasswordValid(profileDTO.password()).message());

        ProfileEntity profile = optionalProfile.get();
        profile.setName(profileDTO.name());
        profile.setUsername(profileDTO.username());
        profile.setStatus(profileDTO.status());
        profile.setVisible(true);
        profile.setPassword(passwordEncoder.encode(profileDTO.password()));
        profileRepository.save(profile);
        return profileMapper.profileToProfileDTO(profile);

    }

    public String delete(Integer profileId) {
        profileRepository.findById(profileId).ifPresent(profile -> {
            profileRepository.delete(profile);
        });
        return "Profile deleted";
    }



    public StandardResponse checkUsernameExists(String email) {
        boolean exists = profileRepository.existsByUsernameAndVisibleTrue(email);
        if (exists) {
            return new StandardResponse("This username already exists", false);
        }
        return new StandardResponse("username", true);
    }
}
