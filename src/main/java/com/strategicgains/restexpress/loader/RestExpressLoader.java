package com.strategicgains.restexpress.loader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.Parameters;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.loader.config.AppConfiguration;
import com.strategicgains.restexpress.loader.config.MetricsConfig;
import com.strategicgains.restexpress.loader.config.YamlConfigRunner;
import com.strategicgains.restexpress.loader.dto.DtoAssembler;
import com.strategicgains.restexpress.loader.serialization.ResponseProcessors;
import com.strategicgains.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import com.strategicgains.restexpress.plugin.cache.CacheControlPlugin;
import com.strategicgains.restexpress.plugin.metrics.MetricsPlugin;
import com.strategicgains.restexpress.plugin.route.RoutesMetadataPlugin;
import com.yammer.metrics.reporting.GraphiteReporter;

public class RestExpressLoader {
	private static final Logger LOG = LoggerFactory
			.getLogger(RestExpressLoader.class);
	private static final String SERVICE_NAME = "RestExpress";

	private static void init(String[] args) throws ClassNotFoundException, IOException {
		AppConfiguration config = new YamlConfigRunner().load(args[0]);

		RestExpress server = new RestExpress()
				.setName(SERVICE_NAME)
				.setBaseUrl(config.getBaseUrl())
				.setDefaultFormat(config.getDefaultFormat())
				.setExecutorThreadCount(config.getExecutorThreadPoolSize())
				.putResponseProcessor(Format.JSON, ResponseProcessors.json())
				.putResponseProcessor(Format.XML, ResponseProcessors.xml())
				.putResponseProcessor(Format.WRAPPED_JSON,
						ResponseProcessors.wrappedJson())
				.putResponseProcessor(Format.WRAPPED_XML,
						ResponseProcessors.wrappedXml())
				.addMessageObserver(new SimpleConsoleLogMessageObserver());
		if (config.getExecutorThreadPoolSize() > 0) {
			server.setExecutorThreadCount(config.getExecutorThreadPoolSize());
		}

		ServiceLoader loader = new ServiceLoader();
		loader.init(config, server);
		DtoAssembler assembler = new DtoAssembler();
		assembler.init(config);

		configureMetrics(config, server);

		new RoutesMetadataPlugin() // Support basic discoverability.
				.register(server).parameter(Parameters.Cache.MAX_AGE, 86400); // Cache
																				// for
																				// 1
																				// day
																				// (24
																				// hours).

		new CacheControlPlugin() // Support caching headers.
				.register(server);

		loader.loadExMapper(config.getExceptionMapper());
		server.bind(config.getPort());
		server.awaitShutdown();
	}

	private static void configureMetrics(AppConfiguration config,
			RestExpress server) {
		MetricsConfig mc = config.getMetricsConfig();

		if (mc.getIsEnabled()) {
			new MetricsPlugin().virtualMachineId(mc.getMachineName()).register(
					server);

			if (mc.getIsGraphiteEnabled()) {
				GraphiteReporter.enable(mc.getPublishSeconds(),
						TimeUnit.SECONDS, mc.getGraphiteHost(),
						mc.getGraphitePort());
			} else {
				LOG.warn("*** Graphite Metrics Publishing is Disabled ***");
			}
		} else {
			LOG.warn("*** Metrics Generation is Disabled ***");
		}
	}

	public static void main(String[] args) throws Exception {
		init(args);
	}

}
