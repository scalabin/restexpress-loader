package com.strategicgains.restexpress.scaffold.minimal.config;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

public class YamlConfigRunner {

	public AppConfiguration load(String filename) {
		AppConfiguration config = null;
		
		Yaml yaml = new Yaml();
		InputStream in = null;

		try {
			//in = new FileInputStream(filename);
			in = this.getClass().getClassLoader().getResourceAsStream(filename);
			
			config = yaml.loadAs(in, AppConfiguration.class);
			System.out.println(config.toString());

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if (null != in) {
				IOUtils.closeQuietly(in);
			}
		}
		
		return config;
	}

}
