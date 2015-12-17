package myshop;

import com.buyexpressly.api.ExpresslyProvider;
import com.buyexpressly.api.MerchantServiceProvider;
import com.buyexpressly.api.MerchantServiceRoute;
import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.merchant.EmailAddressRequest;
import com.buyexpressly.api.resource.merchant.EmailStatusListResponse;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.CustomerData;
import com.buyexpressly.api.resource.server.Metadata;
import com.buyexpressly.api.resource.server.Tuple;
import com.buyexpressly.api.util.Builders;
import myshop.data.DummyDataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyShopExpresslyProvider implements MerchantServiceProvider {

    // Called by MerchantServiceRouter.displayPopup()
    @Override
    public void popupHandler(HttpServletRequest request, HttpServletResponse response, ExpresslyProvider expresslyProvider) {
        try {
            //
            // Attempt to request the popup from the expressly server and add it as an attribute to the session
            request.getSession().setAttribute(
                    "popupContent",
                    expresslyProvider.fetchMigrationConfirmationHtml(MerchantServiceRoute.DISPLAY_POPUP.getUriParameters(request.getRequestURI()).get("campaignCustomerUuid")));

            // forward the request to a page that will render the defined popup landing page at the bottom of the page
            //request.getRequestDispatcher("/homepage.jsp").forward(request, response);
            response.sendRedirect("/");
        } catch (ExpresslyException | IOException e) {
            try {
                response.sendRedirect("/failedpopup.jsp");
            } catch (IOException e1) {
                // handle error
            }
        }

    }

    @Override
    public String registerCustomer(String email, CustomerData customerData) {
        // persist customer to the database
        DummyDataSource.addCustomer(email, customerData);
        DummyDataSource.setCustomerToExisting(email);
        // the returned value will be used in the future to refer to the customer just created
        // it does not need to be the email, just needs to be the same.
        // will be referred to as customer reference
        return email;
    }

    @Override
    public boolean sendPasswordResetEmail(String customerReference) {
        // Reset user password and send them an email
        // so they can login in the future
        return true;
    }

    @Override
    public boolean createCustomerCart(String customerReference, CartData cartData) {
        // append the cart to the customer.
        if (DummyDataSource.getExistingEmails().contains(customerReference)) {
            // cart can have just a coupon, or coupon and a product id.
            if (cartData.getProductId() == null) {
                DummyDataSource.addCouponToCustomerCart(customerReference, cartData.getCouponCode());
            } else {
                DummyDataSource.addCouponToCustomerCart(customerReference, cartData.getCouponCode());
                DummyDataSource.addProductIdToCustomerCart(customerReference, cartData.getProductId());
            }
            return true;
        }
        return false;
    }

    @Override
    public CustomerData getCustomerData(String customerReference) throws ExpresslyException {
        // here we are assuming that the customer always exists in the database,
        // so in this implementation we generate dummy data
        // in your implementation make sure to check and throw an exception if the customer does not exist.
        CustomerData customerData = DummyDataSource.getCustomers().get(customerReference);
        customerData = customerData == null
                ? DummyDataSource.generateCustomerData(customerReference)
                : customerData;
        return customerData;
    }

    @Override
    public EmailStatusListResponse checkCustomerStatus(EmailAddressRequest emailAddressRequest) {
        // Check if the emails exist in the customer database
        // If they do, add them to the response
        List<String> existing = new ArrayList<>(emailAddressRequest.getEmails());
        existing.retainAll(DummyDataSource.getExistingEmails());

        // check if the emails have existed in the past but are now flagged as deleted
        // add these to the response
        List<String> deleted = new ArrayList<>(emailAddressRequest.getEmails());
        deleted.retainAll(DummyDataSource.getDeletedEmails());

        // check if the emails have been added to the database, but the registration hasn't been concluded
        // add these to the response
        List<String> pending = new ArrayList<>(emailAddressRequest.getEmails());
        pending.retainAll(DummyDataSource.getPendingEmails());

        return EmailStatusListResponse.builder()
                .addExisting(existing)
                .addDeleted(deleted)
                .addPending(pending)
                .build();
    }

    @Override
    public List<InvoiceResponse> getInvoices(InvoiceListRequest request) throws ExpresslyException {
        List<InvoiceResponse> invoices = new ArrayList<>();
        // check if each customer has made any orders
        for (InvoiceRequest customer : request.getCustomers()) {
            // if they have add each order into an invoice object and the invoice object to the response.
            invoices.add(DummyDataSource.generateInvoices(customer));
        }
        return invoices;
    }

    @Override
    public boolean checkCustomerAlreadyExists(String email) {
        // Check the Database for whether the customer has been registered in the past.
        return DummyDataSource.checkCustomerExists(email);
    }

    @Override
    public void loginAndRedirectCustomer(String email, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // Log in the customer's session
        httpServletRequest.getSession().setAttribute("login", email);
        // Once the customer is registered and his session loggedin, the customer is redirected to the login landing page defined here.
        try {
            httpServletResponse.sendRedirect("/loggedin.jsp");
        } catch (IOException e) {
            //Handle exception
        }


    }

    @Override
    public void handleCustomerAlreadyExists(String email, HttpServletRequest request, HttpServletResponse response) {
        try {
            //Replace the popup content with a warning message
            request.getSession().setAttribute(
                    "popupContent",
                    generateAlertJs(email));
            // Attempt to forward the request to the defined popup landing page
            response.sendRedirect("/");
        } catch (ExpresslyException | IOException e) {
            // If there's an error, the user is redirected to a pre defined page (usually the homepage)
            try {
                response.sendRedirect("failedpopup.jsp");
            } catch (IOException e1) {
                //handle exception
            }
        }

    }

    @Override
    public Metadata buildMerchantMetaData() {
        // Optional value for metadata, can be an empty String or your shop's url
        String sender = "http://localhost:8080/";
        //Optional metadata field, can be empty string, or your country code
        String locale = "GBR";
        // Metadata can be built with any amount of tuples for extra information on the shop
        // any of the fields used in the constructor are optional and nullable.
        return Metadata.build(sender, locale, Tuple.build("extra", "data"));
    }

    private String generateAlertJs(String email) {
        String message = Builders.isNullOrEmpty(email)
                ? " "
                : String.format(" with email %s, ", email);
        return String.format(
                "<script type=\"text/javascript\">(function(){alert(\"Customer%salready exists. \");})();</script>", message
        );
    }
}
