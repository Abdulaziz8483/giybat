package dasturlashuz.giybat.util.session;

import dasturlashuz.giybat.config.CustomUserDetails;
import dasturlashuz.giybat.entity.ProfileEntity;
import dasturlashuz.giybat.exceptions.AppBadException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SpringSecurityUtil {
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getId();
            }else if (principal instanceof String && "anonymousUser".equals(((String) principal).trim())) {
                throw new RuntimeException("Anonymous user");
            }
        }
        throw new AppBadException("http://localhost:8081/api/v1/auth/login");
    }

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal);
            }else if (principal instanceof String && "anonymousUser".equals(((String) principal).trim())) {
                throw new RuntimeException("Anonymous user");
            }
        }
        throw new AppBadException("http://localhost:8081/api/v1/auth/login");
    }
}
