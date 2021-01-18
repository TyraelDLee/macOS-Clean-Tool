package defaultPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class mainProgress {
    // tmutil deletelocalsnapshots [timestamp]
    private static final String DELETE = "tmutil deletelocalsnapshots";
    // show the all snaps related on timestamp
    private static final String SCAN = "tmutil listlocalsnapshots /";
    private static final String SUDO = " | sudo -S ";//-S is necessary, strange..
    private static final String BIN = "/bin/bash";
    private Observer observer;
    private String command  = "";
    private String password = "";
    private Process executor = null;
    private boolean observed = false;

    public mainProgress(){}

    public mainProgress(boolean observed){
        this.observed = observed;
    }

    public mainProgress(String command){
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
            if(this.observed){
                if(callBack.toString().isEmpty())
                    upd("Request pwd");
                else
                    upd(callBack.toString());
            }else{
                System.out.println(callBack.toString());
            }
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
        String callback = exec(new String[]{BIN, "-c", this.password+SUDO+SCAN});
        String[] snaps = callback.replace("com.apple.TimeMachine.","").replace(".local","").split("\n");
        for(String snap : snaps){
            deleteS(snap);
            System.out.println(snap);
        }
    }

    public static void main(String[] args){
        String[] command = new String[]{"-s"};
        mainProgress mainProgress = new mainProgress(false);
        Scanner input = new Scanner(System.in);
        if(command.length<1){
            mainProgress.scan();
        }else if (command.length==1){
            if(command[0].equals("?") || command[0].equals("-?")){
                System.out.println("-P/-p [PASSWORD] with sudo privilege");
                System.out.println("-d delete all snaps");
                System.out.println("-d [TIMESTAMP] delete the specific snap");
                System.out.println("-s scan the snaps on the disk(local only)");
                System.out.println("-i about");
            }else if(command[0].equals("-s")){
                System.out.println("scanning");
                mainProgress.scan();
            }else if(command[0].equals("-d")){
                mainProgress.getSnaps();
            }else if(command[0].equals("-i")){
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
        }else if(command.length==2){
            if(command[0].equals("-p")||command[0].equals("-P")){
                mainProgress.setPassword(command[1]);
                System.out.println("this command should combine with others. \n" +
                        "i.e. -p [PASSWORD] -d [TIMESTAMP]");
            }else if(command[0].equals("-d")){
                mainProgress.delete(command[1]);
            }
        }else{
            for (int i = 0; i < command.length; i++) {
                if (command[i].equals("-P")||command.equals("-p")){
                    if(i!=command.length-1 && !(command[i+1].equals("-i")||command[i+1].equals("-s")||command[i+1].equals("-d")||command[i+1].equals("-?")))
                        mainProgress.setPassword(command[i+1]);
                    break;
                }
                if(command[i].equals("-s")) mainProgress.scanS();
                if(command[i].equals("-d")){
                    if(i==command.length-1 || (command[i+1].equals("-i")||command[i+1].equals("-s")||command[i+1].equals("-d")||command[i+1].equals("-?")))
                        mainProgress.getSnaps();
                    else if(i!=command.length-1 && !(command[i+1].equals("-i")||command[i+1].equals("-s")||command[i+1].equals("-d")||command[i+1].equals("-?"))){
                        mainProgress.deleteS(command[i+1]);
                    }
                }
            }
        }
    }

    public void reg(Observer observer){
        this.observer = observer;
    }

    public void upd(String message){
        observer.update(message);
    }

}
