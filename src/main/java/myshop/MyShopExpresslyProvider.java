package myshop;

import com.buyexpressly.api.MerchantServiceProvider;
import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.CustomerData;

import java.util.List;

public class MyShopExpresslyProvider implements MerchantServiceProvider {
    @Override
    public CustomerData getCustomerData(String email) throws ExpresslyException {
        return null;
    }

    @Override
    public List<InvoiceResponse> getInvoices(InvoiceListRequest request) throws ExpresslyException {
        return null;
    }

    @Override
    public List<String> getExistingEmails(List<String> request) {
        return null;
    }

    @Override
    public List<String> getDeletedEmails(List<String> request) {
        return null;
    }

    @Override
    public List<String> getPendingEmails(List<String> request) {
        return null;
    }

    @Override
    public String registerCustomer(String email, CustomerData customerData) {
        return null;
    }

    @Override
    public boolean checkCustomerIsNew(String email) {
        return false;
    }

    @Override
    public boolean storeCartData(String email, CartData cartData) {
        return false;
    }

    @Override
    public Integer getCustomerReference(String email) {
        return null;
    }

    @Override
    public String getShopUrl() {
        return null;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public String loginCustomer(String merchantUserReference) {
        return null;
    }

    @Override
    public boolean sendPasswordResetEmail(String email) {
        return false;
    }

    @Override
    public String getPopupDestination() {
        return null;
    }

    @Override
    public String getHomePageLocation() {
        return null;
    }
}
