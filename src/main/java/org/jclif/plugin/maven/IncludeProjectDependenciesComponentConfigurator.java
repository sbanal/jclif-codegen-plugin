package org.jclif.plugin.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.configurator.AbstractComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter;
import org.codehaus.plexus.component.configurator.converters.special.ClassRealmConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

/**
 * Code from http://stackoverflow.com/questions/2659048/add-maven-build-classpath-to-plugin-execution-classpath
 * 
 * @plexus.component 
 *                   role="org.codehaus.plexus.component.configurator.ComponentConfigurator"
 *                   role-hint="include-project-dependencies"
 * @plexus.requirement role=
 *                     "org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup"
 *                     role-hint="default"
 */
public class IncludeProjectDependenciesComponentConfigurator extends
		AbstractComponentConfigurator {

	public void configureComponent(Object component,
			PlexusConfiguration configuration,
			ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm,
			ConfigurationListener listener)
			throws ComponentConfigurationException {

		addProjectDependenciesToClassRealm(expressionEvaluator, containerRealm);

		converterLookup.registerConverter(new ClassRealmConverter(containerRealm));

		ObjectWithFieldsConverter converter = new ObjectWithFieldsConverter();

		converter.processConfiguration(converterLookup, component,
				containerRealm.getClassLoader(), configuration,
				expressionEvaluator, listener);
	}

	private void addProjectDependenciesToClassRealm(
			ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm)
			throws ComponentConfigurationException {
		List<String> runtimeClasspathElements;

		try {
			// noinspection unchecked
			runtimeClasspathElements = (List<String>) expressionEvaluator
					.evaluate("${project.runtimeClasspathElements}");
		} catch (ExpressionEvaluationException e) {
			throw new ComponentConfigurationException(
					"There was a problem evaluating: ${project.runtimeClasspathElements}",
					e);
		}

		// Add the project dependencies to the ClassRealm
		final URL[] urls = buildURLs(runtimeClasspathElements);
		for (URL url : urls) {
			containerRealm.addConstituent(url);
		}
	}

	private URL[] buildURLs(List<String> runtimeClasspathElements)
			throws ComponentConfigurationException {
		
		List<URL> urls = new ArrayList<URL>(runtimeClasspathElements.size());
		for (String element : runtimeClasspathElements) {
			try {
				final URL url = new File(element).toURI().toURL();
				urls.add(url);
			} catch (MalformedURLException e) {
				throw new ComponentConfigurationException(
						"Unable to access project dependency: " + element, e);
			}
		}

		return urls.toArray(new URL[urls.size()]);
	}

}