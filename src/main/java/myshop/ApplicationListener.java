package myshop;

import com.buyexpressly.api.ExpresslyFactory;
import com.buyexpressly.api.MerchantServiceRouter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // The configuration properties to initialise the expressly sdk
        String expresslyApiKey = "NGU2ZDE3OTAtODhiNC00NzljLWFlZWEtZGQ1MDFiODhhYTg2OlRwS3podHVWRkFjbXlLNFhtb3lmYVVMN3liRkhMVW52";
        //String devExpresslyEndpoint = "http://dev.expresslyapp.com/api";
        String expresslyEndpoint = "http://localhost:8181/";
        //String yourShopEndpoint = "http://localhost:8080/";

        // Create a factory
        ExpresslyFactory factory = ExpresslyFactory.createFactory(
                expresslyApiKey,
                //new MyShopExpresslyProvider(),
                new MyShopExpresslyProvider(),
                expresslyEndpoint);
     /*   try {
            factory.buildExpresslyProvider().install("http://localhost:8080");
        } catch (IOException | ExpresslyException e) {
            e.printStackTrace();
        }*/
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
