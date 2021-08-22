package com.github.jknack.handlebars.maven;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

public class Issue234 {

  @Test
  public void withAmdOutput() throws Exception {
    PrecompilePlugin plugin = new PrecompilePlugin();
    plugin.setPrefix("src/test/resources/templates");
    plugin.setSuffix(".hbs");
    plugin.setOutput("target/issue234.js");
    plugin.addTemplate("a");
    plugin.addTemplate("c");
    plugin.setAmd(true);
    plugin.setProject(newProject());
    plugin.setHandlebarsJsFile("/handlebars-v4.7.6.js");

    plugin.execute();

    assertEquals(FileUtils.fileRead("target/issue234.js"), FileUtils.fileRead("src/test/resources/issue234.expected").trim(),
        FileUtils.fileRead("target/issue234.js").trim());
  }

  private MavenProject newProject(final String... classpath)
      throws DependencyResolutionRequiredException {
    MavenProject project = mock(MavenProject.class);
    when(project.getRuntimeClasspathElements()).thenReturn(Lists.newArrayList(classpath));
    return project;
  }
}
