package api.tests;

import api.endpoints.UserEndpoints;
import api.models.UserRequest;
import api.models.UserResponse;
import api.models.UsersListResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Mock User Management")
public class MockTests extends BaseTest {

    @Test
    @Story("Get all users")
    public void testMockGetAllUsers() {
        Response response = UserEndpoints.getAllUsers();
        Assert.assertEquals(response.getStatusCode(), 200);
        UsersListResponse userList = response.as(UsersListResponse.class);
        Assert.assertNotNull(userList.getUsers());
        Assert.assertEquals(userList.getTotal(), Integer.valueOf(2), "Mock should return exactly 2 users!");
        logger.info(" Mock users fetched: " + userList.getTotal());
    }

    @Test
    @Story("Get user by ID")
    public void testMockGetUserById() {
        Response response = UserEndpoints.getUserById(1);
        Assert.assertEquals(response.getStatusCode(), 200);
        UserResponse user = response.as(UserResponse.class);
        Assert.assertEquals(user.getFirstName(), "Mock", "Should return mock user!");
        logger.info(" Mock user fetched: " + user.getFirstName());
    }

    @Test
    @Story("Get user by ID")
    public void testMockGetUserNotFound() {
        Response response = UserEndpoints.getUserById(99999);
        Assert.assertEquals(response.getStatusCode(), 404);
        logger.info(" Mock 404 confirmed.");
    }

    @Test
    @Story("Create user")
    public void testMockCreateUser() {
        UserRequest payload = UserRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .age(25).gender("male").build();
        Response response = UserEndpoints.createUser(payload);
        Assert.assertEquals(response.getStatusCode(), 201);
        UserResponse created = response.as(UserResponse.class);
        Assert.assertNotNull(created.getId());
        logger.info(" Mock user created: " + created.getId());
    }

    @Test
    @Story("Update user")
    public void testMockUpdateUser() {
        UserRequest payload = UserRequest.builder().firstName("Mock Updated").build();
        Response response = UserEndpoints.updateUser(1, payload);
        Assert.assertEquals(response.getStatusCode(), 200);
        UserResponse updated = response.as(UserResponse.class);
        Assert.assertEquals(updated.getFirstName(), "Mock Updated");
        logger.info(" Mock user updated: " + updated.getFirstName());
    }

    @Test
    @Story("Delete user")
    public void testMockDeleteUser() {
        Response response = UserEndpoints.deleteUser(1);
        Assert.assertEquals(response.getStatusCode(), 200);
        UserResponse deleted = response.as(UserResponse.class);
        Assert.assertTrue(deleted.getIsDeleted());
        logger.info(" Mock user deleted.");
    }

    @Test
    @Story("Authentication")
    public void testMockGetMe() {
        Response response = UserEndpoints.getMe();
        Assert.assertEquals(response.getStatusCode(), 200);
        UserResponse me = response.as(UserResponse.class);
        Assert.assertNotNull(me.getEmail());
        logger.info(" Mock auth/me: " + me.getFirstName());
    }
}
