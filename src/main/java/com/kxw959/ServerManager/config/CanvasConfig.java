package com.kxw959.ServerManager.config;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.AccountReader;
import edu.ksu.canvas.model.Account;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import lombok.Data;

@Data
public class CanvasConfig {
    String OAUTH_TOKEN = "7~EUab3jULkQRIL42mrTzAYBCNDsPadRpzJKLdi1xJohtu3Mh7JZcEQseSJaevqCzj";
    String AUTH_HEADER = "Bearer "+OAUTH_TOKEN;

    String canvasBaseUrl = "https://canvas.instructure.com";
    OauthToken oauthToken = new NonRefreshableOauthToken(OAUTH_TOKEN);
    CanvasApiFactory apiFactory = new CanvasApiFactory(canvasBaseUrl);
    String courseID = "4370705";
}
