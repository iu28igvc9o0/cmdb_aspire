package com.aspire.webbas.epm.core.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.aspire.webbas.epm.core.constant.EpmConstant;
import com.aspire.webbas.epm.core.pojo.Choice;
import com.aspire.webbas.epm.core.pojo.Global;
import com.aspire.webbas.epm.core.pojo.Interceptor;
import com.aspire.webbas.epm.core.pojo.Node;

/**
 * 项目名称: [webbas-component-epm] 包名: [com.aspire.webbas.epm.core.config] 类名称:
 * [ProcessConfig] 类描述: [流程相关配置读取接口] 创建人: [王磊] 创建时间: [2014年8月29日 上午9:36:02]
 */
public class ProcessConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessConfig.class);

	private static ProcessConfig sProcessConfig;

	/** 配置文件目录为bin/config/epm目录下所有的xml文件 */
	// private static final String CONFIG_DIR = "/config/epm";
	private static final String CONFIG_DIR = "/epm";

	/** 流程配置文件内容存储Map */
	private static Map<String, Map<String, Object>> sProcessDefinitionMap = new HashMap<String, Map<String, Object>>();

	/**
	 * 单例对象获取方法
	 * @return 流程配置对象
	 * @throws Exception 异常
	 */
	public static ProcessConfig getInstance() throws Exception {
		if (sProcessConfig == null) {
			sProcessConfig = new ProcessConfig();
		}
		return sProcessConfig;
	}

	private ProcessConfig() throws Exception {
		// File file = new File(ConfigHelper.getBasePath() + CONFIG_DIR);
		// //获取流程文件所在目录全路径
		// File file = new
		// File(ResourceUtils.getURL("classpath:").getPath()+CONFIG_DIR);
		// logger.info("加载工作流配置配置文件，文件目录为"+ResourceUtils.getURL("classpath:").getPath()+CONFIG_DIR);

		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = patternResolver.getResources("classpath*:epm/*.xml");
		if (resources != null && resources.length > 0) {
			for (int i = 0; i < resources.length; i++) {
				try {
					/* 读取xml文件 */
					SAXReader reader = new SAXReader();
					Document doc = reader.read(resources[i].getInputStream());
					Element rootElement = doc.getRootElement();
					// process map对象
					Map<String, Object> process = new HashMap<String, Object>();
					/* 组装全局配置（拦截器、参数） */
					Element golobalElement = rootElement.element("global");
					Global global = new Global();
					if (golobalElement != null) {
						/* 加载全局params配置 */
						Element paramsElement = golobalElement.element("params");
						if (paramsElement != null) {
							List<Element> paramElementsList = paramsElement.elements("param");
							for (Element e : paramElementsList) {
								global.getParams().put(e.attributeValue("name"), e.attributeValue("value"));
							}
						}
						/* 加载全局拦截器 */
						Element interceptorsElement = golobalElement.element("interceptors");
						if (interceptorsElement != null) {
							// 获取start——interceptor标签
							List<Element> interceptorElementsList = interceptorsElement.elements("interceptor");
							for (Element e : interceptorElementsList) {
								// 获取节点拦截器的处理类
								Interceptor interceptor = new Interceptor();
								interceptor.setBean(e.attributeValue("bean"));
								// 获取节点拦截器的处理类的配置参数
								List<Element> paramElementsList = e.elements("param");
								for (Element el : paramElementsList) {
									interceptor.getMap().put(el.attributeValue("name"), el.attributeValue("value"));
								}
								global.getInterceptorList().add(interceptor);
							}
						}
					}
					process.put("global", global);
					/* 组装start对象 */
					Node start = convert(rootElement.element("start"), global);
					process.put("start", start);
					/* 组装end对象 */
					Node end = convert(rootElement.element("end"), global);
					process.put("end", end);
					/* 组装流程节点对象 */
					List<Element> nodeElementList = rootElement.elements("node");
					for (Element e : nodeElementList) {
						Node node = convert(e, global);
						process.put(node.getStatus(), node);
					}
					// 加到总的流程静态map中
					sProcessDefinitionMap.put(rootElement.attributeValue("name"), process);
				} catch (Exception e) {
					LOGGER.error("加载工作流配置配置文件失败，错误信息：" + e.getMessage(), e);
				}
			}
		}
		/*
		 * 加载未处理完成的流程实例的自定义流程模版
		 */
		/*
		 * EpmProcess processC = new EpmProcess();
		 * processC.setIsEnd(EpmConstant.NO);
		 * processC.setIsDefine(EpmConstant.YES); EpmProcessDao epmProcessDao =
		 * (EpmProcessDao)sApplicationCcontext.getBean("epmProcessDao");
		 * List<EpmProcess> list = epmProcessDao.list(processC); for(EpmProcess
		 * p : list){ String defineContent = p.getDefineContent();
		 * if(defineContent != null && defineContent.length() > 0){ Gson g = new
		 * Gson(); processDefinitionMap.put(p.getName(),
		 * g.fromJson(p.getDefineContent(), Map.class)); }
		 * }
		 */

	}

	/**
	 * 将xml节点对象转换成特定对象
	 * @param m xml节点
	 * @param global 全局参数对象
	 * @return 节点对象
	 */
	public Node convert(Element m, Global global) {
		// Map<String, Object> processMap = new HashMap<String, Object>();

		/* 先存储start节点 */
		// Element startElement = m.element("start");
		Node node = new Node();
		// node.setTo(m.attributeValue("to")); //获取to值
		node.setStatus(m.attributeValue("status")); // 获取status值

		/* 加载拦截器配置 */
		Element interceptorsElement = m.element("interceptors");
		if (interceptorsElement != null) {
			// 获取start——interceptor标签
			List<Element> interceptorElementsList = interceptorsElement.elements("interceptor");
			for (Element e : interceptorElementsList) {
				// 获取节点拦截器的处理类
				Interceptor interceptor = new Interceptor();
				interceptor.setBean(e.attributeValue("bean"));
				// 获取节点拦截器的处理类的配置参数
				List<Element> paramElementsList = e.elements("param");
				for (Element el : paramElementsList) {
					interceptor.getMap().put(el.attributeValue("name"), el.attributeValue("value"));
				}
				node.getInterceptorList().add(interceptor);
			}
		}
		// 添加全局拦截器
		for (Interceptor gi : global.getInterceptorList()) {
			node.getInterceptorList().add(gi);
		}

		/* 加载decision配置 */
		Element decisionElement = m.element("decision");
		if (decisionElement != null) {
			List<Element> choiceElementsList = decisionElement.elements("choice"); // 获取decision——decision标签
			for (Element e : choiceElementsList) {
				// 获取节点选择器的处理类
				Choice choice = new Choice();
				choice.setBackLevel(e.attributeValue("backLevel"));
				choice.setIsBack(e.attributeValue("isBack"));
				choice.setProcessType(e.attributeValue("processType"));
				choice.setRole(e.attributeValue("role"));
				choice.setDealType(e.attributeValue("dealType"));
				choice.setTo(e.attributeValue("to"));
				choice.setValue(e.attributeValue("value"));
				choice.setViewer(e.attributeValue("viewer"));
				choice.setName(e.attributeValue("name"));
				node.getChoiceList().add(choice);
			}
		} else {
			// 如果没有选择器，创建一个默认选择器，保证所有的流转都从选择器发起
			Choice choice = new Choice();
			// choice.setBackLevel(m.attributeValue("backLevel"));
			choice.setRole(m.attributeValue("role"));
			choice.setDealType(m.attributeValue("dealType"));
			// choice.setIsBack(m.attributeValue("isBack"));
			choice.setProcessType(m.attributeValue("processType"));
			choice.setTo(m.attributeValue("to"));
			choice.setValue(EpmConstant.CHOICE_DEFAULT_VALUE);
			choice.setViewer(m.attributeValue("viewer"));
			node.getChoiceList().add(choice);
		}

		/* 加载params配置 */
		Element paramsElement = m.element("params");
		if (paramsElement != null) {
			List<Element> paramElementsList = paramsElement.elements("param");
			for (Element e : paramElementsList) {
				node.getParams().put(e.attributeValue("name"), e.attributeValue("value"));
			}
		}

		return node;
	}

	/**
	 * 获取流程定义
	 * @param key 流程名
	 * @return 节点map
	 */
	public static Map<String, Object> getProcessDefinition(String key) {
		try {
			return ProcessConfig.getInstance().sProcessDefinitionMap.get(key);
		} catch (Exception e) {
			LOGGER.error("epm：流程定义配置文件加载失败", e);
		}
		return null;
	}

	/**
	 * 添加流程定义
	 * @param processName 流程名
	 * @param process 流程对象
	 */
	public static void putProcess(String processName, Map<String, Object> process) {
		sProcessDefinitionMap.put(processName, process);
	}

	/*public static void main(String[] args) throws Exception {
		ConfigHelper.setBasePath("D:\\developing\\VOMS_PROJECT\\trunk\\02_版本级文档\\VOMS1.0.0.0\\03_架构设计");
		Node start = (Node) ProcessConfig.getInstance().getProcessDefinition("epmProcess").get("start");
		Node node = (Node) ProcessConfig.getInstance().getProcessDefinition("epmProcess").get("1002");
		System.out.println(node.getStatus());
	}*/

}
