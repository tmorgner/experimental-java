package experimental.build.plugin;

import java.nio.file.Files;
import java.nio.file.Path;

class PathInfo {
    private final Path path;
    private final String gradlePath;
    private final boolean project;

    public PathInfo(Path path, String gradlePath, boolean project) {
        this.path = path;
        this.gradlePath = gradlePath;
        this.project = project;
    }

    public Path getPath() {
        return path;
    }

    public String getGradlePath() {
        return gradlePath;
    }

    public boolean isProject() {
        return project;
    }

    public static PathInfo fromPath(Path root, Path path) {
        Path relativize = relativize(root, path);
        StringBuilder b = new StringBuilder();
        for (Path name : relativize) {
            b.append(":");
            b.append(name);
        }
        return new PathInfo(root, b.toString(),
                            Files.exists(path.resolve("settings.gradle")) ||
                            Files.exists(path.resolve("settings.gradle.kts"))
        );
    }

    private static Path relativize(Path root, Path relative) {
        return relative.subpath(root.getNameCount(), relative.getNameCount());
    }
}
