package com.strategicgains.restexpress.loader.config;

import org.jboss.netty.handler.codec.http.HttpMethod;

public class OperationInfo {
	String action;
	HttpMethod httpMethod;
	String path;
	String svcClass; 
	String name;
	String produces;
	
	
	
	/**
	 * @return the produces
	 */
	public String getProduces() {
		return produces;
	}
	/**
	 * @param produces the produces to set
	 */
	public void setProduces(String produces) {
		this.produces = produces;
	}
	public OperationInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OperationInfo(String action, HttpMethod httpMethod, String path,
			String svcClass, String name, String produces) {
		super();
		this.action = action;
		this.httpMethod = httpMethod;
		this.path = path;
		this.svcClass = svcClass;
		this.name = name;
		this.produces = produces;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the svcClass
	 */
	public String getSvcClass() {
		return svcClass;
	}
	/**
	 * @param svcClass the svcClass to set
	 */
	public void setSvcClass(String svcClass) {
		this.svcClass = svcClass;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

}
