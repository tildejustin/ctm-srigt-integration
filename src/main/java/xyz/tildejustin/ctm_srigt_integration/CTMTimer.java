package xyz.tildejustin.ctm_srigt_integration;

import com.redlimerl.speedrunigt.SpeedRunIGT;
import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.category.RunCategory;
import xyz.tildejustin.ctm_srigt_integration.exception.EmptyCategoryException;
import xyz.tildejustin.ctm_srigt_integration.exception.MalformedCategoryException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CTMTimer implements SpeedRunIGTApi {
    public static Path categoriesFolder = SpeedRunIGT.getMainPath().resolve("ctm-categories");
    public static Map<RunCategory, CTMCategory> categoryLinker = new HashMap<>();

    static {
        categoriesFolder.toFile().mkdirs();
    }

    @Override
    public Collection<RunCategory> registerCategories() {
        return getCategories();
    }

    public Set<RunCategory> getCategories() {
        Arrays.asList(Objects.requireNonNull(CTMTimer.categoriesFolder.toFile().listFiles())).forEach(path -> {
                    CTMCategory category;
                    try {
                        category = CTMCategory.CategoryBuilder(new Scanner(path));
                    } catch (EmptyCategoryException | MalformedCategoryException | IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    CTMTimer.categoryLinker.put(new RunCategory(category.id, "", category.name), category);
                }
        );
        return CTMTimer.categoryLinker.keySet();
    }
}
