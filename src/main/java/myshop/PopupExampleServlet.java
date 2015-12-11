package myshop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PopupExampleServlet extends HttpServlet {
    private static final String EXPRESSLY_POPUP_ATTRIBUTE_NAME = "popupContent";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        transferExpresslyPopupContentFromSessionToRequest(req);
        req.getRequestDispatcher("homepage.jsp").forward(req, resp);
    }

    private void transferExpresslyPopupContentFromSessionToRequest(HttpServletRequest req) {
        Object expresslyPopupContent = req.getSession().getAttribute(EXPRESSLY_POPUP_ATTRIBUTE_NAME);
        if (expresslyPopupContent != null) {
            req.getSession().removeAttribute(EXPRESSLY_POPUP_ATTRIBUTE_NAME);
            req.setAttribute(EXPRESSLY_POPUP_ATTRIBUTE_NAME, expresslyPopupContent);
        }
    }
}
