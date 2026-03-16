package api.tests;

import api.endpoints.UserEndpoints;
import api.models.UserRequest;
import api.models.UserResponse;
import api.models.UsersListResponse;
import api.utils.DataProviders;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("User Management")
public class UserTests extends BaseTest {

    // =============================================
    // GET ALL USERS
    // =============================================

    @Test
    @Story("Get all users")
    @Description("Verifies that user list is returned with pagination metadata.")
    public void testGetAllUsers() {
        Response response = UserEndpoints.getAllUsers();
        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected 200 OK");
        UsersListResponse userList = response.as(UsersListResponse.class);
        Assert.assertNotNull(userList.getUsers(),
                "Users list should not be null!");
        Assert.assertTrue(userList.getTotal() > 0,
                "Total should be greater than 0!");

        // Sample first and last — no need to iterate entire list
        UserResponse first = userList.getUsers().get(0);
        UserResponse last = userList.getUsers().get(userList.getUsers().size() - 1);

        Assert.assertNotNull(first.getId(), "First user ID should not be null!");
        Assert.assertNotNull(last.getId(), "Last user ID should not be null!");
        logger.info("✅ Total users: " + userList.getTotal()
                + " | Page size: " + userList.getUsers().size()
                + " | First: " + first.getFirstName()
                + " | Last: " + last.getFirstName());
    }

    // =============================================
    // GET USER BY ID
    // =============================================

    @Test
    @Story("Get user by ID")
    @Description("Verifies that a single user is returned by valid ID.")
    public void testGetUserById() {
        Response response = UserEndpoints.getUserById(1);
        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected 200 OK");
        UserResponse user = response.as(UserResponse.class);
        Assert.assertEquals(user.getId(), Integer.valueOf(1),
                "ID mismatch!");
        Assert.assertNotNull(user.getFirstName(),
                "First name should not be null!");
        Assert.assertNotNull(user.getEmail(),
                "Email should not be null!");
        logger.info("✅ User fetched: " + user.getFirstName()
                + " " + user.getLastName());
    }

    // =============================================
    // GET USER — NOT FOUND
    // =============================================

    @Test
    @Story("Get user by ID")
    @Description("Verifies 404 is returned for non-existing user.")
    public void testGetUserNotFound() {
        Response response = UserEndpoints.getUserById(99999);
        Assert.assertEquals(response.getStatusCode(), 404,
                "Expected 404 Not Found");
        logger.info("✅ 404 confirmed for non-existing user.");
    }

    // =============================================
    // CREATE USER
    // =============================================

    @Test
    @Story("Create user")
    @Description("Verifies that a new user is created successfully.")
    public void testCreateUser() {
        UserRequest payload = UserRequest.builder()
                .firstName("Petar")
                .lastName("Petrovic")
                .email("petar.petrovic@test.com")
                .age(30)
                .gender("male")
                .build();
        Response response = UserEndpoints.createUser(payload);
        Assert.assertEquals(response.getStatusCode(), 201,
                "Expected 201 Created");
        UserResponse created = response.as(UserResponse.class);
        Assert.assertEquals(created.getFirstName(), payload.getFirstName(),
                "First name mismatch!");
        Assert.assertEquals(created.getLastName(), payload.getLastName(),
                "Last name mismatch!");
        Assert.assertNotNull(created.getId(),
                "ID should not be null after creation!");
        logger.info("✅ User created with ID: " + created.getId());
    }

    // =============================================
    // UPDATE USER
    // =============================================

    @Test
    @Story("Update user")
    @Description("Verifies that user fields are updated successfully.")
    public void testUpdateUser() {
        UserRequest payload = UserRequest.builder()
                .firstName("Petar Updated")
                .build();
        Response response = UserEndpoints.updateUser(1, payload);
        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected 200 OK");
        UserResponse updated = response.as(UserResponse.class);
        Assert.assertEquals(updated.getFirstName(), payload.getFirstName(),
                "First name should be updated!");
        logger.info("✅ User updated: " + updated.getFirstName());
    }

    // =============================================
    // DELETE USER
    // =============================================

    @Test
    @Story("Delete user")
    @Description("Verifies that user is deleted and isDeleted flag is true.")
    public void testDeleteUser() {
        Response response = UserEndpoints.deleteUser(1);
        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected 200 OK");
        UserResponse deleted = response.as(UserResponse.class);
        Assert.assertTrue(deleted.getIsDeleted(),
                "isDeleted should be true!");
        logger.info("✅ User deleted. isDeleted: " + deleted.getIsDeleted());
    }

    // =============================================
    // GET ME — BEARER AUTH
    // =============================================

    @Test
    @Story("Authentication")
    @Description("Verifies that BEARER token works — /auth/me returns user.")
    public void testGetMe() {
        Response response = UserEndpoints.getMe();
        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected 200 OK");
        UserResponse me = response.as(UserResponse.class);
        Assert.assertNotNull(me.getEmail(),
                "Email should not be null!");

        logger.info("✅ Auth/me: " + me.getFirstName()
                + " " + me.getLastName());
    }
    // =============================================
    // CREATE USER — DATA DRIVEN
    // =============================================

    @Test(dataProvider = "validUsers", dataProviderClass = DataProviders.class)
    @Story("Create user")
    @Description("DDT: Verifies multiple users can be created in parallel.")
    public void testCreateMultipleUsers(UserRequest payload) {
        Response response = UserEndpoints.createUser(payload);
        Assert.assertEquals(response.getStatusCode(), 201,
                "Expected 201 Created");
        UserResponse created = response.as(UserResponse.class);
        Assert.assertEquals(created.getFirstName(), payload.getFirstName(),
                "First name mismatch!");
        Assert.assertEquals(created.getLastName(), payload.getLastName(),
                "Last name mismatch!");
        Assert.assertNotNull(created.getId(),
                "ID should not be null!");
        logger.info("✅ Thread: " + Thread.currentThread().getId()
                + " | Created: " + created.getFirstName()
                + " " + created.getLastName());
    }

// =============================================
// GET USER — INVALID IDs DATA DRIVEN
// =============================================

    @Test(dataProvider = "invalidUserIds", dataProviderClass = DataProviders.class)
    @Story("Get user by ID")
    @Description("DDT: Verifies 404 for multiple invalid user IDs.")
    public void testGetUserInvalidIds(int invalidId) {
        Response response = UserEndpoints.getUserById(invalidId);
        Assert.assertEquals(response.getStatusCode(), 404,
                "Expected 404 for invalid ID: " + invalidId);
        logger.info("✅ 404 confirmed for ID: " + invalidId);
    }
}