package experimental.build.plugin;

import kotlin.text.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class AutoInclude {
    private static final AutoInclude instance = new AutoInclude();

    public final List<Predicate<String>> ignore;
    public boolean enabled = true;

    public AutoInclude() {
        ignore = new ArrayList<>();
    }

    public void ignore(String path) {
        ignore.add(path::equals);
    }

    public void ignore(Regex pathPattern) {
        ignore.add(pathPattern::matches);
    }

    public void ignore(Pattern pathPattern) {
        ignore.add(s -> pathPattern.matcher(s).matches());
    }

    public void ignore(Predicate<String> path) {
        ignore.add(path);
    }

    public void clear() {
        ignore.clear();
    }

    public static AutoInclude getInstance() {
        return instance;
    }
}
