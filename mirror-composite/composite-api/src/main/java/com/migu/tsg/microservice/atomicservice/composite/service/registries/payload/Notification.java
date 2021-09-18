package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
	private List<Events> events;
	
	
	@Data
	public static class Events {
		private Target target;
		private String action;
		private Request request;
		private Actor actor;
		private String id;
		private String timestamp;
		private Source source;
		
	}
	
	@Data
	public static class Target {
		private String repository;
		private String mediaType;
		private String tag;
		private String digest;
		private String url;
		private Integer size;
		private Integer length;
	}
	
	@Data
	public static class Request {
		private String method;
		private String addr;
		private String id;
		private String host;
		private String useragent;
	}
	
	@Data
	public static class Actor {
		private String name;
	}
	@Data
	public static class Source {
		private String addr;
		private String instanceID;
	}
}
