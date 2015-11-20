package com.testfairy.uploader;

import java.io.DataInputStream;
import java.util.List;

class ProcessExecutor {
    String execute(List<String> commands) {
        try {
            String outputString;
            String outputStringToReturn = "";

            ProcessBuilder pb = new ProcessBuilder(commands);
            Process process = pb.start();
            process.waitFor();
            DataInputStream curlIn = new DataInputStream(process.getInputStream());
            while ((outputString = curlIn.readLine()) != null) {
                outputStringToReturn = outputStringToReturn + outputString;
            }

            if (process.exitValue() > 0) {
                throw new RuntimeException((outputStringToReturn.isEmpty()) ? "Error on " + commands : outputStringToReturn);
            }
            return outputStringToReturn;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
