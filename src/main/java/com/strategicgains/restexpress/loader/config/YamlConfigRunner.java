package com.strategicgains.restexpress.loader.config;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class YamlConfigRunner {
	private static final Logger LOG = LoggerFactory
			.getLogger(YamlConfigRunner.class);

	public AppConfiguration load(String filename) {
		AppConfiguration config = null;

		Yaml yaml = new Yaml();
		InputStream in = null;

		try {
			in = this.getClass().getClassLoader().getResourceAsStream(filename);
			config = yaml.loadAs(in, AppConfiguration.class);
			LOG.info(config.toString());

		} catch (Exception ex) {
			LOG.error("Failed to load config: ", ex);
		} finally {
			if (null != in) {
				IOUtils.closeQuietly(in);
			}
		}

		return config;
	}

}
