package CDBurn.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tools {
    public static long CalculateMemory() throws Exception {
        Process process;
        BufferedReader reader;
        try {
            ProcessBuilder bash = new ProcessBuilder("bash", "-c", "df | grep 'sr0' | awk '{print $3}'");
            process = bash.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String parseLine = reader.readLine();if(parseLine == null) throw new Exception();
            return Long.parseLong(parseLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
