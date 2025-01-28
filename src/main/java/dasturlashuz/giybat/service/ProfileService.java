package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.StandardResponse;
import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.Profile;
import dasturlashuz.giybat.exceptions.EmailOrPhoneAlreadyExistsException;
import dasturlashuz.giybat.exceptions.InvalidPasswordException;
import dasturlashuz.giybat.exceptions.MissingRequiredFieldsException;
import dasturlashuz.giybat.exceptions.UserNotFoundException;
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
    private ProfileRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Qualifier("profileMapper")
    private final ProfileMapper profileMapper;

    public ProfileCreateDTO.ProfileResponse create(ProfileCreateDTO profileDTO) {
        StandardResponse standardResponse = new StandardResponse("Email yoki Phone kiritilmadi", false);
        if (profileDTO.email() == null && profileDTO.phone() == null) {
            throw new MissingRequiredFieldsException(standardResponse.message());
        }
        if (profileDTO.email() != null){
            standardResponse = checkEmailExists(profileDTO.email());
            if (!standardResponse.status()) throw new EmailOrPhoneAlreadyExistsException(standardResponse.message());
        }
        if (profileDTO.phone() != null){
            standardResponse = checkPhoneExists(profileDTO.phone());
            if (!standardResponse.status()) throw new EmailOrPhoneAlreadyExistsException(standardResponse.message());
        }
        //static import
        if (!checkPasswordValid(profileDTO.password()).status()) throw new InvalidPasswordException(checkPasswordValid(profileDTO.password()).message());


        Profile profile = new Profile();
        profile.setName(profileDTO.name());
        profile.setEmail(profileDTO.email());
        profile.setPhone(profileDTO.phone());
        profile.setStatus(profileDTO.status());
        profile.setRole(profileDTO.role());
        profile.setVisible(true);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setPassword(passwordEncoder.encode(profileDTO.password()));
        repository.save(profile);
        return profileMapper.profileToProfileDTO(profile);
    }





    public StandardResponse checkEmailExists(String email) {
        boolean exists = repository.existsByEmailAndVisibleTrue(email);
        if (exists) {
            return new StandardResponse("This email already exists", false);
        }
        return new StandardResponse("email", true);
    }

    public StandardResponse checkPhoneExists(String phone) {
        boolean exists = repository.existsByPhoneAndVisibleTrue(phone);
        if (exists) {
            return new StandardResponse("This phone already exists", false);
        }
        return new StandardResponse("phone", true);
    }

    public List<ProfileCreateDTO.ProfileResponse> getAll() {
        List<ProfileCreateDTO.ProfileResponse> profiles = new ArrayList<>();
        for (Profile profile : repository.findAll()) {
            profiles.add(profileMapper.profileToProfileDTO(profile));
        }
        return profiles;
    }

    public ProfileCreateDTO.ProfileResponse update(Long profileId, ProfileCreateDTO profileDTO) {
        Optional<Profile> optionalProfile = repository.findById(profileId);
        if (optionalProfile.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        StandardResponse standardResponse = new StandardResponse("Email yoki Phone kiritilmadi", false);
        if (profileDTO.email() == null && profileDTO.phone() == null) {
            throw new MissingRequiredFieldsException(standardResponse.message());
        }
        if (profileDTO.email() != null){
            standardResponse = checkEmailExists(profileDTO.email());
            if (!standardResponse.status()) throw new EmailOrPhoneAlreadyExistsException(standardResponse.message());
        }
        if (profileDTO.phone() != null){
            standardResponse = checkPhoneExists(profileDTO.phone());
            if (!standardResponse.status()) throw new EmailOrPhoneAlreadyExistsException(standardResponse.message());
        }
        //static import
        if (!checkPasswordValid(profileDTO.password()).status()) throw new InvalidPasswordException(checkPasswordValid(profileDTO.password()).message());

        Profile profile = optionalProfile.get();
        profile.setName(profileDTO.name());
        profile.setEmail(profileDTO.email());
        profile.setPhone(profileDTO.phone());
        profile.setStatus(profileDTO.status());
        profile.setVisible(true);
        profile.setPassword(passwordEncoder.encode(profileDTO.password()));
        repository.save(profile);
        return profileMapper.profileToProfileDTO(profile);

    }


    public String delete(Long profileId) {
        repository.findById(profileId).ifPresent(profile -> {
            repository.delete(profile);
        });
        return "Profile deleted";
    }
}
