package pl.darenie.dns.test.api;

import org.apache.commons.lang3.RandomStringUtils;
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
import pl.darenie.dns.model.rest.request.UserCash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BillResourceTest {

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
    public void addAndGetBill()  {
        String billName = RandomStringUtils.random(10, true, false);
        UserDTO user = loginHelper.getUserDTO(restTemplate, authHeader);
        BillDTO bill = prepareBill(user, billName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);
        HttpEntity<BillDTO> entity = new HttpEntity<>(bill, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity("/bill", entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<BillDTO[]> getResponse = restTemplate.exchange("/bill", HttpMethod.GET, getEntity, BillDTO[].class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        List<BillDTO> bills = new ArrayList<>(Arrays.asList(getResponse.getBody()));

        assertTrue(bills.stream().anyMatch(e -> e.getName().equals(billName)));
    }

    @Test
    public void addBillAndGetSettlement() throws InterruptedException {
        String billName = RandomStringUtils.random(10, true, false);
        UserDTO user = loginHelper.getUserDTO(restTemplate, authHeader);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);

        HttpEntity<Void> getOldEntity = new HttpEntity<>(headers);
        ResponseEntity<SettlementDTO[]> getOldResponse = restTemplate.exchange("/settlement/dues", HttpMethod.GET, getOldEntity, SettlementDTO[].class);
        assertEquals(HttpStatus.OK, getOldResponse.getStatusCode());
        List<SettlementDTO> oldDues = new ArrayList<>(Arrays.asList(getOldResponse.getBody()));

        BillDTO bill = prepareBill(user, billName);
        HttpEntity<BillDTO> entity = new HttpEntity<>(bill, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity("/bill", entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Thread.sleep(10000L);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<SettlementDTO[]> getResponse = restTemplate.exchange("/settlement/dues", HttpMethod.GET, getEntity, SettlementDTO[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        List<SettlementDTO> newDues = new ArrayList<>(Arrays.asList(getResponse.getBody()));

        assertTrue(newDues.size() > oldDues.size());
    }

    @Test
    public void addBillAndGetSettlementDebt() throws InterruptedException {
        String billName = RandomStringUtils.random(10, true, false);
        UserDTO user = loginHelper.getUserDTO(restTemplate, authHeader);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization-Firebase", authHeader);

        HttpEntity<Void> getOldEntity = new HttpEntity<>(headers);
        ResponseEntity<SettlementDTO[]> getOldResponse = restTemplate.exchange("/settlement/unpaid", HttpMethod.GET, getOldEntity, SettlementDTO[].class);
        assertEquals(HttpStatus.OK, getOldResponse.getStatusCode());
        List<SettlementDTO> oldDues = new ArrayList<>(Arrays.asList(getOldResponse.getBody()));

        BillDTO bill = prepareBill(user, billName);
        HttpEntity<BillDTO> entity = new HttpEntity<>(bill, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity("/bill", entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Thread.sleep(10000L);

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<SettlementDTO[]> getResponse = restTemplate.exchange("/settlement/unpaid", HttpMethod.GET, getEntity, SettlementDTO[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        List<SettlementDTO> newDues = new ArrayList<>(Arrays.asList(getResponse.getBody()));

        assertTrue(newDues.size() == oldDues.size());
    }

    private BillDTO prepareBill(UserDTO user, String billName) {
        UserCash c1 = new UserCash("test_user_token_1", 15.0);
        UserCash c2 = new UserCash("test_user_token_2", 15.0);
        UserCash c3 = new UserCash(user.getFirebaseToken(), 15.0);
        List<UserCash> chargers = new ArrayList<>(Arrays.asList(c1, c2, c3));
        UserCash p1 = new UserCash(user.getFirebaseToken(), 45.0);
        List<UserCash> payers = new ArrayList<>(Arrays.asList(p1));
        return new BillDTO.Builder()
                .name(billName)
                .payment(45.0)
                .priority(BillPriority.MEDIUM)
                .ownerId(user.getFirebaseToken())
                .chargers(chargers)
                .payers(payers)
                .build();
    }


}
