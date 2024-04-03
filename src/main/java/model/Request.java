package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public record Request(String method, String path, String version,
                      Map<String, String> headers, String body) {
    public static Request from(String request) {
        var lines = request.split("\r\n");
        var firstLine = lines[0].split(" ");
        var headers = new HashMap<String, String>();
        var lineIndex = 1;
        while (lineIndex < lines.length && !lines[lineIndex].isEmpty()) {
            var line = lines[lineIndex++];
            var header = Arrays.stream(line.split(":"))
                    .map(String::trim)
                    .toArray(String[] ::new);
            headers.put(header[0], header[1]);
        }
        return new Request(firstLine[0], firstLine[1], firstLine[2], headers, "");
    }
}