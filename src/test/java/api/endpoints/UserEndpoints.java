package api.endpoints;

import api.base.AuthType;
import api.base.BaseRequest;
import api.models.UserRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserEndpoints {

    // GET /users
    public static Response getAllUsers() {
        return given()
                .spec(BaseRequest.getSpec(AuthType.NO_AUTH))
                .when()
                .get(Routes.USERS);
    }

    // GET /users/{id}
    public static Response getUserById(int id) {
        return given()
                .spec(BaseRequest.getSpec(AuthType.NO_AUTH))
                .when()
                .get(Routes.USER_BY_ID, id);
    }

    // POST /users/add
    public static Response createUser(UserRequest payload) {
        return given()
                .spec(BaseRequest.getSpec(AuthType.NO_AUTH))
                .body(payload)
                .when()
                .post(Routes.ADD_USER);
    }

    // PUT /users/{id}
    public static Response updateUser(int id, UserRequest payload) {
        return given()
                .spec(BaseRequest.getSpec(AuthType.NO_AUTH))
                .body(payload)
                .when()
                .put(Routes.USER_BY_ID, id);
    }

    // DELETE /users/{id}
    public static Response deleteUser(int id) {
        return given()
                .spec(BaseRequest.getSpec(AuthType.NO_AUTH))
                .when()
                .delete(Routes.USER_BY_ID, id);
    }

    // GET /auth/me — requires BEARER token
    public static Response getMe() {
        return given()
                .spec(BaseRequest.getSpec(AuthType.BEARER))
                .when()
                .get(Routes.AUTH_ME);
    }
}