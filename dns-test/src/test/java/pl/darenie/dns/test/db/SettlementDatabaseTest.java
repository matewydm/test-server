package pl.darenie.dns.test.db;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.darenie.dns.DnsApplication;
import pl.darenie.dns.dao.BillRepository;
import pl.darenie.dns.dao.SettlementRepository;
import pl.darenie.dns.dao.UserRepository;
import pl.darenie.dns.jpa.*;
import pl.darenie.dns.model.enums.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = ConfigFileApplicationContextInitializer.class,
        classes = {DnsApplication.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.properties")
public class SettlementDatabaseTest {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private SettlementRepository settlementRepository;

    @Test
    public void saveSettlement() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement settlement = settlementRepository.save(prepareSettlement(bill, charger, payer));
        Settlement found = settlementRepository.getOne(settlement.getId());
        assertThat(found.getBillId()).isEqualTo(bill.getId());
        assertThat(found.getChargerId()).isEqualTo(charger.getFirebaseToken());
        assertThat(found.getPayerId()).isEqualTo(payer.getFirebaseToken());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveBillNoStatusConstraint() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement prepared = prepareSettlement(bill, charger, payer);
        prepared.setStatus(null);
        settlementRepository.save(prepared);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveBillNoChargerConstraint() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement prepared = prepareSettlement(bill, charger, payer);
        prepared.setCharger(null);
        settlementRepository.save(prepared);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveBillNoPayerConstraint() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement prepared = prepareSettlement(bill, charger, payer);
        prepared.setPayer(null);
        settlementRepository.save(prepared);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveBillNoChargeConstraint() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement prepared = prepareSettlement(bill, charger, payer);
        prepared.setCharge(null);
        settlementRepository.save(prepared);
    }

    @Test
    public void selectPayerSettlements() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement settlement = settlementRepository.save(prepareSettlement(bill, charger, payer));
        List<Settlement> list = settlementRepository.findByPayerAndStatus(payer, SettlementStatus.UNPAID);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void selectChargerSettlements() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement settlement = settlementRepository.save(prepareSettlement(bill, charger, payer));
        List<Settlement> list = settlementRepository.findByPayerAndStatus(charger, SettlementStatus.PAID);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void selectStatusSettlements() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement settlement = settlementRepository.save(prepareSettlement(bill, charger, payer));
        List<Settlement> list = settlementRepository.findByStatus(SettlementStatus.UNPAID);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void removeSettlement() throws ParseException {
        User payer = userRepository.save(prepareUser("test_token_1", "test1@payer.pl", "505399332"));
        User charger = userRepository.save(prepareUser("test_token_2", "test2@charger.pl", "505399232"));
        Bill bill = billRepository.save(prepareBill(charger, payer));
        Settlement preparedSettlement = prepareSettlement(bill, charger, payer);
        Settlement settlement = settlementRepository.save(preparedSettlement);
        settlementRepository.delete(settlement);
        Settlement found = settlementRepository.getOne(settlement.getId());
        Assert.assertNull(found);
    }

    private User prepareUser(String token, String email, String phone) throws ParseException {
        return new User(
                token,
                email,
                UUID.randomUUID().toString(),
                "Imie",
                "Nazwisko",
                formatter.parse("1995-09-25T00:00:00Z"),
                phone,
                "Poland",
                "null",
                Role.USER,
                new BalanceCache(0.0,0.0));
    }

    private Bill prepareBill(User charger, User payer) {
        Bill bill = new Bill();
        bill.setName("test_bill_settlement");
        bill.setPayment(45.0);
        bill.setPriority(BillPriority.MEDIUM);
        bill.setOwner(payer);
        UserHasCashBill p1 = new UserHasCashBill(bill, payer, 45.0, UserCashType.PAYER);
        UserHasCashBill c1 = new UserHasCashBill(bill, charger, 45.0, UserCashType.CHARGER);
        Set<UserHasCashBill> hasCashList = new HashSet<>(Arrays.asList(p1,c1));
        bill.setUserHasCashBill(hasCashList);
        return bill;
    }
    private Settlement prepareSettlement(Bill bill, User charger, User payer){
        Settlement settlement = new Settlement();
        settlement.setBill(bill);
        settlement.setCharge(45.0);
        settlement.setCharger(charger);
        settlement.setPayer(payer);
        settlement.setStatus(SettlementStatus.UNPAID);
        settlement.setVisibility(Visibility.VISIBLE);
        return settlement;
    }

}