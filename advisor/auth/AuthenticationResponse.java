package advisor.auth;

import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {
    @SerializedName(value = "access_token")
    public String accessToken;

    @SerializedName(value = "refresh_token")
    public String refreshToken;

    @SerializedName(value = "token_type")
    public String tokenType;

    @SerializedName(value = "expires_in")
    public int expiration;

    @SerializedName(value = "scope")
    public String scope;
}
