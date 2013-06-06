package com.strategicgains.restexpress.loader.config;

import org.jboss.netty.handler.codec.http.HttpMethod;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class OperationInfo {
    //~ Instance fields ================================================================================================

    HttpMethod httpMethod;
    String action;
    String name;
    String path;
    String produces;
    String svcClass;
    Class<?>[] paraClasses;

    //~ Constructors ===================================================================================================

    public OperationInfo() {
        super();
    }

    public OperationInfo(String action, HttpMethod httpMethod, String path, String svcClass, String name,
        String produces, Class<?>[] paraClasses) {
        super();
        this.action = action;
        this.httpMethod = httpMethod;
        this.path = path;
        this.svcClass = svcClass;
        this.name = name;
        this.produces = produces;
        this.paraClasses = paraClasses;
    }

    //~ Methods ========================================================================================================

    /**
     * DOCUMENT ME!
     *
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the httpMethod
     */
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the paraClasses
     */
    public Class<?>[] getParaClasses() {
        return paraClasses;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the produces
     */
    public String getProduces() {
        return produces;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the svcClass
     */
    public String getSvcClass() {
        return svcClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * DOCUMENT ME!
     *
     * @param httpMethod the httpMethod to set
     */
    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paraClasses the paraClasses to set
     */
    public void setParaClasses(Class<?>[] paraClasses) {
        this.paraClasses = paraClasses;
    }

    /**
     * DOCUMENT ME!
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * DOCUMENT ME!
     *
     * @param produces the produces to set
     */
    public void setProduces(String produces) {
        this.produces = produces;
    }

    /**
     * DOCUMENT ME!
     *
     * @param svcClass the svcClass to set
     */
    public void setSvcClass(String svcClass) {
        this.svcClass = svcClass;
    }
}
