package CDBurn.Services;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteCommandService {

    public static void Execute(String command) {
        Process process;
        BufferedReader reader;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "echo "
                    + System.getenv("PASSWORD") + " |  " + command});
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
