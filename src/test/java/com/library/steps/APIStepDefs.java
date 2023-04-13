package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class APIStepDefs {


    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    static String param;

    /**
     * US 01 RELATED STEPS
     *
     */
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        givenPart = RestAssured.given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));

    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        givenPart.accept(ContentType.JSON);


    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
         response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
         thenPart = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        thenPart.statusCode(statusCode);


    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);

    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, everyItem(notNullValue()));

    }


    /*
            US 02
     */

    @Given("Path param is {string}")
    public void path_param_is(String param) {

        this.param = param;
        givenPart.pathParam("id", param);

    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String path) {

        String path1 = thenPart.extract().jsonPath().getString(path);

        Assert.assertEquals(path1, param);

    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> dataList) {

        for (String eachField : dataList) {
            thenPart.body(eachField, is(notNullValue()));
        }

    }



}
