package CDBurn.Services;

public class ExecuteCommandService {

    public static void Execute(String command){
        Process process;
        try {
            process = Runtime.getRuntime().exec( new String[]{"/bin/bash","-c","echo "
                    + System.getenv("PASSWORD") + " |  "+command});
            process.waitFor();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
