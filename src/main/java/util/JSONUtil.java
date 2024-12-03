package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Disk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JSONUtil {
    public static void saveToJSON(String fileName, Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> formatDiskToJSON(Disk disk) {
        List<Map<String, Object>> jsonList = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> entry : disk.getMemory().entrySet()) {
            Map<String, Object> jsonEntry = new HashMap<>();
            jsonEntry.put("virtual page number", entry.getKey());
            jsonEntry.put("content", entry.getValue());
            jsonList.add(jsonEntry);
        }

        return jsonList;
    }
}
