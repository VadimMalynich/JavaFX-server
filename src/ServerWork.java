import logic.*;
import org.w3c.dom.ls.LSOutput;
import sql.DatabaseHandler;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerWork implements Runnable {
    protected Socket clientSocket = null;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Users user;
    private Companies company;
    String log;

    List<Users> usersList = new ArrayList<>();


    public ServerWork(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            ///
            LogServer.printSystemServerInfo("Клиент подключается к серверу!");
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            DatabaseHandler dbHandler = new DatabaseHandler();
            while (true) {
                String choice = inputStream.readObject().toString();
                LogServer.printClientInputInfo(choice);
                switch (choice) {
                    case "enter": {
                        LogServer.printSystemServerInfo("Попытка входа в аккаунт");
                        user = (Users) inputStream.readObject();
                        LogServer.printClientInputInfo(user);
                        ResultSet result = dbHandler.getUser(user);
                        int counter = 0;
                        while (result.next()) {
                            counter++;
                        }
                        if (counter >= 1) {
                            LogServer.printSystemServerInfo("Вход выполнен успешно!");
                            outputStream.writeObject("Успешно");
                            String role = dbHandler.returnUserRole(user);
                            outputStream.writeObject(role);
                            if (UserRole.ADMIN.getValue().equals(role)) {
                                LogServer.printClientInputInfo(user);
                                LogServer.printSystemServerInfo("Клиент авторизировался как администратор");
                            } else if (UserRole.COMPANY.getValue().equals(role)) {
                                company = new Companies(user.getLogin(), user.getPassword(), user.getRole());
                                company = dbHandler.checkCompanyInfo(company);
                                LogServer.printClientInputInfo(company);
                                LogServer.printSystemServerInfo("Клиент авторизировался как компания");
                            } else {
                                LogServer.printClientInputInfo(user);
                                LogServer.printSystemServerInfo("Клиент авторизировался как логист");
                            }
                        } else {
                            LogServer.printSystemServerInfo("Пользователь не зарегистрирован!");
                            outputStream.writeObject("Ошибка");
                        }
                        break;
                    }
                    case "signUp": {
                        LogServer.printSystemServerInfo("Регистрация пользователя");
                        String str = inputStream.readObject().toString();
                        LogServer.printClientInputInfo(str);
                        if (!"cancel".equals(str)) {
                            Users user = (Users) inputStream.readObject();
                            LogServer.printClientInputInfo(user);
                            boolean flag = dbHandler.signUpUser(user);
                            if (flag == true) {
                                outputStream.writeObject("Пользователь успешно зарегистрирован!");
                                LogServer.printSystemServerInfo("Пользователь успешно зарегистрирован!");
                            } else {
                                outputStream.writeObject("Не удалось зарегистрировать пользователя!");
                                LogServer.printSystemServerInfo("Не удалось зарегистрировать пользователя!");
                            }
                        }
                        break;
                    }
                    case "exitCompanyAccount": {
                        LogServer.printSystemServerInfo("Выход из аккаунта: " + company.getLogin());
                        break;
                    }
                    case "exitLogistAccount": {
                        LogServer.printSystemServerInfo("Выход из аккаунта: " + user.getLogin());
                        break;
                    }
                    case "getUsers": {
                        LogServer.printSystemServerInfo("Считывание и отправка дынных о компаниях на клиент!");
                        List<Users> usersList = dbHandler.getUsers();
                        outputStream.writeObject(usersList.size());
                        if (usersList.size() > 0) {
                            for (Users u : usersList) {
                                outputStream.writeObject(u);
                                LogServer.printServerDatabaseInfo(u);
                            }
                        }
                        break;
                    }
                    case "deleteUser": {
                        LogServer.printSystemServerInfo("Удаление пользователя!");
                        Users user = (Users) inputStream.readObject();
                        LogServer.printClientInputInfo(user);
                        boolean flag = dbHandler.deleteUser(user);
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "updateUsers": {
                        LogServer.printSystemServerInfo("Считывание измененных дынных о пользователях с клиента!");
                        int count = (int) inputStream.readObject();
                        usersList.clear();
                        for (int i = 0; i < count; i++) {
                            usersList.add((Users) inputStream.readObject());
                        }
                        boolean flag = false;
                        for (Users u : usersList) {
                            flag = dbHandler.updateUsers(u);
                            if (UserRole.LOGIST.getValue().equals(u.getRole()) || UserRole.ADMIN.getValue().equals(u.getRole())) {
                                dbHandler.deleteCompanyInfo(u);
                            }
                        }
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "checkCompanyInfo": {
                        LogServer.printSystemServerInfo("Отправка данных о компании на клиент!");
                        outputStream.writeObject(company);
                        break;
                    }
                    case "setCompanyInfo": {
                        LogServer.printSystemServerInfo("Запись данных о компании в базу данных!");
                        Companies clone = company.copy();
                        clone = (Companies) inputStream.readObject();
                        company.setName(clone.getName());
                        company.setCategory(clone.getCategory());
                        company.setEmail(clone.getEmail());
                        company.setPhoneNumber(clone.getPhoneNumber());
                        company.setStatus(clone.getStatus());
                        LogServer.printClientInputInfo(company);
                        dbHandler.setCompanyInfo(company);
                        break;
                    }
                    case "editCompanyInfo": {
                        LogServer.printSystemServerInfo("Редактирование данных о компании!");
                        Companies clone = company.copy();
                        clone = (Companies) inputStream.readObject();
                        company.setName(clone.getName());
                        company.setCategory(clone.getCategory());
                        company.setEmail(clone.getEmail());
                        company.setPhoneNumber(clone.getPhoneNumber());
                        company.setStatus(clone.getStatus());
                        LogServer.printClientInputInfo(company);
                        boolean flag = dbHandler.editCompanyInfo(company);
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "addProductRequest": {
                        LogServer.printSystemServerInfo("Добавление запроса компании в базу данных!");
                        ProductRequest productRequest = (ProductRequest) inputStream.readObject();
                        LogServer.printClientInputInfo(productRequest);
                        boolean flag = dbHandler.addCompanyRequest(productRequest);
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "deleteCompanyRequest": {
                        LogServer.printSystemServerInfo("Удаление заявки!");
                        ProductRequest productRequest = (ProductRequest) inputStream.readObject();
                        LogServer.printClientInputInfo(productRequest);
                        boolean flag = dbHandler.deleteCompanyRequest(productRequest);
                        List<Warehouses> warehousesList = dbHandler.getWarehouses();
                        for (Warehouses w : warehousesList) {
                            dbHandler.setWarehouseCurrentAmount(w);
                        }
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "getRequestTableData": {
                        LogServer.printSystemServerInfo("Считывание и отправка дынных о запросах на клиент!");
                        boolean bool = (Boolean) inputStream.readObject();
                        List<ProductRequest> requests = dbHandler.getRequestData(company, bool);
                        outputStream.writeObject(requests.size());
                        if (requests.size() > 0) {
                            for (ProductRequest pr : requests) {
                                outputStream.writeObject(pr);
                                LogServer.printServerDatabaseInfo(pr);
                            }
                        }
                        break;
                    }
                    case "updateProductRequestTable": {
                        LogServer.printSystemServerInfo("Считывание измененных дынных о компаниях с клиента!");
                        int count = (int) inputStream.readObject();
                        List<ProductRequest> productRequests = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            productRequests.add((ProductRequest) inputStream.readObject());
                        }
                        boolean flag = false;
                        for (ProductRequest pr : productRequests) {
                            flag = dbHandler.updateProductRequestTable(pr);
                            if (RequestStatus.APPROVED.getValue().equals(pr.getStatus())) {
                                dbHandler.setProductInStock(pr);
                            } else if (RequestStatus.DENIED.getValue().equals(pr.getStatus()) ||
                                    RequestStatus.IN_PROCESSING.getValue().equals(pr.getStatus())) {
                                dbHandler.deleteProductInStock(pr);
                            }
                        }
                        List<Warehouses> warehousesList = dbHandler.getWarehouses();
                        for (Warehouses w : warehousesList) {
                            dbHandler.setWarehouseCurrentAmount(w);
                        }
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "getCompaniesTableData": {
                        LogServer.printSystemServerInfo("Считывание и отправка дынных о компаниях на клиент!");
                        List<Companies> companiesList = dbHandler.getCompaniesTableData();
                        outputStream.writeObject(companiesList.size());
                        if (companiesList.size() > 0) {
                            for (Companies pr : companiesList) {
                                outputStream.writeObject(pr);
                                LogServer.printServerDatabaseInfo(pr);
                            }
                        }
                        break;
                    }
                    case "updateCompanyTable": {
                        LogServer.printSystemServerInfo("Считывание измененных дынных о компаниях с клиента!");
                        int count = (int) inputStream.readObject();
                        List<Companies> companiesList = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            companiesList.add((Companies) inputStream.readObject());
                        }
                        boolean flag = false;
                        for (Companies c : companiesList) {
                            flag = dbHandler.updateCompanyTable(c);
                        }
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "addWarehouse": {
                        LogServer.printSystemServerInfo("Добавление склада!");
                        Warehouses warehouse = (Warehouses) inputStream.readObject();
                        LogServer.printClientInputInfo(warehouse);
                        boolean flag = dbHandler.addWarehouse(warehouse);
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "getWarehouses": {
                        LogServer.printSystemServerInfo("Считывание и отправка дынных о компаниях на клиент!");
                        List<Warehouses> warehousesList = dbHandler.getWarehouses();
                        outputStream.writeObject(warehousesList.size());
                        if (warehousesList.size() > 0) {
                            for (Warehouses wh : warehousesList) {
                                outputStream.writeObject(wh);
                                LogServer.printServerDatabaseInfo(wh);
                            }
                        }
                        break;
                    }
                    case "deleteWarehouse": {
                        LogServer.printSystemServerInfo("Удаление склада!");
                        Warehouses warehouse = (Warehouses) inputStream.readObject();
                        LogServer.printClientInputInfo(warehouse);
                        boolean flag = dbHandler.deleteWarehouse(warehouse);
                        if (flag) {
                            sendResultMessage(flag, outputStream);
                        } else {
                            outputStream.writeObject("За этим складом закреплены компании(ожидают подтверждения)" +
                                    " или на складе хранятся товары!\nУдаление невозможно!");
                        }
                        break;
                    }
                    case "updateWarehousesTable": {
                        LogServer.printSystemServerInfo("Считывание измененных дынных о компаниях с клиента!");
                        int count = (int) inputStream.readObject();
                        List<Warehouses> warehousesList = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            warehousesList.add((Warehouses) inputStream.readObject());
                        }
                        boolean flag = false;
                        for (Warehouses w : warehousesList) {
                            flag = dbHandler.updateWarehousesTable(w);
                        }
                        sendResultMessage(flag, outputStream);
                        break;
                    }
                    case "getWarehousesCategory": {
                        LogServer.printSystemServerInfo("Считывание и отправка дынных о компаниях на клиент!");
                        List<String> stringList = dbHandler.getWarehousesCategory();
                        outputStream.writeObject(stringList.size());
                        if (stringList.size() > 0) {
                            for (String str : stringList) {
                                outputStream.writeObject(str);
                                LogServer.printServerDatabaseInfo(str);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            LogServer.printSystemServerInfo("Клиент отключен.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void sendResultMessage(boolean flag, ObjectOutputStream outputStream) throws IOException {
        if (flag == true) {
            outputStream.writeObject("Успешно");
        } else {
            outputStream.writeObject("Ошибка");
        }
    }
}