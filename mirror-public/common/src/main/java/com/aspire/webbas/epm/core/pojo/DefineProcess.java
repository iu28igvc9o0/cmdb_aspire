package com.aspire.webbas.epm.core.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 自定义流程对象
 * @author wanglei
 */
public class DefineProcess {
	private Node start;
	private Node end;
	private List<Node> nodeList;

	private Map<String, Object> process = new HashMap<String, Object>();

	/**
	 * 自定义函数流程构造
	 * @param start 启动节点
	 * @param end 结束节点
	 * @param nodeList 节点列表
	 */
	public DefineProcess(Node start, List<Node> nodeList, Node end) {
		this.start = start;
		this.nodeList = nodeList;
		this.end = end;

		if (start == null || nodeList == null || nodeList.size() == 0 || end == null) {
			this.process = null;
			return;
		} else {
			process.put("start", start);
			for (Node n : nodeList) {
				process.put(n.getStatus(), n);
			}
			process.put("end", end);
		}

	}

	/**
	 * 将流程定义对象转换为json
	 * @return .
	 */
	public String toJson() {
		if (process == null) {
			return null;
		}
		Gson gson = new Gson();
		return gson.toJson(process);
	}

	/**
	 * @return .
	 */
	public Node getStart() {
		return start;
	}

	/**
	 * @param start .
	 */
	public void setStart(Node start) {
		this.start = start;
	}

	/**
	 * @return .
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * @param end .
	 */
	public void setEnd(Node end) {
		this.end = end;
	}

	/**
	 * @return .
	 */
	public List<Node> getNodeList() {
		return nodeList;
	}

	/**
	 * @param nodeList .
	 */
	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	/**
	 * @return .
	 */
	public Map<String, Object> getProcess() {
		return process;
	}

	/**
	 * @param process .
	 */
	public void setProcess(Map<String, Object> process) {
		this.process = process;
	}

}
