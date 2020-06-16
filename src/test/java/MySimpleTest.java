import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MySimpleTest {
    private final String LoginAPI = "/api/auth/signin";
    private final String allAuthor = "/api/rest/author/all";
    private final String allBooks = "/api/rest/book/all";
    private final String getBook = "/api/rest/book/get/";
    private String accessToken;
    private String tokenType;

    @BeforeMethod
    public void doAuth() throws ParseException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9090;
        JSONObject loginRequest  = new JSONObject();
        loginRequest.put("username","wanikirtesh");
        loginRequest.put("password","password");
        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(loginRequest.toJSONString())
                .post(LoginAPI);
        String strLoginResponse = loginResponse.asString();
        JSONParser parser = new JSONParser();
        JSONObject objLoginResponse = (JSONObject) parser.parse(strLoginResponse);

        accessToken = objLoginResponse.get("accessToken").toString();
        tokenType = objLoginResponse.get("tokenType").toString();
    }

    @Test
    public void validateTotalNumberOfBooks()  {
        given()
                .header("Authorization", tokenType + " " + accessToken)
                .get(allBooks)
                .then()
                .statusCode(200)
                .body("size()",is(13));
    }
    @Test
    public void validateBookNameForId(){
        given()
                .header("Authorization", tokenType + " " + accessToken)
                .get(getBook+1)
                .then()
                .statusCode(200)
                .body("bookName",is("Book 1"))
                .and()
                .body("pages",is(550))
                .and()
                .body("authors.authorName",hasItems("Kirtesh Wani","Gopal Wani"));
    }
    @Test
    public void validateTotalNumberOfAuthors(){
        given()
                .header("Authorization", tokenType + " " + accessToken)
                .get(allAuthor)
                .then()
                .statusCode(200)
                .body("size()",is(5));

    }
}
