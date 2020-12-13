import logic.LogServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer implements Runnable {
    protected int serverPort = 2525;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;

    public MultiThreadedServer(int port) {
        this.serverPort = port;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    LogServer.printSystemServerInfo("Сервер остановлен.");
                    return;
                }
                throw new RuntimeException("Ошибка при приеме клиентского подключения", e);
            }
            new Thread(new ServerWork(clientSocket)).start();
        }
        LogServer.printSystemServerInfo("Сервер остановлен.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка закрытия сервера", e);
        }
    }

    private void openServerSocket() {
        LogServer.printSystemServerInfo("Открытие серверного сокета...");
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Не удается открыть порт " + this.serverPort, e);
        }
    }
}
