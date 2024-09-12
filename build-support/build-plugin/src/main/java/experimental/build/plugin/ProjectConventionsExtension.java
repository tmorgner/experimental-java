package experimental.build.plugin;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.gradle.jvm.toolchain.JavaLauncher;
import org.gradle.jvm.toolchain.JavaToolchainService;

import javax.inject.Inject;
import java.util.function.Predicate;

public abstract class ProjectConventionsExtension implements ExtensionAware {
  @Input
  public abstract Property<JavaLanguageVersion> getMainVersion();

  @Input
  public abstract Property<JavaLanguageVersion> getTestVersion();

  @Input
  public abstract Property<Predicate<Task>> getTestPredicate();

  @Input
  public abstract Property<JavaLauncher> getTestLauncher();

  @Input
  public abstract Property<JavaLauncher> getMainLauncher();

  @Inject
  protected abstract JavaToolchainService getToolchainService();

  protected void postInit(Project project) {
    getMainVersion().convention(JavaLanguageVersion.of(8));
    getTestVersion().convention(getMainVersion());
    getMainLauncher().convention(getToolchainService().launcherFor(spec -> {
      spec.getLanguageVersion().convention(getMainVersion());
    }));
    getTestLauncher().convention(getToolchainService().launcherFor(spec -> {
      spec.getLanguageVersion().convention(getTestVersion());
    }));
    getTestPredicate().convention(this::isTestTask);
  }

  private boolean isTestTask(Task t) {
    if (t instanceof JavaCompile) {
      return t.getName().contains("Test");
    }
    if (t instanceof Test) {
      return t.getName().equals("test") || t.getName().contains("Test");
    }
    return false;
  }

  public static @NotNull ProjectConventionsExtension of(@NotNull Project project) {
    final ExtensionContainer extensions = project.getExtensions();
    final ProjectConventionsExtension projectExt = extensions.findByType(ProjectConventionsExtension.class);
    if (projectExt == null) {
      final ProjectConventionsExtension conventions = extensions.create("ProjectConventions", ProjectConventionsExtension.class);
      conventions.postInit(project);
      return conventions;
    }

    return projectExt;
  }


}
