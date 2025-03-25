package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.StandardResponse;
import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.entity.ProfileRoleEntity;
import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.enums.ProfileStatus;
import dasturlashuz.giybat.exceptions.*;
import dasturlashuz.giybat.mapper.profile.ProfileMapper;
import dasturlashuz.giybat.mapper.profile.ProfileShortInfoMapper;
import dasturlashuz.giybat.repository.ProfileRepository;
import dasturlashuz.giybat.util.ProfileSpecification;
import dasturlashuz.giybat.util.profile.ProfileUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dasturlashuz.giybat.util.profile.ProfileUtil.checkPasswordValid;
import static dasturlashuz.giybat.util.session.SpringSecurityUtil.getCurrentUserId;

@Service
@RequiredArgsConstructor
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private AttachService attachService;
    @Autowired
    @Lazy
    private EmailSendingService emailSendingService;
    @Autowired
    @Lazy
    private PostService postService;

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


    public ProfileCreateDTO.ProfileDetailDTO getDetail() {
        Integer currentUserId = getCurrentUserId();
        ProfileShortInfoMapper map = profileRepository.getProfileShortInfoMapper(currentUserId);
        Optional<String> optionalPhotoId = map.getPhotoId();
        String photoUrl = null;
        String photoId = null;
        if (optionalPhotoId.isPresent()){
            photoUrl = attachService.openUrl(optionalPhotoId.get());
            photoId = optionalPhotoId.get();
        }

        System.out.println(map.getName());
        List<String> roles = map.getRoles();


        return new ProfileCreateDTO.ProfileDetailDTO(map.getName(), map.getUsername(), map.getRoles(), photoId, photoUrl);
    }


    public ProfileCreateDTO.ProfileResponse updateName(ProfileCreateDTO.ProfileUpdateName dto) {
        ProfileEntity currentUser = findByIdForSession(getCurrentUserId());
        currentUser.setName(dto.name());
        profileRepository.save(currentUser);
        return profileMapper.profileToProfileDTO(currentUser);
    }

    public ProfileCreateDTO.ProfileResponse updatePhoto(ProfileCreateDTO.ProfileUpdatePhoto dto) {
        ProfileEntity currentUser = findByIdForSession(getCurrentUserId());
        currentUser.setAttachId(dto.photoId());
        profileRepository.save(currentUser);
        return profileMapper.profileToProfileDTO(currentUser);
    }
    public StandardResponse updatePassword(ProfileCreateDTO.ProfileUpdatePassword dto) {
        ProfileEntity currentUser = findByIdForSession(getCurrentUserId());
        if (!passwordEncoder.matches(dto.oldPassword(), currentUser.getPassword())){
            throw new AppBadException("Old password does not match");
        }

        if (passwordEncoder.matches(dto.newPassword(), currentUser.getPassword())) {
            throw new AppBadException("New password cannot be the same as the old password");
        }
        currentUser.setPassword(passwordEncoder.encode(dto.newPassword()));
        profileRepository.save(currentUser);
        return new StandardResponse("Password updated", true);
    }

    public String updateUsername(ProfileCreateDTO.ProfileUpdateUsername dto) {
        ProfileEntity currentUser = findByIdForSession(getCurrentUserId());
        if (currentUser.getUsername().equals(dto.username())){
            throw new AppBadException("You are entering a previous username.");
        }
        currentUser.setUsername(dto.username());
        currentUser.setStatus(ProfileStatus.UNVERIFIED);
        profileRepository.save(currentUser);
        emailSendingService.sendRegistrationEmail(currentUser.getUsername(), currentUser.getId());
        return "Username updated confirm email";
    }

    public List<ProfileCreateDTO.ProfileFilterResponse> filter(ProfileCreateDTO.ProfileFilterDTO dto, int page, int size) {

        Page<ProfileEntity> profilePage = getFilter(dto, page, size);
        List<ProfileCreateDTO.ProfileFilterResponse> profileResponses = profilePage.getContent().stream()
                .map(profile ->{
                    return new ProfileCreateDTO.ProfileFilterResponse(
                            profile.getId(),
                            profile.getName(),
                            profile.getUsername(),
                            profile.getAttachId(),
                            attachService.openUrl(profile.getAttachId()),
                            postService.getProfilePostCount(profile.getId())
                    );
                })
                .toList();
        return profileResponses;
    }

    // DataBase bilan boglana oladigan yordamchi class lar

    public StandardResponse checkUsernameExists(String email) {
        boolean exists = profileRepository.existsByUsernameAndVisibleTrue(email);
        if (exists) {
            return new StandardResponse("This username already exists", false);
        }
        return new StandardResponse("username", true);
    }

    public ProfileEntity findByIdForSession(Integer profileId){
        Optional<ProfileEntity> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isEmpty()){
            throw new AppBadException("Profile not found");
        }
        return optionalProfile.get();
    }

    public Page<ProfileEntity> getFilter(ProfileCreateDTO.ProfileFilterDTO dto , int page, int size) {
        Specification<ProfileEntity> spec = ProfileSpecification.filterProfiles(dto);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        return profileRepository.findAll(spec, pageable);
    }
}
