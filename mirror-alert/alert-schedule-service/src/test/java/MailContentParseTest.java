import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailContentParseTest {

    /**
     * 正则提取
     * @param regex 正则表达式
     * @param text 待解析字符串
     * @return
     */
    private static String regExtract(String regex, String text) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        try {
            while (matcher.find()) {
                return matcher.group();
            }
        } catch (Exception e) {
            System.out.println("#==正则匹配出错！正则: "+regex+", 文本: {}");
        }
        return null;
    }

    public static void main(String[] args) {
        String regex = "body\\s\\{[\\s\\S]*?\\}";

        String content = "body { line-height: 1.5; }body { font-size: 12pt; font-family: 'Adobe 宋体 Std L'; color: rgb(0, 0, 0); line-height: 1.5; }body { font-size: 12pt; font-family: 'Adobe 宋体 Std L'; color: rgb(0, 0, 0); line-height: 1.5; }\n" +
                "大家好：监控告警产生以下警告，请相关人检查，谢谢！2019-06-18 16:20:00:null--主机【10.121.70.121】产生【低】告警:可用内存超过2G】，【告警值：7.93 GB请联系相关人处理】【告警ID10061818机房位置：null】\n" +
                "\n" +
                "wang@osa.com\n";
        Pattern p_script = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(content);
        String text = m_script.replaceAll("");
        System.out.println(text);
    }
}
