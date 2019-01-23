package pl.darenie.dns.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.darenie.dns.core.CyclicController;
import pl.darenie.dns.core.SettlementController;
import pl.darenie.dns.core.UserController;
import pl.darenie.dns.model.dto.CyclicDTO;
import pl.darenie.dns.model.enums.CyclicType;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    @Autowired
    private UserController userController;
    @Autowired
    private CyclicController cyclicController;

    @Async
    @Scheduled(cron = "0 */15 * * * ?")
    @PostConstruct
    public void processCyclicBill() {
        CyclicDTO cyclicMonth = setTime(CyclicType.MONTH);
        CyclicDTO cyclicWeek = setTime(CyclicType.WEEK);
        cyclicController.processCyclicBills(cyclicMonth);
        cyclicController.processCyclicBills(cyclicWeek);
    }

    @Async
    @Scheduled(cron = "1 */15 * * * ?")
    @PostConstruct
    public void processCyclicSettlements() {
        CyclicDTO cyclicMonth = setTime(CyclicType.MONTH);
        CyclicDTO cyclicWeek = setTime(CyclicType.WEEK);
        cyclicController.processCyclicAccountings(cyclicMonth);
        cyclicController.processCyclicAccountings(cyclicWeek);
    }

    @Scheduled(cron = "0 0 20 * * ?")
    @Async
    public void processUnpaidSettlements() {
        userController.notifyChargers();
    }

    private CyclicDTO setTime(CyclicType type) {
        CyclicDTO cyclicDTO = new CyclicDTO();
        String time = dateFormat.format(new Date());
        Integer hour = Integer.parseInt(time.split(":")[0]);
        Integer minute = Integer.parseInt(time.split(":")[1]);
        Calendar calendar = Calendar.getInstance();
        int day;
        if (type == CyclicType.WEEK) {
            day = calendar.get(Calendar.DAY_OF_WEEK);
        } else {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        cyclicDTO.setType(type);
        cyclicDTO.setDay(day);
        cyclicDTO.setHour(hour);
        cyclicDTO.setMinute(minute);
        return cyclicDTO;
    }

//    @Async
//    @Scheduled(cron = "0 * * * * ?")
//    @PostConstruct
//    public void cacheBalance() {
//        settlementController.cacheUserBalance();
//        settlementController.cacheFriendBalance();
//    }
}
