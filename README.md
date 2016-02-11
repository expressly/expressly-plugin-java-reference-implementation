![Expressly](http://developer.buyexpressly.com/img/expressly-logo-sm-gray.png)
# Expressly Plug-in Java / Tomcat Reference Implementation

This project is supplied as a reference for any developers wishing to integrate their java e-commerce platform
with the [Expressly Network](https://buyexpressly.com/).
  
The reference implementation makes use of the [Expressly Java SDK](https://github.com/expressly/expressly-plugin-sdk-java-core).

- - -

## Quick Start

You can start the reference implementation by executing the following command:

    gradlew tomcatRun

and then go to: [http://localhost:8080/expressly/api/ping](http://localhost:8080/expressly/api/ping)

## What Am I Looking At

This project is a bare bones Tomcat project with the core steps that you will need to integrate your shop with the Expressly Network. These are:

 1. **Added the expressly plugin SDK dependency** - you should restrict any dynamic dependency to the major version as Expressly will 
 endeavour to make sure there are no breaking changes within the same major version but reserve the right to modify the 
 protocol between major releases, i.e. 'com.buyexpressly:plugin-sdk:2.+'

 1. **Implement com.buyexpressly.api.MerchantServiceProvider** - you will need to extend and implement all the methods belonging to 
 this interface. This is the bridge between the Expressly network and your platform. The reference implementation has an 
 example in [MyShopExpresslyProvider](https://github.com/expressly/expressly-plugin-java-reference-implementation/blob/master/src/main/java/myshop/MyShopExpresslyProvider.java).

 1. **Configure the Expressly router** - you will need to create and add an instance of the com.buyexpressly.api.MerchantServiceRouter to the
 application context so that the 
 [MerchantPluginServlet](https://github.com/expressly/expressly-plugin-sdk-java-core/blob/master/src/main/java/com/buyexpressly/api/MerchantPluginServlet.java)
 can access the router. There is an example of this being done in 
 [ApplicationListener](https://github.com/expressly/expressly-plugin-java-reference-implementation/blob/master/src/main/java/myshop/ApplicationListener.java)
 
 1. **Map the MerchantPluginServlet** - You will need to map the MerchantPluginServlet mentioned above to /expressly/api/*. See 
 [web.xml](https://github.com/expressly/expressly-plugin-java-reference-implementation/blob/master/src/main/webapp/WEB-INF/web.xml)
 
 1. **Enable rendering of Expressly popup** - When an Expressly network customer selects to migrate their profile to
 your platform you will be required to display an Expressly customer confirmation popup to the user before they proceed. In the reference implementation
 you will notice that the 
 [PopupExampleServlet](https://github.com/expressly/expressly-plugin-java-reference-implementation/blob/master/src/main/java/myshop/PopupExampleServlet.java)
 and 
 [homepage.jsp](https://github.com/expressly/expressly-plugin-java-reference-implementation/blob/master/src/main/webapp/homepage.jsp) 
 demonstrate one way of handling the flow. This servlet and jsp are meant to represent existing servlets and pages in your
 shop.
  
 Please note that the location and paradigms used in the reference implementation focused on brevity and clarity to help
 the reader.

- - -

## Is That It

Pretty much. If you haven't already you'll have to register and [get your API key](https://buyexpressly.com). 
Feel free to get in touch with us if you have any questions.

You can find further resources on our [developer portal](http://developer.buyexpressly.com/). 

- - -

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

- - -

## License

Released under the [MIT License](http://www.opensource.org/licenses/MIT).
 
 


