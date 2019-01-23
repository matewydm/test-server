package pl.darenie.dns.test.api;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.darenie.dns.model.dto.UserDTO;
import pl.darenie.dns.test.model.LoginRequest;
import pl.darenie.dns.test.model.LoginResponse;


public class LoginHelper {

    public String login(String url, String email, String password) {

        RestTemplate restTemplate =  new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setReturnSecureToken(true);

        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity( url , entity , LoginResponse.class );

        return response.getBody().getIdToken();
    }

    public UserDTO getUserDTO(TestRestTemplate restTemplate, String authHeader){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> response = restTemplate.exchange("/user", HttpMethod.GET, entity, UserDTO.class);
        return response.getBody();
    }
}
