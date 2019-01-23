package pl.darenie.dns.test.api;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.darenie.dns.model.dto.UserDTO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private static final String url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyC-hzWiBVQ9o2wmnYHvw6qkgvYVs_dNSCs";
    private String authHeader;

    @Before
    public void setUp() {
        authHeader = new LoginHelper().login(url, "test@registration.pl", "password");
    }

    @Test
    public void createClient() {
        String randomEmail = RandomStringUtils.random(6, true, true);
        String phone = RandomStringUtils.random(9, false, true);
        UserDTO userRequest = new UserDTO.Builder()
                .email(randomEmail + "@test.pl")
                .firstname(randomEmail)
                .lastname("test")
                .birthDate("1995-09-25T00:00:00Z")
                .country("Poland")
                .phoneNumber(phone)
                .build();
        userRequest.setPassword("password");
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity("/user/register", userRequest, Object.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void createClientError() {
        String randomEmail = RandomStringUtils.random(6, true, true);
        String phone = RandomStringUtils.random(9, false, true);
        UserDTO userRequest = new UserDTO.Builder()
                .firstname(randomEmail)
                .lastname("test")
                .birthDate("1995-09-25T00:00:00Z")
                .country("Poland")
                .phoneNumber(phone)
                .build();
        userRequest.setPassword("password");
        ResponseEntity<Object> responseEntity =
                restTemplate.postForEntity("/user/register", userRequest, Object.class);
        assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getUserDataStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange("/user", HttpMethod.GET, entity, UserDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getUserDataStatusUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange("/user", HttpMethod.GET, entity, UserDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getUserDataBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange("/user", HttpMethod.GET, entity, UserDTO.class);

        assertEquals("test@registration.pl", response.getBody().getEmail());
    }


}
