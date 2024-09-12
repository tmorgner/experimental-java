package experimental.build.plugin;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class ProjectSettingsPlugin implements Plugin<Settings> {

    private final AutoInclude extension;

    @Inject
    public ProjectSettingsPlugin() {
        extension = AutoInclude.getInstance();
        extension.clear();
        extension.ignore(p -> p.startsWith("."));
        extension.ignore("build");
        extension.ignore("buildSrc");
        extension.ignore("build-support");
    }

    @Override
    public void apply(Settings target) {
        System.out.println("Project settings plugin applied");
        target.enableFeaturePreview("VERSION_CATALOGS");
        target.getExtensions().add("autoInclude", extension);
        target.getGradle().settingsEvaluated(s -> {
            if (!extension.enabled) {
                return;
            }


            Path projectPath = target.getRootDir().toPath().toAbsolutePath();
            try {
                PathFileVisitor visitor = new PathFileVisitor(projectPath);
                Files.walkFileTree(projectPath, visitor);
                System.out.println(visitor.directories);

                Stream<PathInfo> stream = visitor.getDirectories()
                                                 .stream()
                                                 .filter(p -> !p.equals(projectPath))
                                                 .map(p -> PathInfo.fromPath(projectPath, p));
                stream.forEach(p -> {
                    if (target.findProject(p.getGradlePath()) != null) {
                        return;
                    }

                    if (p.isProject()) {
                        target.includeBuild(p.getPath());
                    }
                    else {
                        target.include(p.getGradlePath());
                    }
                });
            }
            catch (IOException e) {
                throw new GradleException("Failed to scan modules", e);
            }
        });
    }

    private class PathFileVisitor implements FileVisitor<Path> {
        private final ArrayList<Path> directories;
        private final Path root;

        public PathFileVisitor(Path root) {
            this.root = root;
            directories = new ArrayList<>();
        }

        public ArrayList<Path> getDirectories() {
            return directories;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            if (dir.equals(root)) {
                return FileVisitResult.CONTINUE;
            }
            if (isIncluded(dir)) {
                if (isGradleModule(dir)) {
                    directories.add(dir);
                }
                return FileVisitResult.CONTINUE;
            }
            else {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }


        private boolean isIncluded(Path path) {
            String name = path.getFileName().toString();
            for (Predicate<String> predicate : extension.ignore) {
                if (predicate.test(name)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isGradleModule(Path path) {
            if (!Files.isDirectory(path)) {
                return false;
            }
            if (Files.exists(path.resolve("build.gradle"))) {
                System.out.println("Found build.gradle in " + path);
                return true;
            }
            if (Files.exists(path.resolve("build.gradle.kts"))) {
                System.out.println("Found build.gradle.kts in " + path);
                return true;
            }
            return false;
        }

    }
}

