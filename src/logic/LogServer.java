package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract public class LogServer {
    private static String log;
    public static SimpleDateFormat format3 = new SimpleDateFormat("d/M/yyyy HH:mm:ss");
    private static final String path = "log.txt";
    private static File file = new File(path);

    public static void writeToFile(String str) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
//
//            char symbols[] = str.toCharArray();
//            for (char s : symbols) {
//                bw.write(s);
//            }
//            System.out.println("Данные успешно записаны в файл");
//        } catch (IOException ex) {
//            System.out.println(ex);
//        }
    }

    public static void printClientInputInfo(Object obj) {
        String message = "Данные полученные с клиента: " + obj;
        log = format3.format(new Date()) + " " + message + "\n";
        System.out.print(log);
        writeToFile(log);
    }

    public static void printServerDatabaseInfo(Object obj) {
        String message = "Данные полученные с базы данных: " + obj;
        log = format3.format(new Date()) + " " + message + "\n";
        System.out.print(log);
        writeToFile(log);
    }

    public static void printSystemServerInfo(Object obj) {
        String message = obj.toString();
        log = format3.format(new Date()) + " " + message + "\n";
        System.out.print(log);
        writeToFile(log);
    }
}
