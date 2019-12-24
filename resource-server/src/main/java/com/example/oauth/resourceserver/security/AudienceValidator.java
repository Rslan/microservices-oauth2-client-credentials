package com.example.oauth.resourceserver.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Validates that the JWT token contains the intended audience in its claims.
 */
class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    /**
     * Constructs an instance of the validator.
     *
     * @param audience OAuth audience against which a JWT token is being validated
     */
    AudienceValidator(String audience) {
        this.audience = audience;
    }

    /**
     * Validate whether the JWT token is issued for the correct audience. The audience is specified at the time of
     * construction of the validator instance.
     *
     * @param jwt JWT token to be validated
     * @return result of the validation
     */
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience()
               .contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "The required audience is missing", null));
    }
}
