package com.strategicgains.restexpress.loader.dto;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.loader.config.AppConfiguration;
import com.strategicgains.syntaxe.ValidationEngine;
import com.strategicgains.syntaxe.ValidationException;

public class DtoAssembler {
	private static final Logger LOG = LoggerFactory
			.getLogger(DtoAssembler.class);
	static Map<Class<?>, List<String>> Query_Map = new HashMap<Class<?>, List<String>>();
	static Map<Class<?>, List<String>> Path_Map = new HashMap<Class<?>, List<String>>();
	static Map<Class<?>, List<String>> Body_Map = new HashMap<Class<?>, List<String>>();

	/**
	 * @return the query_Map
	 */
	public static Map<Class<?>, List<String>> getQuery_Map() {
		return Query_Map;
	}

	/**
	 * @return the path_Map
	 */
	public static Map<Class<?>, List<String>> getPath_Map() {
		return Path_Map;
	}

	/**
	 * @return the body_Map
	 */
	public static Map<Class<?>, List<String>> getBody_Map() {
		return Body_Map;
	}

	public void init(AppConfiguration config) throws ClassNotFoundException,
			IOException {
		if (StringUtils.isNotEmpty(config.getDtoPackage())) {
			init(config.getDtoPackage());
		}
	}

	public void init(String pkgName) throws ClassNotFoundException, IOException {
		Set<Class<?>> clazzs = findClasses(pkgName);
		for (Class<?> clazz : clazzs) {
			scanActionDto(clazz);
		}

		LOG.debug(Query_Map.toString());
		LOG.debug(Path_Map.toString());
		LOG.debug(Body_Map.toString());
	}

	/**
	 * Scan the dto annotation to fill all the mapper strategy
	 */
	public void scanActionDto(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();

		List<String> annotatedPathFileds = new ArrayList<String>();
		List<String> annotatedQueryFileds = new ArrayList<String>();
		List<String> allFields = new ArrayList<String>();

		for (Field field : fields) {
			allFields.add(field.getName());

			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (PathParam.class.isInstance(annotation)) {
					annotatedPathFileds.add(field.getName());
				} else if (QueryParam.class.isInstance(annotation)) {
					annotatedQueryFileds.add(field.getName());
				}
			}

		}

		Body_Map.put(clazz, allFields);
		Path_Map.put(clazz, annotatedPathFileds);
		Query_Map.put(clazz, annotatedQueryFileds);
	}

	private Set<Class<?>> findClasses(String packagename) {
		final Reflections reflections = new Reflections(packagename);
		Set<Class<?>> subTypes = reflections
				.getTypesAnnotatedWith(RestDto.class);
		return subTypes;

	}

	public static <T> T mapAndValidate(final Request request,
			final Response response, final Class<T> actClazz) {
		T actionDto = null;

		if (request.getHttpMethod().equals(HttpMethod.PUT)
				|| request.getHttpMethod().equals(HttpMethod.POST)) {
			actionDto = request.getBodyAs(actClazz,
					"Action details not provided");
		}

		try {
			if (null == actionDto) {
				actionDto = actClazz.newInstance();
			}

			for (String name : DtoAssembler.getPath_Map().get(actClazz)) {
				LOG.debug(name);
				BeanUtils.setProperty(actionDto, name, request.getHeader(name));
			}

			for (String name : DtoAssembler.getQuery_Map().get(actClazz)) {
				LOG.debug(name);
				BeanUtils.setProperty(actionDto, name, request.getHeader(name));
			}

			ValidationEngine.validateAndThrow(actionDto);

		} catch (Exception ex) {
			if (ValidationException.class.isInstance(ex)) {
				throw (ValidationException) ex;
			} else {
				LOG.error("Failed to handle the request dto", ex);
				throw new ValidationException(ex.getMessage());
			}
		}

		return actionDto;
	}

}
