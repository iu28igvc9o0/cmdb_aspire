package com.aspire.mirror.misc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	public static String DateToString(Date date){
		String str ="";
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		 str = format.format(date);
		 return str.replace("-", "").replace(":", "").replace(" ", "").replace(".", "").trim();
	}
}
