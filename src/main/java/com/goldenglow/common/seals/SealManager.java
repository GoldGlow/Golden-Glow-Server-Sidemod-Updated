package com.goldenglow.common.seals;


import com.goldenglow.common.util.Reference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SealManager {

    public static List<Seal> loadedSeals = new ArrayList<>();

    public static void init() {
        File dir = new File(Reference.sealsDir);
        if(!dir.exists())
            dir.mkdir();
        for(File f : dir.listFiles()) {
            if(f.getName().toLowerCase().endsWith(".js")) {
                try {
                    loadedSeals.add(new Seal(readFile(f)));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String readFile(final File file) throws IOException {
        final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        try {
            final StringBuilder stringBuilder3 = new StringBuilder();
            for (String string4 = bufferedReader2.readLine(); string4 != null; string4 = bufferedReader2.readLine()) {
                stringBuilder3.append(string4);
                stringBuilder3.append("\n");
            }
            return stringBuilder3.toString();
        }
        finally {
            bufferedReader2.close();
        }
    }

}
