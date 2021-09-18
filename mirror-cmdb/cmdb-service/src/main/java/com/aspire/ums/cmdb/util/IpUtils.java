package com.aspire.ums.cmdb.util;

import com.google.common.collect.Maps;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/13 19:59
 */
public class IpUtils {
    /**
     * @param url1
     * @param url2
     * @return url1包含url2返回true, 否则返回false
     */
    public static boolean urlCompare(String url1, String url2) {
        // 除了*以外的所有特殊字符转义为普通字符处理
        url1 = url1.replace("\\", "\\").replace("+", "\\+").replace("|", "\\|").replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)").replace("^", "\\^").replace("$", "\\$").replace("[", "\\[")
                .replace("]", "\\]").replace("?", "\\?").replace(",", "\\,").replace(".", "\\.").replace("&", "\\&");
        StringBuffer a = new StringBuffer(url1);
        String temp1 = "";
        String temp2 = url2;
        int startIndex1 = a.indexOf("*");
        int endIndex1 = a.lastIndexOf("*");
        if (startIndex1 < 0) {
            // 字符串无*
            temp1 = a.toString();
        } else if (startIndex1 == endIndex1) {
            // 只有一个*，判断是否在首位
            if (startIndex1 == 0) {
                temp1 = a.toString().replace("*", "((?!/).)*");
            } else {
                // 不在首位
                temp1 = a.toString().replace("*", ".*");
            }
        } else {
            // 有两个*号
            // 第一个*号在首位
            if (startIndex1 == 0) {
                temp1 = a.replace(endIndex1, endIndex1 + 1, ".*").replace(startIndex1, startIndex1 + 1, "((?!/).)*").toString();
            } else {
                temp1 = a.replace(endIndex1, endIndex1 + 1, ".*").replace(startIndex1, startIndex1 + 1, ".*").toString();
            }

        }
        Pattern pattern = Pattern.compile(temp1);
        Matcher m = pattern.matcher(temp2);
        return m.matches();
    }

    public static boolean isURL(String url) {
        Pattern pattern1 = Pattern.compile("^((?!\\s)(?!\\*).)+(\\*){0,1}((?!\\s)(?!\\*).)+(\\*){0,1}$");
        Pattern pattern2 = Pattern.compile("^(\\*){0,1}((?!\\s)(?!\\*).)+(\\*){0,1}$");
        Pattern pattern3 = Pattern.compile("^(\\*){0,1}((?!\\s)(?!\\*).)+(\\*){0,1}((?!\\s)(?!\\*).)+$");
        Matcher m1 = pattern1.matcher(url);
        Matcher m2 = pattern2.matcher(url);
        Matcher m3 = pattern3.matcher(url);
        if (m1.matches()) {
            return true;
        } else if (m2.matches()) {
            return true;
        } else if (m3.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param ipaddr
     * @return 第一个布尔值表示是否符合正则表达式，第二个布尔值表示是否是合理的IP值
     */
    public static Object[] isIPAddress(String ipaddr) {
        Object[] result = new Object[]{false, false};
        boolean flag = false;
        boolean flag2 = false;
        Pattern pattern = Pattern.compile(
                "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])(/\\d+){0,1}");
        Matcher m = pattern.matcher(ipaddr);
        flag = m.matches();
        if (!flag) {
            result = new Object[]{false, false};
        } else {
            String[] ips = ipaddr.split("/");
            if (ips.length == 1) {
                flag2 = true;
            } else {
                Pattern pattern2 = Pattern.compile("^\\d{1,2}$");
                Matcher m2 = pattern2.matcher(ips[1]);
                int temp = Integer.parseInt(ips[1]);
                if (m2.matches() && temp < 32 && temp > 0) {
                    String[] t = ips[0].split("\\.");
                    int a = 32 - Integer.parseInt(ips[1]);
                    if (a <= 8) {
                        int num = (int) Math.pow(2, a);
                        if (Integer.parseInt(t[3]) % num != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 16 && a > 8) {
                        int num = (int) Math.pow(2, (a - 8));
                        int num1 = (int) Math.pow(2, 8);
                        if (Integer.parseInt(t[2]) % num != 0 || Integer.parseInt(t[3]) % num1 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 24 && a > 16) {
                        int num = (int) Math.pow(2, (a - 16));
                        int num1 = (int) Math.pow(2, 8);
                        int num2 = (int) Math.pow(2, 8);
                        if (Integer.parseInt(t[1]) % num != 0 || Integer.parseInt(t[2]) % num1 != 0
                                || Integer.parseInt(t[3]) % num2 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 32) {
                        int num = (int) Math.pow(2, (a - 24));
                        int num1 = (int) Math.pow(2, 8);
                        int num2 = (int) Math.pow(2, 8);
                        int num3 = (int) Math.pow(2, 8);
                        if (Integer.parseInt(t[0]) % num != 0 || Integer.parseInt(t[1]) % num1 != 0
                                || Integer.parseInt(t[2]) % num2 != 0 || Integer.parseInt(t[3]) % num3 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    }

                } else {
                    flag2 = false;
                }
            }
            result = new Object[]{true, flag2};
        }
        return result;
    }

    /**
     * @param ipv6addr
     * @return 第一个布尔值表示是否符合正则表达式，第二个布尔值表示是否是合理的IP值
     */
    public static Object[] isIPV6Address(String ipv6addr) {
        Object[] result = new Object[]{false, false};
        boolean flag = false;
        boolean flag2 = false;
        Pattern pattern = Pattern.compile(
                "^((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4}){1}|:))|(([0-9A-Fa-f]{1,4}:){6}((:[0-9A-Fa-f]{1,4}){1}|:))|(([0-9A-Fa-f]{1,4}:){5}((:[0-9A-Fa-f]{1,4}){1,2}|:))|(([0-9A-Fa-f]{1,4}:){4}((:[0-9A-Fa-f]{1,4}){1,3}|:))|(([0-9A-Fa-f]{1,4}:){3}((:[0-9A-Fa-f]{1,4}){1,4}|:))|(([0-9A-Fa-f]{1,4}:){2}((:[0-9A-Fa-f]{1,4}){1,5}|:))|(([0-9A-Fa-f]{1,4}:){1}((:[0-9A-Fa-f]{1,4}){1,6}|:))|(:((:[0-9A-Fa-f]{1,4}){1,7}|:)))(/\\d+){0,1}$");
        Matcher m = pattern.matcher(ipv6addr);
        flag = m.matches();
        if (!flag) {
            result = new Object[]{false, false};
        } else {
            String[] ips = ipv6addr.split("/");
            if (ips.length == 1) {
                flag2 = false;
            } else {
                Pattern pattern2 = Pattern.compile("^\\d{1,3}$");
                Matcher m2 = pattern2.matcher(ips[1]);
                int temp = Integer.parseInt(ips[1]);
                if ((m2.matches() && temp <= 128 && temp > 0)) {
                    String t[] = new String[8];
                    if (ips[0].length() == 2 && "::".equals(ips[0])) {
                        for (int i = 0; i < t.length; i++) {
                            t[i] = "0000";
                        }
                    } else if (ips[0].length() > 2 && "::".equals(ips[0].substring(0, 2))) {
                        String[] t_temp = ips[0].split("\\:");
                        int len_temp = t_temp.length;
                        int zero_num = 2 + (8 - len_temp);
                        int non_zero_index_start = 2;
                        for (int i = 0; i < t.length; i++) {
                            if (i < zero_num) {
                                t[i] = "0000";
                            } else {
                                t[i] = t_temp[non_zero_index_start];
                                non_zero_index_start = non_zero_index_start + 1;
                            }
                        }
                    } else if (ips[0].length() > 2 && "::".equals(ips[0].substring(ips[0].length() - 2))) {
                        String[] t_temp = ips[0].split("\\:");
                        int len_temp = t_temp.length;
                        for (int i = 0; i < t.length; i++) {
                            if (i < len_temp) {
                                t[i] = t_temp[i];
                            } else {
                                t[i] = "0000";
                            }
                        }
                    } else {
                        String[] t_temp = ips[0].split("\\:");
                        int len_temp = t_temp.length;
                        if (len_temp == 8) {
                            for (int i = 0; i < t.length; i++) {
                                t[i] = t_temp[i];
                            }
                        } else {
                            int zero_index_start = 0;
                            for (int i = 0; i < t_temp.length; i++) {
                                if (StringUtils.isEmpty(t_temp[i])) {
                                    zero_index_start = i;
                                    break;
                                }
                            }
                            int zero_num = 8 - len_temp + 1;
                            int none_zero_re_start = zero_index_start + zero_num;
                            int temp_none_zero_re_start = zero_index_start + 1;
                            for (int j = 0; j < t.length; j++) {
                                if (j < zero_index_start) {
                                    t[j] = t_temp[j];
                                } else if (zero_index_start <= j && j < (none_zero_re_start)) {
                                    t[j] = "0000";
                                } else if (j >= none_zero_re_start) {
                                    t[j] = t_temp[temp_none_zero_re_start];
                                    temp_none_zero_re_start = temp_none_zero_re_start + 1;
                                }
                            }
                        }
                    }
                    for (int n = 0; n < t.length; n++) {
                        if (t[n].length() == 1) {
                            t[n] = "000" + t[n];
                        } else if (t[n].length() == 2) {
                            t[n] = "00" + t[n];
                        } else if (t[n].length() == 3) {
                            t[n] = "0" + t[n];
                        }
                    }
                    long a = 128 - Long.parseLong(ips[1]);
                    if (a <= 16 && a > 0) {
                        long num = (long) Math.pow(2, (a));
                        if (Long.parseLong(t[7], 16) % num != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 32 && a > 16) {
                        long num = (long) Math.pow(2, (a - 16));
                        long num1 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[6], 16) % num != 0 || Long.parseLong(t[7], 16) % num1 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 48 && a > 32) {
                        long num = (long) Math.pow(2, (a - 32));
                        long num1 = (long) Math.pow(2, 16);
                        long num2 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[5], 16) % num != 0 || Long.parseLong(t[6], 16) % num1 != 0
                                || Long.parseLong(t[7], 16) % num2 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 64 && a > 48) {
                        long num = (long) Math.pow(2, (a - 48));
                        long num1 = (long) Math.pow(2, 16);
                        long num2 = (long) Math.pow(2, 16);
                        long num3 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[4], 16) % num != 0 || Long.parseLong(t[5], 16) % num1 != 0
                                || Long.parseLong(t[6], 16) % num2 != 0 || Long.parseLong(t[7], 16) % num3 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 80 && a > 64) {
                        long num = (long) Math.pow(2, (a - 64));
                        long num1 = (long) Math.pow(2, 16);
                        long num2 = (long) Math.pow(2, 16);
                        long num3 = (long) Math.pow(2, 16);
                        long num4 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[3], 16) % num != 0 || Long.parseLong(t[4], 16) % num1 != 0
                                || Long.parseLong(t[5], 16) % num2 != 0 || Long.parseLong(t[6], 16) % num3 != 0
                                || Long.parseLong(t[7], 16) % num4 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 96 && a > 80) {
                        long num = (long) Math.pow(2, (a - 80));
                        long num1 = (long) Math.pow(2, 16);
                        long num2 = (long) Math.pow(2, 16);
                        long num3 = (long) Math.pow(2, 16);
                        long num4 = (long) Math.pow(2, 16);
                        long num5 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[2], 16) % num != 0 || Long.parseLong(t[3], 16) % num1 != 0
                                || Long.parseLong(t[4], 16) % num2 != 0 || Long.parseLong(t[5], 16) % num3 != 0
                                || Long.parseLong(t[6], 16) % num4 != 0 || Long.parseLong(t[7], 16) % num5 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 112 && a > 96) {
                        long num = (long) Math.pow(2, (a - 96));
                        long num1 = (long) Math.pow(2, 16);
                        long num2 = (long) Math.pow(2, 16);
                        long num3 = (long) Math.pow(2, 16);
                        long num4 = (long) Math.pow(2, 16);
                        long num5 = (long) Math.pow(2, 16);
                        long num6 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[1], 16) % num != 0 || Long.parseLong(t[2], 16) % num1 != 0
                                || Long.parseLong(t[3], 16) % num2 != 0 || Long.parseLong(t[4], 16) % num3 != 0
                                || Long.parseLong(t[5], 16) % num4 != 0 || Long.parseLong(t[6], 16) % num5 != 0
                                || Long.parseLong(t[7], 16) % num6 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a <= 128 && a > 112) {
                        long num = (long) Math.pow(2, (a - 112));
                        long num1 = (long) Math.pow(2, 16);
                        long num2 = (long) Math.pow(2, 16);
                        long num3 = (long) Math.pow(2, 16);
                        long num4 = (long) Math.pow(2, 16);
                        long num5 = (long) Math.pow(2, 16);
                        long num6 = (long) Math.pow(2, 16);
                        long num7 = (long) Math.pow(2, 16);
                        if (Long.parseLong(t[0], 16) % num != 0 || Long.parseLong(t[1], 16) % num1 != 0
                                || Long.parseLong(t[2], 16) % num2 != 0 || Long.parseLong(t[3], 16) % num3 != 0
                                || Long.parseLong(t[4], 16) % num4 != 0 || Long.parseLong(t[5], 16) % num5 != 0
                                || Long.parseLong(t[6], 16) % num6 != 0 || Long.parseLong(t[7], 16) % num7 != 0) {
                            flag2 = false;
                        } else {
                            flag2 = true;
                        }
                    } else if (a == 0) {
                        flag2 = true;
                    }
                } else {
                    flag2 = false;
                }
            }
            result = new Object[]{true, flag2};
        }
        return result;
    }

    public static long getIplong(String ip) {
        ip = ip.trim();
        String[] ips = ip.split("\\.");
        long ip1 = Integer.parseInt(ips[0]);
        long ip2 = Integer.parseInt(ips[1]);
        long ip3 = Integer.parseInt(ips[2]);
        long ip4 = Integer.parseInt(ips[3]);
        long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;
        return ip2long;
    }

    public static BigInteger ipv6toBigInt(String ipv6) {
        int compressIndex = ipv6.indexOf("::");
        if (compressIndex != -1) {
            String part1s = ipv6.substring(0, compressIndex);
            String part2s = ipv6.substring(compressIndex + 1);
            BigInteger part1 = ipv6toBigInt(part1s);
            BigInteger part2 = ipv6toBigInt(part2s);
            int part1hasDot = 0;
            char ch[] = part1s.toCharArray();
            for (char c : ch) {
                if (c == ':') {
                    part1hasDot++;
                }
            }
            // ipv6 has most 7 dot
            return part1.shiftLeft(16 * (7 - part1hasDot)).add(part2);
        }
        String[] str = ipv6.split(":");
        BigInteger big = BigInteger.ZERO;
        for (int i = 0; i < str.length; i++) {
            // ::1
            if (str[i].isEmpty()) {
                str[i] = "0";
            }
            big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16)).shiftLeft(16 * (str.length - i - 1)));
        }
        return big;
    }

    public static String bigInt2ipv6(BigInteger big) {
        String str = "";
        BigInteger ff = BigInteger.valueOf(0xffff);
        for (int i = 0; i < 8; i++) {
            str = big.and(ff).toString(16) + ":" + str;
            big = big.shiftRight(16);
        }
        // the last :
        str = str.substring(0, str.length() - 1);
        return str.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
    }

    /**
     * @param segmentStr [ip地址]/[掩码]
     * @return 根据网段生成该网段的所有ip
     */
    public static List<String> generateIp(String segmentStr) {
        List<String> list = new ArrayList<String>();
        String[] ipArray = segmentStr.split("/");
        long start = ipToLong(ipArray[0]);
        long number = ipCount(Integer.parseInt(ipArray[1]));
        long lmask = maskToLong(Integer.parseInt(ipArray[1]));
        String startIp = (longToIP((start & lmask)));
        String endIp = (longToIP((start & lmask) + number));

        long start_ip = ipToLong(startIp);
        long endIp_ip = ipToLong(endIp);
        list.add(startIp);
        while (start_ip < endIp_ip) {
            start_ip = start_ip + 1;
            list.add(longToIP(start_ip));
        }
        return list;
    }

    /**
     * 生产对应count数量的ipv6地址，ABCD:EF01:2345:6789:ABCD:EF01:2345:1-（count）
     *
     * @param segmentStr X:X:X:X:X:X:X:X
     * @param count      生产数量
     * @return
     */
    public static List<String> generateIpv6(String segmentStr, int count) {
        List<String> list = new ArrayList<>();
        int lastIndex = segmentStr.lastIndexOf(":");
        String ipTop = segmentStr.substring(0, lastIndex);
        for (int i = 1; i <= count; i++) {
            list.add(String.format("%s:%d", ipTop, i));
        }
        return list;
    }

    public static boolean isNetworkSegment(String segmentStr) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(
                "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])(/\\d+){1}");
        Matcher m = pattern.matcher(segmentStr);
        if (m.matches()) {
            String[] ips = segmentStr.split("/");
            if (ips.length == 2) {
                Pattern pattern2 = Pattern.compile("^\\d{1,2}$");
                Matcher m2 = pattern2.matcher(ips[1]);
                int temp = Integer.parseInt(ips[1]);
                if (m2.matches() && temp <= 32 && temp >= 22) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 校验网段格式是否正确
     *
     * @param segmentStr
     * @return
     */
    public static boolean checkIpMaskReg(String segmentStr) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(
                "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])(/\\d+){1}");
        Matcher m = pattern.matcher(segmentStr);
        flag = m.matches();
        return flag;
    }

    public static boolean checkMaskReg(String segmentStr) {
        boolean flag = false;
        String[] ips = segmentStr.split("/");
        if (ips.length == 2) {
            Pattern pattern2 = Pattern.compile("^\\d{1,2}$");
            Matcher m2 = pattern2.matcher(ips[1]);
            int temp = Integer.parseInt(ips[1]);
            if (m2.matches() && temp <= 32 && temp >= 22) {
                flag = true;
            }
        }
        return flag;
    }

    public static boolean isIpReg(String ipStr) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(
                "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])");
        Matcher m = pattern.matcher(ipStr);
        flag = m.matches();
        return flag;

    }

    public static boolean isPortReg(String portStr) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])");
        Matcher m = pattern.matcher(portStr);
        flag = m.matches();
        return flag;
    }

    /*
     * public static boolean isIpPortReg(String ipPortStr) { boolean flag = false; Pattern pattern = Pattern.compile(
     * "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])(/)([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])}"
     * ); Matcher m = pattern.matcher(ipPortStr); flag = m.matches(); return flag; }
     */

    public static boolean compare(String ip1, String ip2) {
        // 新增需求，ip有一个为空默认有冲突
        if (ip1 == null || ip1.equals("") || ip2 == null || ip2.equals("")) {
            return false;
        }
        // flag表示是否有冲突，true为无冲突，false为有冲突
        boolean flag = false;
        String[] ipArray1 = ip1.split("/");
        String[] ipArray2 = ip2.split("/");
        long min1 = 0L;
        long max1 = 0L;
        long min2 = 0L;
        long max2 = 0L;
        if (ipArray1.length < 2) {
            min1 = getIplong(ipArray1[0]);
            max1 = min1;
        } else {
            long start = ipToLong(ipArray1[0]);
            long number = ipCount(Integer.parseInt(ipArray1[1]));
            long lmask = maskToLong(Integer.parseInt(ipArray1[1]));
            String startIp = (longToIP((start & lmask)));
            String endIp = (longToIP((start & lmask) + number));
            min1 = getIplong(startIp);
            max1 = getIplong(endIp);
        }
        if (ipArray2.length < 2) {
            min2 = getIplong(ipArray2[0]);
            max2 = min2;
        } else {
            long start = ipToLong(ipArray2[0]);
            long number = ipCount(Integer.parseInt(ipArray2[1]));
            long lmask = maskToLong(Integer.parseInt(ipArray2[1]));
            String startIp = (longToIP((start & lmask)));
            String endIp = (longToIP((start & lmask) + number));
            min2 = getIplong(startIp);
            max2 = getIplong(endIp);
        }
        if (max1 < min2 || min1 > max2) {
            flag = true;
        }
        return flag;
    }

    public static boolean compareIPV6(String ip1, String ip2) {
        // 两个ip中只要有一个为空，默认有冲突
        if (ip1 == null || ip1.equals("") || ip2 == null || ip2.equals("")) {
            return false;
        }
        // flag表示是否有冲突，true为无冲突，false为有冲突
        boolean flag = false;
        String[] ipArray1 = ip1.split("/");
        String[] ipArray2 = ip2.split("/");
        BigInteger min1;
        BigInteger max1;
        BigInteger min2;
        BigInteger max2;
        if (ipArray1.length < 2) {
            min1 = ipv6toBigInt(ipArray1[0]);
            max1 = min1;
        } else {
            BigInteger start = ipToBigInt(ipArray1[0]);
            BigInteger number = ipv6Count(Integer.parseInt(ipArray1[1]));
            BigInteger lmask = maskToBigInt(Integer.parseInt(ipArray1[1]));
            String startIp = (bigInt2ipv6((start.and(lmask))));
            String endIp = (bigInt2ipv6((start.and(lmask)).add(number)));
            min1 = ipv6toBigInt(startIp);
            max1 = ipv6toBigInt(endIp);
        }
        if (ipArray2.length < 2) {
            min2 = ipv6toBigInt(ipArray2[0]);
            max2 = min2;
        } else {
            BigInteger start = ipToBigInt(ipArray2[0]);
            BigInteger number = ipv6Count(Integer.parseInt(ipArray2[1]));
            BigInteger lmask = maskToBigInt(Integer.parseInt(ipArray2[1]));
            String startIp = (bigInt2ipv6((start.and(lmask))));
            String endIp = (bigInt2ipv6((start.and(lmask)).add(number)));
            min2 = ipv6toBigInt(startIp);
            max2 = ipv6toBigInt(endIp);
        }
        if (-1 == max1.compareTo(min2) || 1 == min1.compareTo(max2)) {
            flag = true;
        }

        /*
         * if(max1<min2 || min1>max2){ flag = true; }
         */

        return flag;
    }

    /**
     * @param port1 端口号
     * @param port2 端口号
     * @return 两个端口号不为空且不相等返回false，其他返回true
     */
    public static boolean portCompare(String port1, String port2) {
        boolean flag = true;
        if (port1 != null && port2 != null && !port1.equals(port2)) {
            flag = false;
        }
        return flag;
    }

    public static Map<String, Long> getRange(String ip) {
        Map<String, Long> result = Maps.newHashMap();
        String[] ipArray = ip.split("/");
        long min = 0L;
        long max = 0L;
        if (ipArray.length < 2) {
            min = getIplong(ipArray[0]);
            max = min;
        } else {
            long start = ipToLong(ipArray[0]);
            long number = ipCount(Integer.parseInt(ipArray[1]));
            long lmask = maskToLong(Integer.parseInt(ipArray[1]));
            String startIp = (longToIP((start & lmask)));
            String endIp = (longToIP((start & lmask) + number));
            min = getIplong(startIp);
            max = getIplong(endIp);
        }
        result.put("min", min);
        result.put("max", max);
        return result;
    }

    /*
     * ip1 被 ip2完全包含返回true，否则返回false
     */
    public static boolean compareWithChinaMobileDev(String ip1, String sIp, String eIp) {
        boolean flag = false;
        String[] ipArray1 = ip1.split("/");
        long min1 = 0L;
        long max1 = 0L;
        long min2 = 0L;
        long max2 = 0L;
        if (ipArray1.length < 2) {
            min1 = getIplong(ipArray1[0]);
            max1 = min1;
        } else {
            long start = ipToLong(ipArray1[0]);
            long number = ipCount(Integer.parseInt(ipArray1[1]));
            long lmask = maskToLong(Integer.parseInt(ipArray1[1]));
            String startIp = (longToIP((start & lmask)));
            String endIp = (longToIP((start & lmask) + number));
            min1 = getIplong(startIp);
            max1 = getIplong(endIp);
        }
        min2 = getIplong(sIp);
        max2 = getIplong(eIp);
        if (min2 <= min1 && max2 >= max1) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public static boolean compareWithChinaMobileDevByIpv6(String ip1, String sIp, String eIp) {
        boolean flag = false;
        String[] ipArray1 = ip1.split("/");
        BigInteger min1 = BigInteger.ZERO;
        BigInteger max1 = BigInteger.ZERO;
        BigInteger min2 = BigInteger.ZERO;
        BigInteger max2 = BigInteger.ZERO;
        if (ipArray1.length < 2) {
            min1 = ipv6toBigInt(ipArray1[0]);
            max1 = min1;
        } else {
            BigInteger start = ipToBigInt(ipArray1[0]);
            BigInteger number = ipv6Count(Integer.parseInt(ipArray1[1]));
            BigInteger lmask = maskToBigInt(Integer.parseInt(ipArray1[1]));
            String startIp = (bigInt2ipv6((start.and(lmask))));
            String endIp = (bigInt2ipv6((start.and(lmask)).add(number)));
            min1 = ipv6toBigInt(startIp);
            max1 = ipv6toBigInt(endIp);
        }
        min2 = ipv6toBigInt(sIp);
        max2 = ipv6toBigInt(eIp);
        if ((1 == max1.compareTo(min2) || 0 == max1.compareTo(min2)) && (-1 == min1.compareTo(max2) || 0 == min1.compareTo(max2))) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /*
     * ip1 被 ip2完全包含返回true，否则返回false
     */
    public static boolean compareWithChinaMobileIpv6(String ip1, String ip2) {
        boolean flag = false;
        String[] ipArray1 = ip1.split("/");
        String[] ipArray2 = ip2.split("/");
        BigInteger min1 = BigInteger.ZERO;
        BigInteger max1 = BigInteger.ZERO;
        BigInteger min2 = BigInteger.ZERO;
        BigInteger max2 = BigInteger.ZERO;
        if (ipArray1.length < 2) {
            min1 = ipv6toBigInt(ipArray1[0]);
            max1 = min1;
        } else {
            BigInteger start = ipToBigInt(ipArray1[0]);
            BigInteger number = ipv6Count(Integer.parseInt(ipArray1[1]));
            BigInteger lmask = maskToBigInt(Integer.parseInt(ipArray1[1]));
            String startIp = (bigInt2ipv6((start.and(lmask))));
            String endIp = (bigInt2ipv6((start.add(lmask)).add(number)));
            min1 = ipv6toBigInt(startIp);
            max1 = ipv6toBigInt(endIp);
        }
        if (ipArray2.length < 2) {
            min2 = ipv6toBigInt(ipArray2[0]);
            max2 = min2;
        } else {
            BigInteger start = ipToBigInt(ipArray2[0]);
            BigInteger number = ipv6Count(Integer.parseInt(ipArray2[1]));
            BigInteger lmask = maskToBigInt(Integer.parseInt(ipArray2[1]));
            String startIp = (bigInt2ipv6((start.add(lmask))));
            String endIp = (bigInt2ipv6((start.add(lmask)).add(number)));
            min2 = ipv6toBigInt(startIp);
            max2 = ipv6toBigInt(endIp);
        }
        if ((1 == min1.compareTo(min2) || 0 == min1.compareTo(min2)) && (-1 == max1.compareTo(max2) || 0 == max1.compareTo(max2))) {
            // 如果/前缀区间范围比现网目标大,例如2409:8c62:e10:2::/63范围大于2409:8C62:0E10:0002::/64,返回false
            if (Integer.parseInt(ipArray1[1]) < Integer.parseInt(ipArray2[1])) {
                flag = false;
            } else {
                flag = true;
            }
        } else if (Integer.parseInt(ipArray1[1]) >= Integer.parseInt(ipArray2[1])
                && compare2ipv6(ipArray1[0], ipArray2[0], ipArray2[1])) {
            String s = "::";
            String[] split1 = ipArray1[0].split("::");
            if (split1.length > 1) {
                s = s + split1[1];
                BigInteger bigInteger = ipv6toBigInt(s);
                if (0 == max2.compareTo(max1.subtract(bigInteger))) {
                    flag = true;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static boolean compareWithChinaMobile(String ip1, String ip2) {
        boolean flag = false;
        String[] ipArray1 = ip1.split("/");
        String[] ipArray2 = ip2.split("/");
        long min1 = 0L;
        long max1 = 0L;
        long min2 = 0L;
        long max2 = 0L;
        if (ipArray1.length < 2) {
            min1 = getIplong(ipArray1[0]);
            max1 = min1;
        } else {
            long start = ipToLong(ipArray1[0]);
            long number = ipCount(Integer.parseInt(ipArray1[1]));
            long lmask = maskToLong(Integer.parseInt(ipArray1[1]));
            String startIp = (longToIP((start & lmask)));
            String endIp = (longToIP((start & lmask) + number));
            min1 = getIplong(startIp);
            max1 = getIplong(endIp);
        }
        if (ipArray2.length < 2) {
            min2 = getIplong(ipArray2[0]);
            max2 = min2;
        } else {
            long start = ipToLong(ipArray2[0]);
            long number = ipCount(Integer.parseInt(ipArray2[1]));
            long lmask = maskToLong(Integer.parseInt(ipArray2[1]));
            String startIp = (longToIP((start & lmask)));
            String endIp = (longToIP((start & lmask) + number));
            min2 = getIplong(startIp);
            max2 = getIplong(endIp);
        }
        if (min2 <= min1 && max2 >= max1) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public static long ipCount(int mask) {
        long number = 0;
        for (int i = 0; i < 32 - mask; i++) {
            number += Math.pow(2, i);
        }
        return number;
    }

    public static BigInteger ipv6Count(int mask) {
        BigInteger big = BigInteger.ZERO;
        BigInteger b_temp = new BigInteger("2");
        for (int i = 0; i < 128 - mask; i++) {
            big = big.add(b_temp.pow(i));
        }
        return big;
    }

    public static long[] splitIp(String ip) {
        long[] ipArray = new long[4];
        int position1 = ip.indexOf(".");
        int length = ip.length();
        if (length >= 7 && length < 16) {
            if (position1 > 0) {
                int position2 = ip.indexOf(".", position1 + 1);
                if (position2 > 0) {
                    int position3 = ip.indexOf(".", position2 + 1);
                    if (position3 > 0 && position3 < length - 1) {
                        try {
                            ipArray[0] = Long.parseLong(ip.substring(0, position1));
                            ipArray[1] = Long.parseLong(ip.substring(position1 + 1, position2));
                            ipArray[2] = Long.parseLong(ip.substring(position2 + 1, position3));
                            ipArray[3] = Long.parseLong(ip.substring(position3 + 1));
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return ipArray;
    }

    public static BigInteger[] splitIpv6(String ip) {
        BigInteger[] ipArray = new BigInteger[8];

        // String t[] = new String[8];
        if (ip.length() == 2 && "::".equals(ip)) {
            for (int i = 0; i < ipArray.length; i++) {
                long long_temp = Long.parseLong("0000", 16);
                String big_temp = Long.toString(long_temp);
                ipArray[i] = new BigInteger(big_temp);
            }
        } else if (ip.length() > 2 && "::".equals(ip.substring(0, 2))) {
            String[] t_temp = ip.split("\\:");
            int len_temp = t_temp.length;
            int zero_num = 2 + (8 - len_temp);
            int non_zero_index_start = 2;
            for (int i = 0; i < ipArray.length; i++) {
                if (i < zero_num) {
                    long long_temp = Long.parseLong("0000", 16);
                    String big_temp = Long.toString(long_temp);
                    ipArray[i] = new BigInteger(big_temp);
                } else {
                    long long_temp = Long.parseLong(t_temp[non_zero_index_start], 16);
                    String big_temp = Long.toString(long_temp);
                    ipArray[i] = new BigInteger(big_temp);
                    non_zero_index_start = non_zero_index_start + 1;
                }
            }
        } else if (ip.length() > 2 && "::".equals(ip.substring(ip.length() - 2))) {
            String[] t_temp = ip.split("\\:");
            int len_temp = t_temp.length;
            for (int i = 0; i < ipArray.length; i++) {
                if (i < len_temp) {
                    long long_temp = Long.parseLong(t_temp[i], 16);
                    String big_temp = Long.toString(long_temp);
                    ipArray[i] = new BigInteger(big_temp);
                } else {
                    long long_temp = Long.parseLong("0000", 16);
                    String big_temp = Long.toString(long_temp);
                    ipArray[i] = new BigInteger(big_temp);
                }
            }
        } else {
            String[] t_temp = ip.split("\\:");
            int len_temp = t_temp.length;
            if (len_temp == 8) {
                for (int i = 0; i < ipArray.length; i++) {
                    long long_temp = Long.parseLong(t_temp[i], 16);
                    String big_temp = Long.toString(long_temp);
                    ipArray[i] = new BigInteger(big_temp);
                }
            } else {
                int zero_index_start = 0;
                for (int i = 0; i < t_temp.length; i++) {
                    if (StringUtils.isEmpty(t_temp[i])) {
                        zero_index_start = i;
                        break;
                    }
                }
                int zero_num = 8 - len_temp + 1;
                int none_zero_re_start = zero_index_start + zero_num;
                int temp_none_zero_re_start = zero_index_start + 1;
                for (int j = 0; j < ipArray.length; j++) {
                    if (j < zero_index_start) {
                        long long_temp = Long.parseLong(t_temp[j], 16);
                        String big_temp = Long.toString(long_temp);
                        ipArray[j] = new BigInteger(big_temp);
                    } else if (zero_index_start <= j && j < (none_zero_re_start)) {
                        long long_temp = Long.parseLong("0000", 16);
                        String big_temp = Long.toString(long_temp);
                        ipArray[j] = new BigInteger(big_temp);
                    } else if (j >= none_zero_re_start) {
                        long long_temp = Long.parseLong(t_temp[temp_none_zero_re_start], 16);
                        String big_temp = Long.toString(long_temp);
                        ipArray[j] = new BigInteger(big_temp);
                        temp_none_zero_re_start = temp_none_zero_re_start + 1;
                    }
                }
            }
        }
        return ipArray;
    }

    public static long ipToLong(String str) {
        long[] ip = splitIp(str);
        if (ip != null) {
            // ip=*256*256*256+ip2*256*256+ip3*256+ip4
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        } else {
            return 0;
        }
    }

    // IPV6 ipToBigInteger
    public static BigInteger ipToBigInt(String str) {
        BigInteger big = BigInteger.ZERO;
        BigInteger[] ip = splitIpv6(str);
        if (ip != null) {
            for (int i = 0; i < ip.length; i++) {
                big = big.add(ip[i].shiftLeft(16 * (ip.length - i - 1)));
            }
            return big;
        } else {
            return big;
        }
    }

    public static long maskToLong(int mask) {
        long longMask = 0;
        for (int i = 31; i >= 32 - mask; i--) {
            longMask += Math.pow(2, i);
        }
        return longMask;
    }

    public static BigInteger maskToBigInt(int mask) {
        BigInteger bigMask = BigInteger.ZERO;
        BigInteger b_temp = new BigInteger("2");
        for (int i = 127; i >= 128 - mask; i--) {
            bigMask = bigMask.add(b_temp.pow(i));
        }
        return bigMask;
    }

    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(longIp >>> 24));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIp & 0x000000FF));
        return sb.toString();
    }

    /**
     * 该算法 只做一次合并 如果多次合并 请确认遍历深度 重复调用即可
     *
     * @param list
     * @return
     */
    public static List<NetWorkAddress> netWorkUnion(List<NetWorkAddress> list) {
        List<NetWorkAddress> resultList = new ArrayList<NetWorkAddress>();
        // 分組
        Map<Integer, LinkedList<NetWorkAddress>> groups = new HashMap<Integer, LinkedList<NetWorkAddress>>();
        for (NetWorkAddress netWorkAddresses : list) {
            LinkedList<NetWorkAddress> tempList = groups.get(netWorkAddresses.getMask());
            if (tempList == null) {
                tempList = new LinkedList<NetWorkAddress>();
            }
            if (tempList.size() == 0) {
                tempList.add(netWorkAddresses);
            } else {
                if (Long.parseLong(tempList.get(tempList.size() - 1).getStartIp()) < Long
                        .parseLong(netWorkAddresses.getStartIp())) {
                    tempList.add(netWorkAddresses);
                } else if (Long.parseLong(tempList.get(tempList.size() - 1).getStartIp()) >= Long
                        .parseLong(netWorkAddresses.getStartIp())) {
                    // 排序
                    Integer length = new Integer(tempList.size());
                    for (int i = 0; i < tempList.size(); i++) {
                        NetWorkAddress tempAddress = tempList.get(i);
                        if (Long.parseLong(tempAddress.getStartIp()) > Long.parseLong(netWorkAddresses.getStartIp())) {
                            tempList.add(i, netWorkAddresses);
                            break;
                        }
                    }
                    if (length.equals(tempList.size())) {
                        tempList.add(netWorkAddresses);
                    }
                }
            }
            groups.put(netWorkAddresses.getMask(), tempList);
        }
        // 遍历
        Iterator<Map.Entry<Integer, LinkedList<NetWorkAddress>>> it = groups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, LinkedList<NetWorkAddress>> entry = it.next();
            List<NetWorkAddress> netAddresses = entry.getValue();
            if (netAddresses.size() > 1) {
                for (int i = 0; i < netAddresses.size() - 1; i++) {
                    NetWorkAddress address1 = netAddresses.get(i);
                    for (int j = i + 1; j < netAddresses.size(); j++) {
                        NetWorkAddress address2 = netAddresses.get(j);
                        NetWorkAddress address = union(address1, address2);
                        if (address != null) {
                            resultList.add(address);
                            netAddresses.remove(i);
                            netAddresses.remove(j - 1);
                            i--;
                            break;
                        }
                    }
                }
            }
            resultList.addAll(netAddresses);
        }
        return resultList;
    }

    /**
     * 该算法 只做一次合并 如果多次合并 请确认遍历深度 重复调用即可
     *
     * @param list
     * @return
     */
    public static List<NetWorkAddress> netWorkUnionIpv6(List<NetWorkAddress> list) {
        List<NetWorkAddress> resultList = new ArrayList<NetWorkAddress>();
        // 分組
        Map<Integer, LinkedList<NetWorkAddress>> groups = new HashMap<Integer, LinkedList<NetWorkAddress>>();
        for (NetWorkAddress netWorkAddresses : list) {
            LinkedList<NetWorkAddress> tempList = groups.get(netWorkAddresses.getMask());
            if (tempList == null) {
                tempList = new LinkedList<NetWorkAddress>();
            }
            if (tempList.size() == 0) {
                tempList.add(netWorkAddresses);
            } else {
                BigInteger bigStartIp = new BigInteger(tempList.get(tempList.size() - 1).getStartIp());
                BigInteger bigEndIp = new BigInteger(netWorkAddresses.getStartIp());
                if (bigStartIp.compareTo(bigEndIp) == -1) {
                    tempList.add(netWorkAddresses);
                } else if (bigStartIp.compareTo(bigEndIp) == -1 || bigStartIp.compareTo(bigEndIp) == 0) {
                    // 排序
                    Integer length = new Integer(tempList.size());
                    for (int i = 0; i < tempList.size(); i++) {
                        NetWorkAddress tempAddress = tempList.get(i);
                        BigInteger big_start = new BigInteger(tempAddress.getStartIp());
                        BigInteger big_end = new BigInteger(netWorkAddresses.getStartIp());
                        if (big_start.compareTo(big_end) == 1) {
                            tempList.add(i, netWorkAddresses);
                            break;
                        }
                    }
                    if (length.equals(tempList.size())) {
                        tempList.add(netWorkAddresses);
                    }
                }
            }
            groups.put(netWorkAddresses.getMask(), tempList);
        }
        // 遍历
        Iterator<Map.Entry<Integer, LinkedList<NetWorkAddress>>> it = groups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, LinkedList<NetWorkAddress>> entry = it.next();
            List<NetWorkAddress> netAddresses = entry.getValue();
            if (netAddresses.size() > 1) {
                for (int i = 0; i < netAddresses.size() - 1; i++) {
                    NetWorkAddress address1 = netAddresses.get(i);
                    for (int j = i + 1; j < netAddresses.size(); j++) {
                        NetWorkAddress address2 = netAddresses.get(j);
                        NetWorkAddress address = unionIpv6(address1, address2);
                        if (address != null) {
                            resultList.add(address);
                            netAddresses.remove(i);
                            netAddresses.remove(j - 1);
                            i--;
                            break;
                        }
                    }
                }
            }
            resultList.addAll(netAddresses);
        }
        return resultList;
    }

    private static NetWorkAddress union(NetWorkAddress netAdd1, NetWorkAddress netadd4) {
        NetWorkAddress add = null;

        NetWorkAddress add1 = null;
        NetWorkAddress add4 = null;

        if (Long.parseLong(netAdd1.getStartIp()) < Long.parseLong(netadd4.getStartIp())) {
            add1 = netAdd1;
            add4 = netadd4;
        } else if (Long.parseLong(netAdd1.getStartIp()) >= Long.parseLong(netadd4.getStartIp())) {
            add1 = netadd4;
            add4 = netAdd1;
        }
        // url是否符合要求 && (Long.parseLong(add1.getEndIp())%2==0)
        boolean urlFlag = false;
        if (add1 != null && add4 != null) {
            if ((add1.getUrl() == null && add4.getUrl() == null)
                    || (add1.getUrl() != null && add4.getUrl() != null && add1.getUrl().equals(add4.getUrl()))) {
                urlFlag = true;
            }
        }
        long pow_mask = Math.round(Math.pow(2, 32 - (add1.getMask() - 1)));
        if (Long.parseLong(add1.getEndIp()) + 1 == Long.parseLong(add4.getStartIp())
                && Long.parseLong(add1.getStartIp()) % pow_mask == 0 && urlFlag) {
            add = new NetWorkAddress();
            add.setNetAdd(add1.getNetAdd());
            add.setStartIp(add1.getStartIp());
            add.setEndIp(add4.getEndIp());
            add.setMask(add1.getMask() - 1);
            add.setUrl(add1.getUrl());
            // 判断新形成的ip是否合法
            Boolean flag2 = false;
            int a = 32 - add1.getMask();
            String[] t = add.getNetAdd().split("\\.");
            if (a <= 8) {
                int num = (int) Math.pow(2, a);
                if (Integer.parseInt(t[3]) % num != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 16 && a > 8) {
                int num = (int) Math.pow(2, (a - 8));
                int num1 = (int) Math.pow(2, 8);
                if (Integer.parseInt(t[2]) % num != 0 || Integer.parseInt(t[3]) % num1 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 24 && a > 16) {
                int num = (int) Math.pow(2, (a - 16));
                int num1 = (int) Math.pow(2, 8);
                int num2 = (int) Math.pow(2, 8);
                if (Integer.parseInt(t[1]) % num != 0 || Integer.parseInt(t[2]) % num1 != 0 || Integer.parseInt(t[3]) % num2 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 32) {
                int num = (int) Math.pow(2, (a - 24));
                int num1 = (int) Math.pow(2, 8);
                int num2 = (int) Math.pow(2, 8);
                int num3 = (int) Math.pow(2, 8);
                if (Integer.parseInt(t[0]) % num != 0 || Integer.parseInt(t[1]) % num1 != 0 || Integer.parseInt(t[2]) % num2 != 0
                        || Integer.parseInt(t[3]) % num3 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            }
            if (!flag2) {
                return null;
            }
            StringBuffer sb = new StringBuffer();
            // sb.append((add1.getDescr()==null?"":(add1.getDescr()+";")));
            // sb.append((add4.getDescr()==null?"":(add4.getDescr()+";")));
            sb.append((add1.getDescr() == null ? "" : (add1.getDescr() + ",")));
            // sb.append((add4.getDescr()==null?"":(add4.getDescr()+",")));
            sb.append((add4.getDescr() == null ? "" : (add4.getDescr())));
            if (add1.getDescr() == null) {
                sb.append(add1.getNetAdd()).append("/").append(add1.getMask()).append(",");
            }
            if (add4.getDescr() == null) {
                sb.append(add4.getNetAdd()).append("/").append(add4.getMask());
            }
            add.setDescr(sb.toString());
        }
        return add;
    }

    private static NetWorkAddress unionIpv6(NetWorkAddress netAdd1, NetWorkAddress netadd4) {
        NetWorkAddress add = null;

        NetWorkAddress add1 = null;
        NetWorkAddress add4 = null;

        BigInteger big_start_net1 = new BigInteger(netAdd1.getStartIp());
        BigInteger big_start_net4 = new BigInteger(netadd4.getStartIp());
        if (-1 == big_start_net1.compareTo(big_start_net4)) {
            add1 = netAdd1;
            add4 = netadd4;
        } else if (1 == big_start_net1.compareTo(big_start_net4) || 0 == big_start_net1.compareTo(big_start_net4)) {
            add1 = netadd4;
            add4 = netAdd1;
        }
        // url是否符合要求
        boolean urlFlag = false;
        if (add1 != null && add4 != null) {
            if ((add1.getUrl() == null && add4.getUrl() == null)
                    || (add1.getUrl() != null && add4.getUrl() != null && add1.getUrl().equals(add4.getUrl()))) {
                urlFlag = true;
            }
        }
        BigInteger bigEndIp_add_1 = new BigInteger(add1.getEndIp());
        BigInteger bigStartIp = new BigInteger(add4.getStartIp());
        // BigInteger two = new BigInteger("2");
        // long pow_mask = Math.round(Math.pow(2, 32-(add1.getMask()-1)));
        // && (bigEndIp_add_1.remainder(two).compareTo(BigInteger.ZERO)==0)
        long pow_mask = Math.round(Math.pow(2, 128 - (add1.getMask() - 1)));
        BigInteger big_pow_mask = new BigInteger(String.valueOf(pow_mask));
        if ((bigEndIp_add_1.add(BigInteger.ONE)).compareTo(bigStartIp) == 0
                && (bigEndIp_add_1.remainder(big_pow_mask).compareTo(BigInteger.ZERO) == 0) && urlFlag) {
            add = new NetWorkAddress();
            add.setNetAdd(add1.getNetAdd());
            add.setStartIp(add1.getStartIp());
            add.setEndIp(add4.getEndIp());
            add.setMask(add1.getMask() - 1);
            add.setUrl(add1.getUrl());
            // 判断新形成的ip是否合法
            Boolean flag2 = false;
            // int a = 32-add.getMask();

            String t[] = new String[8];
            if (add.getNetAdd().length() == 2 && "::".equals(add.getNetAdd())) {
                for (int i = 0; i < t.length; i++) {
                    t[i] = "0000";
                }
            } else if (add.getNetAdd().length() > 2 && "::".equals(add.getNetAdd().substring(0, 2))) {
                String[] t_temp = add.getNetAdd().split("\\:");
                int len_temp = t_temp.length;
                int zero_num = 2 + (8 - len_temp);
                int non_zero_index_start = 2;
                for (int i = 0; i < t.length; i++) {
                    if (i < zero_num) {
                        t[i] = "0000";
                    } else {
                        t[i] = t_temp[non_zero_index_start];
                        non_zero_index_start = non_zero_index_start + 1;
                    }
                }
            } else if (add.getNetAdd().length() > 2 && "::".equals(add.getNetAdd().substring(add.getNetAdd().length() - 2))) {
                String[] t_temp = add.getNetAdd().split("\\:");
                int len_temp = t_temp.length;
                for (int i = 0; i < t.length; i++) {
                    if (i < len_temp) {
                        t[i] = t_temp[i];
                    } else {
                        t[i] = "0000";
                    }
                }
            } else {
                String[] t_temp = add.getNetAdd().split("\\:");
                int len_temp = t_temp.length;
                if (len_temp == 8) {
                    for (int i = 0; i < t.length; i++) {
                        t[i] = t_temp[i];
                    }
                } else {
                    int zero_index_start = 0;
                    for (int i = 0; i < t_temp.length; i++) {
                        if (StringUtils.isEmpty(t_temp[i])) {
                            zero_index_start = i;
                            break;
                        }
                    }
                    int zero_num = 8 - len_temp + 1;
                    int none_zero_re_start = zero_index_start + zero_num;
                    int temp_none_zero_re_start = zero_index_start + 1;
                    for (int j = 0; j < t.length; j++) {
                        if (j < zero_index_start) {
                            t[j] = t_temp[j];
                        } else if (zero_index_start <= j && j < (none_zero_re_start)) {
                            t[j] = "0000";
                        } else if (j >= none_zero_re_start) {
                            t[j] = t_temp[temp_none_zero_re_start];
                            temp_none_zero_re_start = temp_none_zero_re_start + 1;
                        }
                    }
                }
            }
            for (int n = 0; n < t.length; n++) {
                if (t[n].length() == 1) {
                    t[n] = "000" + t[n];
                } else if (t[n].length() == 2) {
                    t[n] = "00" + t[n];
                } else if (t[n].length() == 3) {
                    t[n] = "0" + t[n];
                }
            }
            long a = 128 - add1.getMask();
            if (a <= 16 && a > 0) {
                long num = (long) Math.pow(2, (a));
                if (Long.parseLong(t[7], 16) % num != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 32 && a > 16) {
                long num = (long) Math.pow(2, (a - 16));
                long num1 = (long) Math.pow(2, 16);
                if (Long.parseLong(t[6], 16) % num != 0 || Long.parseLong(t[7], 16) % num1 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 48 && a > 32) {
                long num = (long) Math.pow(2, (a - 32));
                long num1 = (long) Math.pow(2, 16);
                long num2 = (long) Math.pow(2, 16);
                if (Long.parseLong(t[5], 16) % num != 0 || Long.parseLong(t[6], 16) % num1 != 0
                        || Long.parseLong(t[7], 16) % num2 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 64 && a > 48) {
                long num = (long) Math.pow(2, (a - 48));
                long num1 = (long) Math.pow(2, 16);
                long num2 = (long) Math.pow(2, 16);
                long num3 = (long) Math.pow(2, 16);
                if (Long.parseLong(t[4], 16) % num != 0 || Long.parseLong(t[5], 16) % num1 != 0
                        || Long.parseLong(t[6], 16) % num2 != 0 || Long.parseLong(t[7], 16) % num3 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 80 && a > 64) {
                long num = (long) Math.pow(2, (a - 64));
                long num1 = (long) Math.pow(2, 16);
                long num2 = (long) Math.pow(2, 16);
                long num3 = (long) Math.pow(2, 16);
                long num4 = (long) Math.pow(2, 16);
                if (Long.parseLong(t[3], 16) % num != 0 || Long.parseLong(t[4], 16) % num1 != 0
                        || Long.parseLong(t[5], 16) % num2 != 0 || Long.parseLong(t[6], 16) % num3 != 0
                        || Long.parseLong(t[7], 16) % num4 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 96 && a > 80) {
                long num = (long) Math.pow(2, (a - 80));
                long num1 = (long) Math.pow(2, 16);
                long num2 = (long) Math.pow(2, 16);
                long num3 = (long) Math.pow(2, 16);
                long num4 = (long) Math.pow(2, 16);
                long num5 = (long) Math.pow(2, 16);
                if (Long.parseLong(t[2], 16) % num != 0 || Long.parseLong(t[3], 16) % num1 != 0
                        || Long.parseLong(t[4], 16) % num2 != 0 || Long.parseLong(t[5], 16) % num3 != 0
                        || Long.parseLong(t[6], 16) % num4 != 0 || Long.parseLong(t[7], 16) % num5 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 112 && a > 96) {
                long num = (long) Math.pow(2, (a - 96));
                long num1 = (long) Math.pow(2, 16);
                long num2 = (long) Math.pow(2, 16);
                long num3 = (long) Math.pow(2, 16);
                long num4 = (long) Math.pow(2, 16);
                long num5 = (long) Math.pow(2, 16);
                long num6 = (long) Math.pow(2, 16);
                if (Long.parseLong(t[1], 16) % num != 0 || Long.parseLong(t[2], 16) % num1 != 0
                        || Long.parseLong(t[3], 16) % num2 != 0 || Long.parseLong(t[4], 16) % num3 != 0
                        || Long.parseLong(t[5], 16) % num4 != 0 || Long.parseLong(t[6], 16) % num5 != 0
                        || Long.parseLong(t[7], 16) % num6 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a <= 128 && a > 112) {
                long num = (long) Math.pow(2, (a - 112));
                long num1 = (long) Math.pow(2, 16);
                long num2 = (long) Math.pow(2, 16);
                long num3 = (long) Math.pow(2, 16);
                long num4 = (long) Math.pow(2, 16);
                long num5 = (long) Math.pow(2, 16);
                long num6 = (long) Math.pow(2, 16);
                long num7 = (long) Math.pow(a, 16);
                if (Long.parseLong(t[0], 16) % num != 0 || Long.parseLong(t[1], 16) % num1 != 0
                        || Long.parseLong(t[2], 16) % num2 != 0 || Long.parseLong(t[3], 16) % num3 != 0
                        || Long.parseLong(t[4], 16) % num4 != 0 || Long.parseLong(t[5], 16) % num5 != 0
                        || Long.parseLong(t[6], 16) % num6 != 0 || Long.parseLong(t[7], 16) % num7 != 0) {
                    flag2 = false;
                } else {
                    flag2 = true;
                }
            } else if (a == 0) {
                flag2 = true;
            }
            if (!flag2) {
                return null;
            }
            StringBuffer sb = new StringBuffer();
            sb.append((add1.getDescr() == null ? "" : (add1.getDescr() + ",")));
            sb.append((add4.getDescr() == null ? "" : (add4.getDescr())));
            if (add1.getDescr() == null) {
                sb.append(add1.getNetAdd()).append("/").append(add1.getMask()).append(",");
            }
            if (add4.getDescr() == null) {
                sb.append(add4.getNetAdd()).append("/").append(add4.getMask());
            }
            add.setDescr(sb.toString());
        }
        return add;
    }

    /**
     * 功能：判断是否是一个ip 格式：isIP("192.192.192.1")
     */
    public static boolean isIP(String str) {
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }

    /**
     * 根据掩码位数获取掩码
     */
    public static String getNetMask(String mask) {
        int inetMask = Integer.parseInt(mask);
        if (inetMask > 32) {
            return null;
        }
        // 子网掩码为1占了几个字节
        int num1 = inetMask / 8;
        // 子网掩码的补位位数
        int num2 = inetMask % 8;
        int array[] = new int[4];
        for (int i = 0; i < num1; i++) {
            array[i] = 255;
        }
        for (int i = num1; i < 4; i++) {
            array[i] = 0;
        }
        for (int i = 0; i < num2; num2--) {
            array[num1] += Math.pow(2, 8 - num2);
        }
        String netMask = array[0] + "." + array[1] + "." + array[2] + "." + array[3];
        return netMask;
    }

    /**
     * 根据ip地址和掩码获取起始IP
     */
    public static String getLowAddr(String ipinfo, String netMask) {
        String lowAddr = "";
        int ipArray[] = new int[4];
        int netMaskArray[] = new int[4];
        if (4 != ipinfo.split("\\.").length || "" == netMask) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            try {
                ipArray[i] = Integer.parseInt(ipinfo.split("\\.")[i]);
            } catch (NumberFormatException e) {
                String ip = ipinfo.replaceAll("\n", "");
                ipArray[i] = Integer.parseInt(ip.split("\\.")[i]);
            }
            netMaskArray[i] = Integer.parseInt(netMask.split("\\.")[i]);
            if (ipArray[i] > 255 || ipArray[i] < 0 || netMaskArray[i] > 255 || netMaskArray[i] < 0) {
                return null;
            }
            ipArray[i] = ipArray[i] & netMaskArray[i];
        }
        // 构造最小地址
        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                ipArray[i] = ipArray[i] + 1;
            }
            if ("" == lowAddr) {
                lowAddr += ipArray[i];
            } else {
                lowAddr += "." + ipArray[i];
            }
        }
        return lowAddr;
    }

    /**
     * 根据ip地址和掩码获取终止IP
     */
    public static String getHighAddr(String ipinfo, String netMask) {
        String lowAddr = getLowAddr(ipinfo, netMask);
        int hostNumber = getHostNumber(netMask);
        if ("" == lowAddr || hostNumber == 0) {
            return null;
        }
        int lowAddrArray[] = new int[4];
        for (int i = 0; i < 4; i++) {
            lowAddrArray[i] = Integer.parseInt(lowAddr.split("\\.")[i]);
            if (i == 3) {
                lowAddrArray[i] = lowAddrArray[i] - 1;
            }
        }
        lowAddrArray[3] = lowAddrArray[3] + (hostNumber - 1);
        if (lowAddrArray[3] > 255) {
            int k = lowAddrArray[3] / 256;
            lowAddrArray[3] = lowAddrArray[3] % 256;
            lowAddrArray[2] = lowAddrArray[2] + k;
        }
        if (lowAddrArray[2] > 255) {
            int j = lowAddrArray[2] / 256;
            lowAddrArray[2] = lowAddrArray[2] % 256;
            lowAddrArray[1] = lowAddrArray[1] + j;
            if (lowAddrArray[1] > 255) {
                int k = lowAddrArray[1] / 256;
                lowAddrArray[1] = lowAddrArray[1] % 256;
                lowAddrArray[0] = lowAddrArray[0] + k;
            }
        }
        String highAddr = "";
        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                lowAddrArray[i] = lowAddrArray[i] - 1;
            }
            if ("" == highAddr) {
                highAddr = lowAddrArray[i] + "";
            } else {
                highAddr += "." + lowAddrArray[i];
            }
        }
        return highAddr;
    }

    /**
     * ip转换Long
     */
    public static long ip2long(String ip) {
        String[] ips = ip.split("[.]");
        long num = 16777216L * Long.parseLong(ips[0]) + 65536L * Long.parseLong(ips[1]) + 256 * Long.parseLong(ips[2])
                + Long.parseLong(ips[3]);
        return num;
    }

    /**
     * 实际可用ip数量
     */
    public static int getHostNumber(String netMask) {
        int hostNumber = 0;
        int[] netMaskArray = new int[4];
        for (int i = 0; i < 4; i++) {
            netMaskArray[i] = Integer.parseInt(netMask.split("\\.")[i]);
            if (netMaskArray[i] < 255) {
                hostNumber = (int) (Math.pow(256, 3 - i) * (256 - netMaskArray[i]));
                break;
            }
        }
        return hostNumber;
    }

    /**
     * 判断IP是否在网段范围内.
     *
     * @param ipInfo 网段IP段
     * @param mask   掩码段
     * @param srcIp  需比较的源IP
     * @return 是否
     */
    public static boolean betweenLowAndHighIpAddr(String ipInfo, String mask, String srcIp) {
        String netMask = getNetMask(mask);
        String lowAddr = getLowAddr(ipInfo, netMask);
        String highAddr = getHighAddr(ipInfo, netMask);
        Long low = ip2long(lowAddr);
        Long high = ip2long(highAddr);
        Long compare = ip2long(srcIp);
        return compare <= high && compare >= low;
    }

    public static String getIp(String ipMask) {
        if (!checkIpMaskReg(ipMask)) {
            return null;
        }
        String[] arr = ipMask.split("\\/");
        return arr[0];
    }

    public static String getMask(String ipMask) {
        if (!checkIpMaskReg(ipMask)) {
            return null;
        }
        String[] arr = ipMask.split("\\/");
        return arr[1];
    }

    public static boolean isInRange(String network, String mask) {
        String[] networkips = network.split("\\.");
        int ipAddr = (Integer.parseInt(networkips[0]) << 24) | (Integer.parseInt(networkips[1]) << 16)
                | (Integer.parseInt(networkips[2]) << 8) | Integer.parseInt(networkips[3]);
        int type = Integer.parseInt(mask.replaceAll(".*/", ""));
        int mask1 = 0xFFFFFFFF << (32 - type);
        String maskIp = mask.replaceAll("/.*", "");
        String[] maskIps = maskIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(maskIps[0]) << 24) | (Integer.parseInt(maskIps[1]) << 16)
                | (Integer.parseInt(maskIps[2]) << 8) | Integer.parseInt(maskIps[3]);

        return (ipAddr & mask1) == (cidrIpAddr & mask1);
    }

    /**
     * ipv6前缀匹配 例如2409:8C62:0E10:2::d/128属于局数据2409:8C62:0E10:0002::/64
     *
     * @param api1 需要鉴定的ip
     * @param api2 现网拥有的ip
     * @param mask 前缀
     * @return
     */
    public static Boolean compare2ipv6(String api1, String api2, String mask) {
        api1 = api1.toUpperCase();
        api2 = api2.toUpperCase();
        // 计算ip前缀,标记网络地址,比如 /64就是前缀长度，表示前面64位为网络位，作用就是标示网络地址，
        int i1 = Integer.parseInt(mask);
        int length = i1 / 16;
        int lest = i1 % 16 / 4;
        // 正位判断 比如2409:8C62:0E10:0002::/64 只需要检验2409:8C62:0E10:0002
        Boolean len_flag = false;
        // 余数位判断 比如2409:8C62:0E10:0002::/60 只需要检验2409:8C62:0E10:000分为两部分,正位检验2409:8C62:0E10,余数位检验000
        Boolean lest_flag = false;
        String[] s1 = api1.split(":");
        String[] s2 = api2.split(":");
        if (s1.length <= length || s2.length <= length) {
            // 不符合匹配规则
        } else {
            for (int i = 0; i < length; i++) {
                String res1 = String.format("%4s", s1[i]);
                String res2 = String.format("%4s", s2[i]);
                res1 = res1.replaceAll("\\s", "0");
                res2 = res2.replaceAll("\\s", "0");
                if (res2.equals(res1)) {
                    len_flag = true;
                } else {
                    len_flag = false;
                    break;
                }
            }
            if (0 != lest && StringUtils.isNotEmpty(s2[length]) && StringUtils.isNotEmpty(s1[length])) {
                String res1 = String.format("%4s", s1[length]);
                String res2 = String.format("%4s", s2[length]);
                res1 = res1.replaceAll("\\s", "0");
                res2 = res2.replaceAll("\\s", "0");
                if (res1.substring(0, lest).equals(res2.substring(0, lest))) {
                    lest_flag = true;
                } else {
                    lest_flag = false;
                }
            } else {
                lest_flag = true;
            }
        }
        return len_flag && lest_flag;
    }

    /**
     * 校验子网掩码数是否匹配
     *
     * @param segmentStr
     * @return
     */
    public static boolean checkMaskReg4Bpm(String segmentStr) {
        boolean flag = false;
        String[] ips = segmentStr.split("/");
        if (ips.length == 2) {
            Pattern pattern2 = Pattern.compile("^\\d{1,2}$");
            Matcher m2 = pattern2.matcher(ips[1]);
            int temp = Integer.parseInt(ips[1]);
            if (m2.matches() && temp <= 32 && temp >= 1) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 校验IP段是否符合规范 最后一位在 0-255 之间
     *
     * @param ipBlock IP段：192.168.1.0-255
     * @return true - 符合规范
     */
    public static boolean checkIPRange(String ipBlock) {
        if (StringUtils.isEmpty(ipBlock)) {
            return false;
        }
        String patternOfIPRange = "^(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))-((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))$";
        Pattern pattern = Pattern.compile(patternOfIPRange);
        if (!pattern.matcher(ipBlock).matches()) {
            return false;
        }
        return true;
    }

    /**
     * IPV6地址校验
     *
     * @param ipAddr
     * @return
     */
    public static boolean isValidIpv6Addr(String ipAddr) {
        String regex = "(^((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4}){1}|:))"
                + "|(([0-9A-Fa-f]{1,4}:){6}((:[0-9A-Fa-f]{1,4}){1}|"
                + "((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){5}((:[0-9A-Fa-f]{1,4}){1,2}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){4}((:[0-9A-Fa-f]{1,4}){1,3}"
                + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})){3})|:))|(([0-9A-Fa-f]{1,4}:){3}((:[0-9A-Fa-f]{1,4}){1,4}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){2}((:[0-9A-Fa-f]{1,4}){1,5}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))"
                + "|(([0-9A-Fa-f]{1,4}:){1}((:[0-9A-Fa-f]{1,4}){1,6}"
                + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(:((:[0-9A-Fa-f]{1,4}){1,7}|(:[fF]{4}){0,1}:((22[0-3]|2[0-1][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})"
                + "([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|([0-9]){1,2})){3})|:)))$)";

        if (ipAddr == null) {
            System.out.println("ipv6 addresss is null ");
            return false;
        }
        ipAddr = Normalizer.normalize(ipAddr, Normalizer.Form.NFKC);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddr);

        boolean match = matcher.matches();
        // if (!match) {
        //     // System.out.println("invalid ipv6 addresss = " + ipAddr);
        // }
        return match;
    }

    /**
     *
     * @param ipAddr
     * @return
     */
    public static boolean isValidIpv6Addr2(String ipAddr){
        String regex = "(^((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4}){1}|:))"
                + "|(([0-9A-Fa-f]{1,4}:){6}((:[0-9A-Fa-f]{1,4}){1}|"
                + "((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){5}((:[0-9A-Fa-f]{1,4}){1,2}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){4}((:[0-9A-Fa-f]{1,4}){1,3}"
                + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})){3})|:))|(([0-9A-Fa-f]{1,4}:){3}((:[0-9A-Fa-f]{1,4}){1,4}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(([0-9A-Fa-f]{1,4}:){2}((:[0-9A-Fa-f]{1,4}){1,5}|"
                + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))"
                + "|(([0-9A-Fa-f]{1,4}:){1}((:[0-9A-Fa-f]{1,4}){1,6}"
                + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
                + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
                + "(:((:[0-9A-Fa-f]{1,4}){1,7}|(:[fF]{4}){0,1}:((22[0-3]|2[0-1][0-9]|"
                + "[0-1][0-9][0-9]|([0-9]){1,2})"
                + "([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|([0-9]){1,2})){3})|:)))/([1-2][0-9]|3[0-2]|[1-9]|64)$)";

        if (ipAddr == null) {
            System.out.println("ipv6 addresss is null ");
            return false;
        }
        ipAddr = Normalizer.normalize(ipAddr, Normalizer.Form.NFKC);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddr);

        return matcher.matches();
    }

    /**
     * 获取广播地址
     *
     * @param ipMash ip掩码
     * @return
     */
    public static String getBcast(String ipMash) {
        String[] ipMaskArray = ipMash.split("\\/");
        int mask = Integer.parseInt(ipMaskArray[1]);
        String ipaddr = ipMaskArray[0];
        StringBuffer ipMasksb = new StringBuffer();
        StringBuffer ipMaskfsb = new StringBuffer();
        for (int i = 1; i <= 32; i++) {
            if (i < mask) {
                ipMasksb.append(1);
                ipMaskfsb.append(0);
            } else {
                ipMasksb.append(0);
                ipMaskfsb.append(1);
            }
        }
        String netAddr = Long.toBinaryString(ipToLong(ipaddr) & Long.parseLong(ipMasksb.toString(), 2));
        long longBcast = Long.parseLong(netAddr, 2) | Long.parseLong(ipMaskfsb.toString(), 2);
        return longToIP(longBcast);
    }

    /**
     * 获取网络地址
     *
     * @param ipMash ip掩码
     * @return
     */
    public static String getNetwork(String ipMash) {
        StringBuilder networkIp = new StringBuilder();
        String[] temp = ipMash.split("\\/");
        String[] sIP = temp[0].split("\\.");
        int Mask = Integer.parseInt(temp[1]);
        int[] ip = new int[4];

        for (int i = 0; i < sIP.length; i++) {
            ip[i] = Integer.parseInt(sIP[i]);
        }
        int b = 0;
        int a = Mask % 8;
        b = Mask / 8;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a; i++) sb.append('1');
        for (int i = 0; i < 8 - a; i++) sb.append('0');
        int end = Integer.parseInt(sb.toString(), 2);
        ip[b] = ip[b] & end;
        for (int i = b + 1; i < 4; i++) ip[i] = 0;
        for (int i = 0; i < 4; i++) {
            networkIp.append(ip[i]);
            if (i != 3) {
                networkIp.append(".");
            }
        }
        return networkIp.toString();
    }
    /**
     *
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     *
     *
     * @param request
     *
     * @return
     * @throws UnknownHostException
     *
     */

    public final static String getIpAddress(HttpServletRequest request) throws UnknownHostException {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
		if(ip.equals("0:0:0:0:0:0:0:1")) {
            //InetAddress address = InetAddress.getLocalHost();
            ip = "127.0.0.1";
        }
        return ip;
    }

    @Data
    private static class NetWorkAddress {
        private String netAdd;
        private String url;
        private Integer mask;
        private String startIp;
        private String endIp;
        private String descr;
    }
}
