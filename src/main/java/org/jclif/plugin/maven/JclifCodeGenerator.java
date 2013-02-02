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
 * @phase process-classes
 */
public class JclifCodeGenerator extends AbstractMojo {

	/**
	 * Package name of the command handler definitions.
	 * 
	 * @parameter	expression="${commandHandlerPackage}" 
	 * @required
	 */
	private String commandHandlerPackage = "";
	
	/**
	 * @parameter	expression="${applicationName}"
	 * @required 
	 */
	private String applicationName;
	
	/**
	 * @parameter	expression="${sourceMainDirectory}"
	 * @required 
	 */
	private String sourceMainDirectory;
	
	/**
	 * @parameter	expression="${targetDirectory}"
	 * @required 
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
