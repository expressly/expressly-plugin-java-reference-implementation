package myshop.data;

import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.merchant.InvoiceOrderResponse;
import com.buyexpressly.api.resource.merchant.InvoiceRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.Address;
import com.buyexpressly.api.resource.server.CustomerData;
import com.buyexpressly.api.resource.server.Email;
import com.buyexpressly.api.resource.server.Phone;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 12/10/15.
 */
public final class DummyDataSource {
    /**
     * (Mock data)
     * List of emails that are set as active in the shop database
     */
    private static List<String> existingEmails = new ArrayList<>(Arrays.asList("a@test.com", "b@test.com"));
    /**
     * (Mock data)
     * List of emails that have started migration / registration but are not yet set as active on the DB (nor are deleted)
     */
    private static List<String> pendingEmails = new ArrayList<>(Arrays.asList("e@test.com", "f@test.com"));
    /**
     * (Mock data)
     * List of emails that are set as deleted in the shop database
     */
    private static List<String> deletedEmails = Arrays.asList("g@test.com", "i@test.com");
    /**
     * (Mock data)
     * Map of the existing customers currently on the Database
     */
    private static Map<String, CustomerData> customers = new HashMap<>();
    /**
     * (Mock data)
     * Map of the existing coupons associated with a customer currently on the Database
     */
    private static Map<String, String> allCoupons = new HashMap<>();
    /**
     * (Mock data)
     * Map of the existing carts currently on the Database
     */
    private static Map<String, String> allProductIds = new HashMap<>();

    private DummyDataSource() {

    }


    public static List<String> getPendingEmails() {
        return pendingEmails;
    }

    public static List<String> getDeletedEmails() {
        return deletedEmails;
    }

    public static Map<String, CustomerData> getCustomers() {
        return customers;
    }

    public static List<String> getExistingEmails() {

        return existingEmails;
    }

    /**
     * (Mock Data)
     *
     * @param email customer's email
     * @return a dummy CustomerData object
     */
    public static CustomerData generateCustomerData(String email) {
        existingEmails.add(email);
        ObjectMapper om = new ObjectMapper();

        try {
            return CustomerData.builder()
                    .addAddress(genAddress(om))
                    .addEmailAddress(genEmailAddress(email, om))
                    .addPhoneNumber(genPhoneNumber(om))
                    .withBillingAddress(0)
                    .withShippingAddress(0)
                    .withFirstName("testName")
                    .withLastName("Burberry")
                    .withGender("M")
                    .withDob(LocalDate.now().minusYears(30))
                    .withCompany("Test Company")
                    .withTaxNumber("SE123456")
                    .withLastUpdatedAtSource(DateTime.now().minusDays(2))
                    .withLastOrderTimeAtSource(LocalDate.now().minusDays(2))
                    .withOrderItemCount(2)
                    .build();
        } catch (IOException e) {
            throw new ExpresslyException("Couldn't retrieve customer, message: " + e.getMessage());
        }
    }

    private static Phone genPhoneNumber(ObjectMapper om) throws IOException {
        return om.readValue(
                "      {" +
                        "        \"type\" : \"L\"," +
                        "        \"number\" : \"02079460975\"," +
                        "        \"countryCode\" : 44" +
                        "      }",
                Phone.class);
    }

    private static Email genEmailAddress(String email, ObjectMapper om) throws IOException {
        return om.readValue(
                String.format(
                        "      {" +
                                "        \"alias\" : \"personal\"," +
                                "        \"email\" : \"%s\"" +
                                "      }", email),
                Email.class
        );
    }

    private static Address genAddress(ObjectMapper om) throws IOException {
        return om.readValue("{" +
                        "        \"firstName\" : \"testName\"," +
                        "        \"lastName\" : \"Burberry\"," +
                        "        \"address1\" : \"Basement Flat\"," +
                        "        \"address2\" : \"61 Wellfield Road\"," +
                        "        \"city\" : \"Roath\"," +
                        "        \"companyName\" : \"company\"," +
                        "        \"zip\" : \"CF243DG\"," +
                        "        \"phone\" : 0," +
                        "        \"addressAlias\" : \"home\"," +
                        "        \"stateProvince\" : \"Cardiff\"," +
                        "        \"country\" : \"GB\"" +
                        "      }",
                Address.class);
    }

    public static InvoiceResponse generateInvoices(InvoiceRequest customer) {
        return InvoiceResponse.builder()
                .withEmail(customer.getEmail())
                .withPostTaxTotal(new BigDecimal(110.0))
                .withPreTaxTotal(new BigDecimal(100.0))
                .withTax(new BigDecimal(10.0))
                .add(InvoiceOrderResponse.builder()
                        .setOrderId("ORDER-5321311")
                        .setOrderDate(DateTime.parse("2015-07-10T11:42:00+01:00").toLocalDate())
                        .setCurrency("GBP")
                        .setItemCount(2)
                        .setCoupon("COUPON")
                        .setPostTaxTotal(new BigDecimal(110.0))
                        .setPreTaxTotal(new BigDecimal(100.0))
                        .setTax(new BigDecimal(10.0))
                        .build())
                .build();
    }

    public static void addPendingEmail(String email) {
        pendingEmails.add(email);
    }

    public static void addCustomer(String email, CustomerData customerData) {
        customers.put(email, customerData);
    }

    public static void setCustomerToExisting(String email) {
        pendingEmails.remove(email);
        existingEmails.add(email);
    }

    public static void addCouponToCustomerCart(String email, String couponCode) {
        allCoupons.put(email, couponCode);
    }

    public static void addProductIdToCustomerCart(String customerReference, String productId) {
        allProductIds.put(customerReference, productId);
    }

    public static boolean checkCustomerExists(String email) {
        return existingEmails.contains(email);
    }
}
