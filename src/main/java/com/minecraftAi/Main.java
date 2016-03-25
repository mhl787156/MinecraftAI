package com.minecraftAi;

import java.io.IOException;

/**
 * Created by Mickey on 25/03/2016.
 */
public class Main {
    private final static String filePath = "G:\\Program Files (x86)\\Minecraft\\game\\launcher.jar";
    private final static String exec = "java -jar ";

    public static void main(String args[]) {
        //String[] temp = getUsernamePasswordFromFile().split(" ");
        // String username = temp[0];
        // String password = temp[1];

        try {
            System.out.println("Running Minecraft");
            System.out.println(exec + filePath);
            Process p = Runtime.getRuntime().exec(exec + "\"" + filePath + "\"");
            System.out.println("Finished Executing Minecraft");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUsernamePasswordFromFile(){
        return "a b";
    }

}
