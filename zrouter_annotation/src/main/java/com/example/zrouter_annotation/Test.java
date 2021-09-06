package com.example.zrouter_annotation;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) {
        String cmd = "E:\\sdk\\ndk\\19.2.5345600\\toolchains\\aarch64-linux-android-4.9\\prebuilt\\windows-x86_64\\bin\\aarch64-linux-android-addr2line.exe -f -C -e \"E:\\tools\\buglyqq-upload-symbol\\libunity.sym.so\" 0xc05058 0xc04bf8 0xc04b48 0x419b9c 0x56f214 0xc32088 0xc3e7ac 0x623d90 0x61ac4c 0x61ae14 0x5048a4 0x297364 0x506c80 0x506acc 0x506a38 0x58ebfc";
        String s = excuteCMDCommand(cmd);
        System.out.println(s);
    }

    public static String excuteCMDCommand(String cmdCommand) {
        StringBuilder stringBuilder = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
