package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeInMemoryDB {
    public static Map<String, List<Map<String, Object>>> TABLES = new HashMap<>();

    static {
        TABLES.put("users", List.of(
                Map.of("id", 1, "name", "John", "age", 30),
                Map.of("id", 2, "name", "Jane", "age", 25),
                Map.of("id", 3, "name", "Joe", "age", 20)
        ));

        TABLES.put("posts", List.of(
                Map.of("id", 1, "title", "Post 1", "user_id", 1),
                Map.of("id", 2, "title", "Post 2", "user_id", 2),
                Map.of("id", 3, "title", "Post 3", "user_id", 3)
        ));
    }

    private FakeInMemoryDB() {
    }
}
