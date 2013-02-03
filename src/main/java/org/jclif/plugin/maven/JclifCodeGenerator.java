/** 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclif.plugin.maven;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jclif.plugin.CodeGenerator;


/**
 * Generates JCLIF configuration.
 * 
 * TODO: Generate code instead of configuration to avoid runtime cost doing class reflection every execution.
 * 
 * @author Stephen Lou Banal
 * 
 * @goal generate-config
 * @requiresProject true
 * @phase process-classes 
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
public class JclifCodeGenerator extends AbstractMojo {

	/**
	 * Package name of the command handler definitions.
	 * 
	 * @parameter	expression="${commandHandlerPackage}" default-value=""
	 * @required
	 */
	private String commandHandlerPackage = "";
	
	/**
	 * @parameter	expression="${applicationName}" 
	 * @required 
	 */
	private String applicationName;
	
	/**
	 * @parameter	expression="${sourceMainDirectory}" default-value="src/main/java"
	 */
	private String sourceMainDirectory;
	
	/**
	 * @parameter	expression="${targetDirectory}"  default-value="target/classes"
	 */
	private String targetDirectory;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		getLog().info(String.format("CodeGenerator called [appName=%s,srcMainDir=%s,targetDir=%s,handlerPackage=%s]",
				this.applicationName, this.sourceMainDirectory, this.targetDirectory, this.commandHandlerPackage));
		
		try {
			CodeGenerator codeGen = new CodeGenerator(applicationName, sourceMainDirectory);
			codeGen.generateMain(commandHandlerPackage, targetDirectory);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Unable to generate JCLIF configuration", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException("Unable to generate JCLIF configuration", e);
		} finally {
			getLog().info("CodeGenerator done");
		}
		
	}

}
