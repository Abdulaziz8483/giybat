package dasturlashuz.giybat.service;

import dasturlashuz.giybat.dto.StandardResponse;
import dasturlashuz.giybat.dto.profile.ProfileCreateDTO;
import dasturlashuz.giybat.entity.Profile;
import dasturlashuz.giybat.exceptions.EmailOrPhoneAlreadyExistsException;
import dasturlashuz.giybat.exceptions.MissingRequiredFieldsException;
import dasturlashuz.giybat.repository.ProfileRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository repository;

   /* public ProfileCreateDTO.ProfileResponse create(ProfileCreateDTO profileDTO) {
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
        Profile profile = new Profile();
        profile.

    }*/



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
}
