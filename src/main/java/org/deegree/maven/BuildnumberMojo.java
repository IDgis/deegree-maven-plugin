//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.maven;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
@Execute(goal = "generate-buildinfo", phase = LifecyclePhase.GENERATE_RESOURCES)
@Mojo(name = "generate-buildinfo", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class BuildnumberMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute()
                            throws MojoExecutionException,
                            MojoFailureException {
        Log log = getLog();
        Properties ps = new Properties();
        ps.put( "build.artifactId", project.getArtifactId() );
        ps.put( "build.by", System.getProperty( "user.name" ) );
        ps.put( "build.date", project.getProperties().getProperty( "buildTimestamp" ) );
        ps.put( "build.svnrev", project.getProperties().getProperty( "buildNumber" ) );
        FileOutputStream os = null;
        try {
            File out = new File( project.getBasedir(), "target/classes/META-INF/deegree/buildinfo.properties" );
            if ( !out.getParentFile().exists() && !out.getParentFile().mkdirs() ) {
                throw new MojoFailureException( "Could not create parent directory for buildinfo.properties." );
            }
            os = new FileOutputStream( out );
            ps.store( os, "generated by deegree-maven-plugin" );
            log.info( "Wrote " + out );
        } catch ( IOException e ) {
            throw new MojoExecutionException( e.getLocalizedMessage(), e );
        } finally {
            closeQuietly( os );
        }
    }

}
