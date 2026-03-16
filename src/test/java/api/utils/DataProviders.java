package api.utils;

import api.models.UserRequest;
import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class DataProviders {

    // New Faker instance for each DataProvider call
    // ensures different data on every test run
    private final Faker faker = new Faker();

    @DataProvider(name = "validUsers", parallel = true)
    public Object[][] validUsers() {
        return new Object[][]{
                {
                        UserRequest.builder()
                                .firstName(faker.name().firstName())
                                .lastName(faker.name().lastName())
                                .email(faker.internet().safeEmailAddress())
                                .age(faker.number().numberBetween(18, 65))
                                .gender("male")
                                .build()
                },
                {
                        UserRequest.builder()
                                .firstName(faker.name().firstName())
                                .lastName(faker.name().lastName())
                                .email(faker.internet().safeEmailAddress())
                                .age(faker.number().numberBetween(18, 65))
                                .gender("female")
                                .build()
                },
                {
                        UserRequest.builder()
                                .firstName(faker.name().firstName())
                                .lastName(faker.name().lastName())
                                .email(faker.internet().safeEmailAddress())
                                .age(faker.number().numberBetween(18, 65))
                                .gender("male")
                                .build()
                }
        };
    }

    @DataProvider(name = "invalidUserIds", parallel = true)
    public Object[][] invalidUserIds() {
        return new Object[][]{
                {99999},
                {-1},
                {0}
        };
    }
}