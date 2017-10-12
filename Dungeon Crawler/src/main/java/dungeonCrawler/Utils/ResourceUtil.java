package dungeonCrawler.Utils;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceUtil {
	
	public static String loadResource(String fileName) throws Exception {
		String result = "test";
        try (InputStream in = ResourceUtil.class.getClass().getResourceAsStream(fileName);
        		Scanner scanner = new Scanner(in, "UTF-8")) {
					result = scanner.useDelimiter("\\A").next();
		}
        return result;
	}
}
