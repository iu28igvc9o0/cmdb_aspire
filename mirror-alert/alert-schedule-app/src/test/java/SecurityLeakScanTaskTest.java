import com.aspire.mirror.alert.server.task.leakScan.SecurityLeakScanTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityLeakScanTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(SecurityLeakScanTaskTest.class);
//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private SecurityLeakScanTask securityLeakScanTask;

    @Test
    public void leakScanTest(){
        securityLeakScanTask.scan(new Date());
    }
}
