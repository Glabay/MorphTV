package de.timroes.axmlrpc;

import de.timroes.base64.Base64;
import java.net.HttpURLConnection;

public class AuthenticationManager {
    private String pass;
    private String user;

    public void clearAuthData() {
        this.user = null;
        this.pass = null;
    }

    public void setAuthData(String str, String str2) {
        this.user = str;
        this.pass = str2;
    }

    public void setAuthentication(HttpURLConnection httpURLConnection) {
        if (!(this.user == null || this.pass == null || this.user.length() <= 0)) {
            if (this.pass.length() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.user);
                stringBuilder.append(":");
                stringBuilder.append(this.pass);
                String encode = Base64.encode(stringBuilder.toString());
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Basic ");
                stringBuilder2.append(encode);
                httpURLConnection.addRequestProperty("Authorization", stringBuilder2.toString());
            }
        }
    }
}
