import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JunitCommonTest {

    @Test
    public void testTime() {
System.out.println(getTime("2019-04-02T11:31:26.190360785+08:00"));
    }
    public static Date getTime(String time) {
        String s1 = (time.split("[.]"))[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date newtime = null;
        try {
            newtime = sdf.parse(s1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newtime;
    }
}
