package xyz.tildejustin.ctm_srigt_integration;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import xyz.tildejustin.ctm_srigt_integration.exception.EmptyCategoryException;
import xyz.tildejustin.ctm_srigt_integration.exception.MalformedCategoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CTMCategory {
    public String id;
    public String name;
    public List<Entry> criteria;

    public CTMCategory(String id, String name, List<Entry> criteria) {
        this.id = id;
        this.name = name;
        this.criteria = criteria;
    }

    public static class Entry {
        public Identifier identifier;
        public BlockPos position;

        public Entry(String identifier, int x, int y, int z) {
            this.identifier = new Identifier(identifier);
            this.position = new BlockPos(x, y, z);
        }
    }

    public static CTMCategory CategoryBuilder(Scanner file) throws EmptyCategoryException, MalformedCategoryException {
        if (!file.hasNext()) {
            throw new EmptyCategoryException("category is empty");
        }
        String id = file.nextLine().trim();
        String name = file.nextLine().trim();
        if (name.isEmpty() || id.isEmpty()) {
            throw new MalformedCategoryException("category name and / or id is empty");
        }
        if (!file.hasNextLine()) {
            throw new EmptyCategoryException("category has no criteria");
        }
        List<Entry> criteria = new ArrayList<>();
        while (file.hasNextLine()) {
            String[] parts = file.nextLine().split(" ");
            if (parts.length != 4) {
                throw new MalformedCategoryException("expected 4 in a criteria");
            }
            criteria.add(
                    new Entry(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3])
                    )
            );
        }
        return new CTMCategory(id, name, criteria);
    }
}
