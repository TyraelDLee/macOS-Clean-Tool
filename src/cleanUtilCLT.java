

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class cleanUtilCLT {
    // tmutil deletelocalsnapshots [timestamp]
    private static final String DELETE = "tmutil deletelocalsnapshots";
    // show the all snaps related on timestamp
    private static final String SCAN = "tmutil listlocalsnapshots /";
    private static final String SUDO = " | sudo -S ";//-S is necessary, strange..
    private static final String BIN = "/bin/bash";
    private String command  = "";
    private String password = "";
    private Process executor = null;
    private boolean observed = false;

    public cleanUtilCLT(){}

    public cleanUtilCLT(boolean observered){
        this.observed = observered;
    }

    public cleanUtilCLT(String command){
        this.command = command;
    }

    public void setPassword(String password){
        this.password = "echo \""+password+"\"";
    }

    public void scanS(){
        String[] cmd = {BIN, "-c", this.password+SUDO+SCAN};
        exec(cmd);
    }

    public void deleteS(String timestamp){
        String[] cmd = {BIN, "-c", this.password+SUDO+DELETE+" "+timestamp};
        exec(cmd);
    }

    public void scan(){
        String[] cmd = {SCAN};
        exec(cmd);
    }

    public void delete(String timestamp){
        String[] cmd = {DELETE+" "+timestamp};
        exec(cmd);
    }

    private String exec(String[] cmd){
        StringBuilder callBack = new StringBuilder();
        try {
            executor = Runtime.getRuntime().exec(cmd[0]);
            BufferedReader br = new BufferedReader(new InputStreamReader(executor.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
                callBack.append(line).append("\n");
            System.out.println(callBack.toString());
            executor.waitFor();
            br.close();
            executor.destroy();
        }catch (IOException | InterruptedException e){}
        return callBack.toString();
    }

    public void getSnaps(){
        String callback = exec(new String[]{SCAN});
        String[] snaps = callback.replace("com.apple.TimeMachine.","").replace(".local","").split("\n");
        for(String snap : snaps){
            if(!snap.contains("Snapshots for disk")){
                delete(snap);
            }
        }
    }

    public void getSnapsS(){
        this.observed = true;
        String callback = exec(new String[]{BIN, "-c", this.password+SUDO+SCAN});
        String[] snaps = callback.replace("com.apple.TimeMachine.","").replace(".local","").split("\n");
        if(snaps.length<=1)
            System.out.println("All clear.");
        else{
            for(String snap : snaps){
                System.out.println(snaps.length);
                deleteS(snap);
            }
        }

    }

    public static void main(String[] args) {
        cleanUtilCLT mainProgress = new cleanUtilCLT(false);
        if (args.length < 1) {
            mainProgress.scan();
        } else if (args.length == 1) {
            if (args[0].equals("?") || args[0].equals("-?")) {
                System.out.println("-P/-p [PASSWORD] with sudo privilege");
                System.out.println("-d delete all snaps");
                System.out.println("-d [TIMESTAMP] delete the specific snap");
                System.out.println("-s scan the snaps on the disk(local only)");
                System.out.println("-i about");
            } else if (args[0].equals("-s")) {
                mainProgress.scan();
            } else if (args[0].equals("-d")) {
                mainProgress.getSnaps();
            } else if (args[0].equals("-i")) {
                System.out.println(
                        "/////////////////////////////////////////////////////\n" +
                                "//                                                 //\n" +
                                "//                    cleanUtil                    //\n" +
                                "//                                                 //\n" +
                                "//                   version 0.1b                  //\n" +
                                "//            Copyright Tyrael Lee 2020            //\n" +
                                "/////////////////////////////////////////////////////\n" +
                                "This tool will help to remove the time machine snap from your local disk,\n" +
                                "which can be vary large, invisible and non-deletable normally.\n\n" +
                                "Caution: you cannot restore this step and removing snaps might be affect\n" +
                                "the Time machine backup. If you are using Time machine for backup, you may\n" +
                                "should not remove snaps."
                );
            }
        } else if (args.length == 2) {
            if (args[0].equals("-p") || args[0].equals("-P")) {
                mainProgress.setPassword(args[1]);
                System.out.println("this command should combine with others. \n" +
                        "i.e. -p [PASSWORD] -d [TIMESTAMP]");
            } else if (args[0].equals("-d")) {
                mainProgress.delete(args[1]);
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-P") || args.equals("-p")) {
                    if (i != args.length - 1 && !(args[i + 1].equals("-i") || args[i + 1].equals("-s") || args[i + 1].equals("-d") || args[i + 1].equals("-?")))
                        mainProgress.setPassword(args[i + 1]);
                    break;
                }
                if (args[i].equals("-s")) mainProgress.scanS();
                if (args[i].equals("-d")) {
                    if (i == args.length - 1 || (args[i + 1].equals("-i") || args[i + 1].equals("-s") || args[i + 1].equals("-d") || args[i + 1].equals("-?")))
                        mainProgress.getSnapsS();
                    else if (i != args.length - 1 && !(args[i + 1].equals("-i") || args[i + 1].equals("-s") || args[i + 1].equals("-d") || args[i + 1].equals("-?"))) {
                        mainProgress.deleteS(args[i + 1]);
                    }
                }
            }
        }
    }

}
