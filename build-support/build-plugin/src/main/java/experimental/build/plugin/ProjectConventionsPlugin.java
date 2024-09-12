package experimental.build.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.JavaTestFixturesPlugin;

public abstract class ProjectConventionsPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        project.getPluginManager().apply(JavaTestFixturesPlugin.class);
        ProjectConventionsExtension extension = ProjectConventionsExtension.of(project);
        project.getExtensions().add("toolchainDefaults", extension);
        project.getExtensions()
               .getByType(JavaPluginExtension.class)
               .getToolchain()
               .getLanguageVersion()
               .convention(extension.getMainVersion());

        if (project != project.getRootProject()) {
            project.setGroup(project.getRootProject().getGroup());
            project.setVersion(project.getRootProject().getVersion());
        }

        project.beforeEvaluate(p -> System.out.println("BeforeEvaluate"));
    }
}
