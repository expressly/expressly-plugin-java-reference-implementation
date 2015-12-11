package myshop;

import com.buyexpressly.api.MerchantServiceProvider;
import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.CustomerData;
import myshop.data.DummyDataSource;

import java.util.ArrayList;
import java.util.List;


public class MyShopExpresslyProvider implements MerchantServiceProvider {

    @Override
    public CustomerData getCustomerData(String email) throws ExpresslyException {
        CustomerData customerData = DummyDataSource.getCustomers().get(email);
        customerData = customerData == null
                ? DummyDataSource.generateCustomerData(email)
                : customerData;
        DummyDataSource.addPendingEmail(email);
        return customerData;
    }

    @Override
    public List<InvoiceResponse> getInvoices(InvoiceListRequest request) throws ExpresslyException {
        List<InvoiceResponse> invoices = new ArrayList<>();
        for (InvoiceRequest customer : request.getCustomers()) {
            invoices.add(DummyDataSource.generateInvoices(customer));
        }
        return invoices;
    }

    @Override
    public List<String> getExistingEmails(List<String> request) {
        List<String> response = new ArrayList<>(request);
        response.retainAll(DummyDataSource.getExistingEmails());
        return response;
    }

    @Override
    public List<String> getDeletedEmails(List<String> request) {
        List<String> response = new ArrayList<>(request);
        response.retainAll(DummyDataSource.getDeletedEmails());
        return response;
    }

    @Override
    public List<String> getPendingEmails(List<String> request) {
        List<String> response = new ArrayList<>(request);
        response.retainAll(DummyDataSource.getPendingEmails());
        return response;
    }

    @Override
    public String registerCustomer(String email, CustomerData customerData) {
        DummyDataSource.addCustomer(email, customerData);
        DummyDataSource.upgradeCustomerToExisting(email);
        return email;
    }

    @Override
    public boolean checkCustomerIsNew(String email) {
        return DummyDataSource.getExistingEmails().contains(email) ? false : true;
    }

    @Override
    public boolean storeCartData(String email, CartData cartData) {
        if (DummyDataSource.getExistingEmails().contains(email)) {
            DummyDataSource.addCartToCustomer(email, cartData);
            return true;
        }
        return false;
    }

    @Override
    public Integer getCustomerReference(String email) {
        return DummyDataSource.getExistingEmails().indexOf(email);
    }

    @Override
    public String getShopUrl() {
        return "http://localhost:8080/";
    }

    @Override
    public String getLocale() {
        return "GBR";
    }

    @Override
    public String loginCustomer(String merchantUserReference) {
        return "/loggedin.jsp";
    }

    @Override
    public boolean sendPasswordResetEmail(String email) {
        return DummyDataSource.getExistingEmails().contains(email) ? true : false;
    }

    @Override
    public String getPopupDestination() {
        return "/";
    }

    @Override
    public String getHomePageLocation() {
        return "/failedpopup.jsp";
    }
}
