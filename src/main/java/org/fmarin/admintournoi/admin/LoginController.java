package org.fmarin.admintournoi.admin;

import com.auth0.NonceUtils;
import com.auth0.SessionUtils;
import com.auth0.web.Auth0CallbackHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class LoginController extends Auth0CallbackHandler {

    @GetMapping(value = "/login")
    public ModelAndView login(final Map<String, Object> model, final HttpServletRequest req) {
        detectError(model);
        NonceUtils.addNonceToStorage(req);
        model.put("state", SessionUtils.getState(req));
        return new ModelAndView("login");
    }

    @GetMapping(value = "${auth0.loginCallback}")
    public void callback(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        super.handle(req, res);
    }

    private void detectError(final Map<String, Object> model) {
        if (model.get("error") != null) {
            model.put("error", true);
        } else {
            model.put("error", false);
        }
    }
}
