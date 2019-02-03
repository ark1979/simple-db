import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;


public class SimpleDB {
    private static final String FILE_PATH = "simple_db.txt";
    private static long pointer = 0;

    public static void main(String[] args) throws IOException {
        //Write to file
        Map<String, Integer> countries = getLeastCorruptCountries();
        countries.forEach((key, value) -> {
            try {
                writeToFile(FILE_PATH, key, value, pointer);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Read from file
        readFromFile();
    }

    private static Map<String, Integer> getLeastCorruptCountries() {
        Map<String, Integer> countries = new HashMap<>();

        countries.put("Denmark", 1);
        countries.put("New Zealand", 2);
        countries.put("Finland", 3);
        countries.put("Sweden", 4);
        countries.put("Switzerland", 5);
        countries.put("United Kingdom", 11);
        countries.put("United States", 22);
        countries.put("Norway", 14);

        return countries;
    }

    private static void writeToFile(String filePath, String key, int value, long position)
            throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.seek(position);
        file.write((key + " : " + value).getBytes());
        //Write each record in new line
        file.writeBytes(System.getProperty("line.separator"));
        pointer = file.getFilePointer();
        file.close();
    }

    private static void readFromFile() throws IOException {
        Map<String, Long> map = new HashMap<>();
        RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r");
        //read file and populate map.
        String line;
        do {
            pointer = file.getFilePointer();
            line = file.readLine();
            if (line != null) {
                String key = line.split(":")[0];
                map.put(key, pointer);
            }
        } while (line != null);

        for (String key : map.keySet()) {
            System.out.printf("key %s has a position of %s\n", key, map.get(key));
            file.seek(map.get(key));
            System.out.println(file.readLine());
        }
        file.close();
    }


}

