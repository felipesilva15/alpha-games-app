package senac.alphagames.model;

public class TokenInfo {
    private String access_token;
    private String token_type;
    private int expires_in;

    public TokenInfo() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }
}
