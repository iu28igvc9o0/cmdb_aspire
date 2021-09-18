import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.aspire.webbas.epm.core.entity.EpmProcess;
import com.aspire.webbas.epm.core.interceptor.NodeLeaveInterceptor;

/**   
 * 项目名称:  [webbas-component-epm]
 * 包名:      [com.aspire.webbas.epm.core.interceptor.test]    
 * 类名称:    [TestNodeLeaveInterceptor]  
 * 类描述:    [一句话描述该类的功能]
 * 创建人:    [王磊]   
 * 创建时间:  [2014年9月2日 下午6:18:23]     
 */
public class TestNodeLeaveInterceptor implements NodeLeaveInterceptor{

	@Override
	public void execute(EpmProcess process, Map<String, Object> m) {
		System.out.println("=============================="+process.getStatus());
		Iterator iter = m.entrySet().iterator();
		while(iter.hasNext()){
			Entry e = (Entry)iter.next();
			System.out.println(e.getKey()+":"+e.getValue());
		}
	}

}
