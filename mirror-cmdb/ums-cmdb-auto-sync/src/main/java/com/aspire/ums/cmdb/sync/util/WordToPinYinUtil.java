package com.aspire.ums.cmdb.sync.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @ClassName: WordToPinYinUtil
 * @Description: 汉字转换为拼音
 * @Author: liangjun
 * @Date: 2018/6/23
 * @Version: 1.0
 */
public class WordToPinYinUtil {

    private static final Logger logger = LoggerFactory.getLogger(WordToPinYinUtil.class);
    /**
     * @Description:    汉字转换为拼音
     * @Author:         liangjun
     * @CreateDate:     2018/6/23
     * @Version:        1.0
     */
    public static String ToPinyin(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }
    /**
     * @Description:    汉字转换为拼音首字母
     * @Author:         liangjun
     * @CreateDate:     2018/6/23
     * @Version:        1.0
     */
    public static String ToFirstChar(String str){
        String convert = "";
        for (int j = 0; j <  str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
}
