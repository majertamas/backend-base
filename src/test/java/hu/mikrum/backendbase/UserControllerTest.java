package hu.mikrum.backendbase;

import hu.mikrum.backendbase.model.entity.User;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.Test;

import static hu.mikrum.backendbase.util.Util.TEST_USER_EMAIL;
import static hu.mikrum.backendbase.util.Util.TEST_USER_NAME;
import static hu.mikrum.backendbase.util.Util.TEST_USER_PASSWORD;

public class UserControllerTest extends BackendBaseApplicationTests {


    public static final String API_USERS = "/api/users";
    public static final String SLASH = "/";

    @Test
    public void test_get_user() throws Exception {
        User user = User.builder()
                .name(TEST_USER_NAME)
                .password(TEST_USER_PASSWORD)
                .email(TEST_USER_EMAIL)
                .build();
        String userJson = objectMapper.writeValueAsString(user);

        MvcResult saveResult = doPost(API_USERS, userJson);

        String contentAsString = saveResult.getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(contentAsString, User.class);

        assert createdUser.getId() != null;
        assert TEST_USER_NAME.equals(createdUser.getName());
        assert TEST_USER_EMAIL.equals(createdUser.getEmail());

        MvcResult getResult = doGet(API_USERS + SLASH + createdUser.getId());

        contentAsString = getResult.getResponse().getContentAsString();
        User foundUser = objectMapper.readValue(contentAsString, User.class);

        assert createdUser.getId().equals(foundUser.getId());
        assert createdUser.getName().equals(foundUser.getName());
        assert createdUser.getEmail().equals(foundUser.getEmail());

    }
}