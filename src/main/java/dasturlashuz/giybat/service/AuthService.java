package dasturlashuz.giybat.service;



import dasturlashuz.giybat.config.CustomUserDetails;
import dasturlashuz.giybat.dto.AuthDto;
import dasturlashuz.giybat.dto.LoginResponseDTO;
import dasturlashuz.giybat.dto.RegistrationDTO;
import dasturlashuz.giybat.dto.StandardResponse;
import dasturlashuz.giybat.dto.attach.AttachResponse;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.enums.ProfileStatus;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.repository.ProfileRepository;
import dasturlashuz.giybat.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static dasturlashuz.giybat.util.session.SpringSecurityUtil.getCurrentUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    @Lazy
    private AttachService attachService;


    public String Registration(RegistrationDTO dto) {


        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());

        if (optional.isPresent()) {
            ProfileEntity profile = optional.get();
            if (profile.getStatus().equals(ProfileStatus.IN_REGISTERED)) {
                profileRoleService.delete(profile.getId());
                profileRepository.delete(profile);

            } else {
                throw new AppBadException("User already exists");
            }
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setUsername(dto.getUsername());
        entity.setName(dto.getName());
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.IN_REGISTERED);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        profileRepository.save(entity);

        profileRoleService.createProfileRole(entity.getId(), ProfileRole.USER);

        emailSendingService.sendRegistrationEmail(entity.getUsername(), entity.getId());

        return "Registration successful";
    }

    public String regVerification(Integer profileId) {
        ProfileEntity profile = profileService.getbyId(profileId);
        if (profile.getStatus().equals(ProfileStatus.IN_REGISTERED)) {
            profileRepository.changeStatus(profileId, ProfileStatus.ACTIVE);
            return "verification finished";
        }
        throw new AppBadException("Verification failed. User is not in registration status");
    }

    public LoginResponseDTO login(@Valid AuthDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();
                String token = JwtUtil.encode(profile.getUsername(), profile.getId(), profile.getRole());

                //getRole list
                List<String> profileRoles = profileRoleService.getRolesByProfileId(profile.getId());
                //get Photo id and url
                ProfileEntity currentUser = profileService.getbyId(profile.getId());

                attachService.openUrl(currentUser.getAttachId());
                AttachResponse.AttachUrl photo = new AttachResponse.AttachUrl(currentUser.getAttachId(), attachService.openUrl(currentUser.getAttachId()));

                return new LoginResponseDTO(profile.getName(), profile.getUsername(),profileRoles,token,photo);
            }
            throw new AppBadException("Invalid username or password");
        } catch (BadCredentialsException e) {
            throw new AppBadException("Invalid username or password");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppBadException("Internal server error");
        }

    }

}
