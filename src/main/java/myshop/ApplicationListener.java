package myshop;

import com.buyexpressly.api.ExpresslyFactory;
import com.buyexpressly.api.MerchantServiceRouter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // The configuration properties to initialise the expressly sdk
        String expresslyApiKey = "ZjkyMThhYzEtMmMzNS00MzYxLTg2ZDMtMDNiMjk5NDJmMDBmOkhaQUhkc0R4b0x1YXlteE5hZmtxUFpBRFlnTFNkb3hp";
        String expresslyEndpoint = "http://localhost:8181/";
        //String yourShopEndpoint = "http://localhost:8080/";

        // Create a factory
        ExpresslyFactory factory = ExpresslyFactory.createFactory(
                expresslyApiKey,
                new MyShopExpresslyProvider(),
                expresslyEndpoint);

        // Add the router to the attribute space so that the MerchantPluginServlet can
        // access it.
        sce.getServletContext().setAttribute(
                MerchantServiceRouter.class.getName(),
                factory.buildRouter());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
