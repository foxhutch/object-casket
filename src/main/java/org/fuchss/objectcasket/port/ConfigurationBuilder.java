package org.fuchss.objectcasket.port;

/**
 * Builder for {@link Configuration Configurations}.
 *
 */
public interface ConfigurationBuilder {
	/**
	 * Create a {@link Configuration}.
	 *
	 * @return a new {@link Configuration}
	 */
	Configuration createConfiguration();
}
