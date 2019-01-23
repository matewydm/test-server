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
import pl.darenie.dns.dao.UserRepository;
import pl.darenie.dns.jpa.BalanceCache;
import pl.darenie.dns.jpa.Bill;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.jpa.UserHasCashBill;
import pl.darenie.dns.model.enums.BillPriority;
import pl.darenie.dns.model.enums.Role;
import pl.darenie.dns.model.enums.UserCashType;

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
public class BillDatabaseTest {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillRepository billRepository;

    @Test
    public void saveBill() throws ParseException {
        User user = userRepository.save(prepareUser());
        Bill bill = billRepository.save(prepareBill(user));
        Bill foundBill = billRepository.findById(bill.getId());
        assertThat(foundBill.getName()).isEqualTo("test_bill_1");
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveBillNoNameConstraint() throws ParseException {
        User user = userRepository.save(prepareUser());
        Bill preparedBill = prepareBill(user);
        preparedBill.setName(null);
        billRepository.save(preparedBill);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveBillNoPaymentConstraint() throws ParseException {
        User user = userRepository.save(prepareUser());
        Bill preparedBill = prepareBill(user);
        preparedBill.setPayment(null);
        billRepository.save(preparedBill);
    }

    @Test
    public void selectOwnerBills() throws ParseException {
        User user = userRepository.save(prepareUser());
        Bill preparedBill = prepareBill(user);
        billRepository.save(preparedBill);
        List<Bill> bills = billRepository.findBillHistory(user.getFirebaseToken());
        Assert.assertEquals(bills.size(),1);
    }

    @Test
    public void selectQueryUserBills() throws ParseException {
        User user = userRepository.save(prepareUser());
        Bill preparedBill = prepareBill(user);
        billRepository.save(preparedBill);
        List<Bill> bills = billRepository.findDistinctByOwnerId(user.getFirebaseToken());
        Assert.assertEquals(bills.size(),1);
    }

    @Test
    public void removeBill() throws ParseException {
        User user = userRepository.save(prepareUser());
        Bill bill = billRepository.save(prepareBill(user));
        Bill foundBill = billRepository.findById(bill.getId());
        assertThat(foundBill.getName()).isEqualTo("test_bill_1");
        billRepository.delete(foundBill);
        Bill deletedBill = billRepository.findById(bill.getId());
        Assert.assertNull(deletedBill);
    }

    private User prepareUser() throws ParseException {
        return new User(
                "Token",
                "email",
                UUID.randomUUID().toString(),
                "Imie",
                "Nazwisko",
                formatter.parse("1995-09-25T00:00:00Z"),
                "505299959",
                "Poland",
                "null",
                Role.USER,
                new BalanceCache(0.0,0.0));
    }

    private Bill prepareBill(User user)  {
        Bill bill = new Bill();
        bill.setName("test_bill_1");
        bill.setPayment(45.0);
        bill.setPriority(BillPriority.MEDIUM);
        bill.setOwner(user);
        UserHasCashBill p1 = new UserHasCashBill(bill, user, 45.0, UserCashType.PAYER);
        UserHasCashBill c1 = new UserHasCashBill(bill, user, 45.0, UserCashType.CHARGER);
        Set<UserHasCashBill> hasCashList = new HashSet<>(Arrays.asList(p1,c1));
        bill.setUserHasCashBill(hasCashList);
        return bill;
    }

}