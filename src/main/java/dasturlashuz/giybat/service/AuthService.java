package dasturlashuz.giybat.service;



import dasturlashuz.giybat.config.CustomUserDetails;
import dasturlashuz.giybat.dto.AuthDto;
import dasturlashuz.giybat.dto.RegistrationDTO;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.enums.ProfileRole;
import dasturlashuz.giybat.enums.ProfileStatus;
import dasturlashuz.giybat.exceptions.AppBadException;
import dasturlashuz.giybat.repository.ProfileRepository;
import dasturlashuz.giybat.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ProfileService profileService;

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

    public String login(@Valid AuthDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();
                String token = JwtUtil.encode(profile.getUsername(), profile.getId(), profile.getRole());
                return "{\"message\": \"Login successful.\", \"token\": \"" + token + "\"}";
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
