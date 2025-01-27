package hu.mikrum.backendbase;

import hu.mikrum.backendbase.model.entity.User;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;

import static hu.mikrum.backendbase.UserControllerTest.API_USERS;
import static hu.mikrum.backendbase.util.Util.PASSWORD;
import static hu.mikrum.backendbase.util.Util.TEST_USER_EMAIL;
import static hu.mikrum.backendbase.util.Util.TEST_USER_NAME;
import static hu.mikrum.backendbase.util.Util.TEST_USER_PASSWORD;
import static hu.mikrum.backendbase.util.Util.USERNAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BackendBaseApplicationTests {


    public static final String API_AUTH = "/api/auth";

    @Test
    public void test_authorization_header_missing() throws Exception {
        doGet(API_AUTH, 401);
    }

    @Test
    public void test_ok() throws Exception {
        User user = User.builder()
                .name(TEST_USER_NAME)
                .password(TEST_USER_PASSWORD)
                .email(TEST_USER_EMAIL)
                .build();
        String userJson = objectMapper.writeValueAsString(user);

        doPost(API_USERS, userJson);


        mockMvc.perform(get(API_AUTH)
                        .header(USERNAME, TEST_USER_NAME)
                        .header(PASSWORD, TEST_USER_PASSWORD)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}