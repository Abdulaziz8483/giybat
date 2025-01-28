package dasturlashuz.giybat.util.profile;

import dasturlashuz.giybat.dto.StandardResponse;

public class ProfileUtil {

    public static StandardResponse checkPasswordValid(String password) {
        if (password.length() < 6) {
            return new StandardResponse("Password must be at least 6 characters long", false);
        }
        return new StandardResponse("Ok", true);
    }
}
