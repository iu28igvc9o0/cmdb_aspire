package com.aspire.cdn.esdatawrap.biz;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: IElasticSearchBizRunAction
 * <p/>
 *
 * 类功能描述: ES业务动作接口; 所有和ES相关的业务驱动动作(初始化动作、定时任务动作)都需要实现此接口; 按照@Order指定的值顺序执行
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月15日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IElasticSearchBizRunAction {
	public void doAction();
}
