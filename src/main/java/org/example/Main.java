package org.example;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        // строка имя файл
        String fileName = args[0];

        //получаем reader из файла
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        // кладём json из файла в список
        ArrayList<LinkedTreeMap> maps = new Gson().fromJson(reader.readLine(), ArrayList.class);

        //закрытие ридера
        reader.close();

        //открытие connection
        String username = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ri_tape?useSSL=false", username, password);

        //добавляем сделки в базу
        for (int i = 0; i < maps.size(); i++) {
            long number = Long.valueOf(String.valueOf(maps.get(i).get("number")));
            String date = String.valueOf(maps.get(i).get("date"));
            String time = String.valueOf(maps.get(i).get("time"));
            int price = Integer.valueOf(String.valueOf(maps.get(i).get("price")));
            int quantity = Integer.valueOf(String.valueOf(maps.get(i).get("quantity")));
            String direction = String.valueOf(maps.get(i).get("direction"));

            String query = "INSERT INTO `ri_tape`.`deals` (`number`, `date`, `time`, `price`, `quantity`, `direction`) VALUES (?, ?, ?, ?, ?, ?);";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, number);
            statement.setString(2, date);
            statement.setString(3, time);
            statement.setInt(4, price);
            statement.setInt(5, quantity);
            statement.setString(6, direction);

            statement.executeUpdate();
        }

        connection.close();
    }
}
