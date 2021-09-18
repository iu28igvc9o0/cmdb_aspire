package com.aspire.mirror.scada.biz.impl;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FontUtils {
	
	/**
	 * <p>
	 * 获取宋体
	 * </p>
	 * @author 曾祥华
	 * @version 0.1 2019年10月31日
	 * @param fs 字体大小
	 * @return
	 * Font
	 */
	public static Font getDefinedFont(float fs) {
		Font definedFont = null; 
        InputStream is = null;  
        BufferedInputStream bis = null;  
        try {  
//            File file = new File("/usr/share/fonts/simsun.ttc");
            File file = new File("E:simsun.ttc");
            if(!file.exists()) {
            	return null;
            }
			is =new FileInputStream(file);
            bis = new BufferedInputStream(is);  
            definedFont = Font.createFont(Font.TRUETYPE_FONT, is);
            //设置字体大小，float型
           definedFont = definedFont.deriveFont(fs);
        } catch (FontFormatException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally { 
           try {  
                if (null != bis) {  
                    bis.close();  
                }  
                if (null != is) {  
                    is.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        }  
        return definedFont;  
    }  
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FontUtils.getDefinedFont(50);
		
	}

}
