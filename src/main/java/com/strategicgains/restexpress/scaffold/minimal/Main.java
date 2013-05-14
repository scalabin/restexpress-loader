package com.strategicgains.restexpress.scaffold.minimal;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.Parameters;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import com.strategicgains.restexpress.plugin.cache.CacheControlPlugin;
import com.strategicgains.restexpress.plugin.metrics.MetricsPlugin;
import com.strategicgains.restexpress.plugin.route.RoutesMetadataPlugin;
import com.strategicgains.restexpress.scaffold.minimal.config.AppConfiguration;
import com.strategicgains.restexpress.scaffold.minimal.config.MetricsConfig;
import com.strategicgains.restexpress.scaffold.minimal.config.ServiceLoader;
import com.strategicgains.restexpress.scaffold.minimal.config.YamlConfigRunner;
import com.strategicgains.restexpress.scaffold.minimal.serialization.ResponseProcessors;
import com.strategicgains.syntaxe.ValidationException;
import com.yammer.metrics.reporting.GraphiteReporter;

public class Main
{
	private static final String SERVICE_NAME = "RestExpress";
	private static final Logger LOG = LoggerFactory.getLogger(SERVICE_NAME);

	public static void main(String[] args) throws Exception
	{
		init(args);
	}

	private static void init(String[] args) throws ClassNotFoundException {
		AppConfiguration config = new YamlConfigRunner().load(args[0]);
		
		RestExpress server = new RestExpress()
		    .setName(SERVICE_NAME)
		    .setBaseUrl(config.getBaseUrl())
		    .setDefaultFormat(config.getDefaultFormat())
		    .setExecutorThreadCount(config.getExecutorThreadPoolSize())
		    .putResponseProcessor(Format.JSON, ResponseProcessors.json())
		    .putResponseProcessor(Format.XML, ResponseProcessors.xml())
		    .putResponseProcessor(Format.WRAPPED_JSON, ResponseProcessors.wrappedJson())
		    .putResponseProcessor(Format.WRAPPED_XML, ResponseProcessors.wrappedXml())
		    .addMessageObserver(new SimpleConsoleLogMessageObserver());
		if (config.getExecutorThreadPoolSize() > 0)
		{
			server.setExecutorThreadCount(config.getExecutorThreadPoolSize());
		}
		
		ServiceLoader loader = new ServiceLoader();
		loader.init(config, server);

		configureMetrics(config, server);

		new RoutesMetadataPlugin()							// Support basic discoverability.
			.register(server)
			.parameter(Parameters.Cache.MAX_AGE, 86400);	// Cache for 1 day (24 hours).

		new CacheControlPlugin()							// Support caching headers.
			.register(server);

		loader.loadExMapper(config.getExceptionMapper());
		server.bind(config.getPort());
		server.awaitShutdown();
	}

	private static void configureMetrics(AppConfiguration config, RestExpress server)
    {
		MetricsConfig mc = config.getMetricsConfig();

	    if (mc.getIsEnabled())
		{
			new MetricsPlugin()
				.virtualMachineId(mc.getMachineName())
				.register(server);

			if (mc.getIsGraphiteEnabled())
			{
				GraphiteReporter.enable(mc.getPublishSeconds(), TimeUnit.SECONDS,
					mc.getGraphiteHost(), mc.getGraphitePort());
			}
			else
			{
				LOG.warn("*** Graphite Metrics Publishing is Disabled ***");
			}
		}
		else
		{
			LOG.warn("*** Metrics Generation is Disabled ***");
		}
    }

}
