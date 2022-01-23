package com.scrum.planningandretro.resource;

import com.scrum.planningandretro.converter.UserConverter;
import com.scrum.planningandretro.converter.UserConverterImpl;
import com.scrum.planningandretro.model.User;
import com.scrum.planningandretro.repository.UserRepository;
import com.scrum.planningandretro.service.impl.UserServiceImpl;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "local.server.port=8080")
@ActiveProfiles("test")
class UserResourceTest {

    @MockBean
    private UserRepository userRepository;

    private Integer serverPort = 8080;

    private String userResource = "http://localhost:" + serverPort;

    private PageRequest defaultPageable = PageRequest.of(0, 5);

    @BeforeEach
    public void setup() {
        RestAssured.port = 8080;

        List<User> users = List.of(
                new User(1L, "Richard", "richard@scrum.com"),
                new User(2L, "Leonardo", "leonardo@scrum.com"),
                new User(3L, "Gilberto", "gilberto@scrum.com")
        );
        Page<User> paginatedUsers = new PageImpl(users, defaultPageable, 5);

        Mockito.when(userRepository.findAll(defaultPageable)).thenReturn(paginatedUsers);
    }

    @Test
    void mustRetrieveUsersPaginated() {
        RestAssured.given()
                .when()
                    .get(userResource + "/v1/users")
                .then()
                    .assertThat()
                    .body("content", hasSize(3))
                    .body("page", is(0))
                    .body("per_page", is(5));
    }
}