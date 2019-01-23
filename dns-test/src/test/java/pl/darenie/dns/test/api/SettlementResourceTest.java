package pl.darenie.dns.test.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.darenie.dns.model.dto.BillDTO;
import pl.darenie.dns.model.dto.SettlementDTO;
import pl.darenie.dns.model.dto.UserDTO;
import pl.darenie.dns.model.enums.BillPriority;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.rest.request.SettlementRequest;
import pl.darenie.dns.model.rest.request.UserCash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SettlementResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private static final String url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyC-hzWiBVQ9o2wmnYHvw6qkgvYVs_dNSCs";
    private String authHeader;
    private LoginHelper loginHelper;

    @Before
    public void setUp() {
        loginHelper = new LoginHelper();
        authHeader = loginHelper.login(url, "test@registration.pl", "password");
    }

    @Test
    public void changeSettlementStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<SettlementDTO[]> getResponse = restTemplate.exchange("/settlement/dues", HttpMethod.GET, getEntity, SettlementDTO[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        List<SettlementDTO> dues = new ArrayList<>(Arrays.asList(getResponse.getBody()));

        SettlementDTO dto = dues.get(0);
        SettlementRequest rq = new SettlementRequest();
        rq.setSettlementId(dto.getId());
        rq.setStatus(SettlementStatus.WAITING_FOR_CONFIRMATION);
        HttpEntity<SettlementDTO> postEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> postResponse = restTemplate.postForEntity("/settlement", postEntity, Object.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        HttpEntity<Void> getNewEntity = new HttpEntity<>(headers);
        ResponseEntity<SettlementDTO[]> getNewResponse = restTemplate.exchange("/settlement/dues", HttpMethod.GET, getNewEntity, SettlementDTO[].class);
        assertEquals(HttpStatus.OK, getNewResponse.getStatusCode());
        List<SettlementDTO> newDues = new ArrayList<>(Arrays.asList(getNewResponse.getBody()));

        assertTrue(newDues.size() < dues.size());
    }

}
