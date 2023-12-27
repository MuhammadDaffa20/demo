package com.example.demo;


import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Random;

public class PendaftaranKTP extends Application {

    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private static final String FILE_PATH = "user_data.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadUserDataFromFile();

        primaryStage.setTitle("Pendaftaran KTP");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);


        Label nameLabel = new Label("Nama:");
        Label addressLabel = new Label("Alamat:");
        Label birthDateLabel = new Label("Tanggal Lahir:");
        Label ktpNumberLabel = new Label("Nomor KTP:");


        TextField nameField = new TextField();
        TextField addressField = new TextField();
        DatePicker birthDatePicker = new DatePicker();
        TextField ktpNumberField = new TextField();
        ktpNumberField.setEditable(false);


        Button generateButton = new Button("Generate Nomor KTP");
        Button registerButton = new Button("Daftar");
        Button deleteButton = new Button("Hapus Data Terpilih");


        TableView<User> tableView = new TableView<>();
        TableColumn<User, String> nameColumn = new TableColumn<>("Nama");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> addressColumn = new TableColumn<>("Alamat");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<User, String> birthDateColumn = new TableColumn<>("Tanggal Lahir");
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        TableColumn<User, String> ktpNumberColumn = new TableColumn<>("Nomor KTP");
        ktpNumberColumn.setCellValueFactory(new PropertyValueFactory<>("ktpNumber"));
        tableView.getColumns().addAll(nameColumn, addressColumn, birthDateColumn, ktpNumberColumn);
        tableView.setItems(userList);


        generateButton.setOnAction(e -> generateKtpNumber(ktpNumberField));
        registerButton.setOnAction(e -> registerUser(nameField, addressField, birthDatePicker, ktpNumberField, nameField.getText(), addressField.getText(), ktpNumberField.getText()));
        deleteButton.setOnAction(e -> deleteSelectedUser(tableView));


        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(addressLabel, 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(birthDateLabel, 0, 2);
        grid.add(birthDatePicker, 1, 2);
        grid.add(ktpNumberLabel, 0, 3);
        grid.add(ktpNumberField, 1, 3);
        grid.add(generateButton, 0, 4);
        grid.add(registerButton, 1, 4);
        grid.add(tableView, 0, 5, 2, 1);
        grid.add(deleteButton, 0, 6);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(e -> saveUserDataToFile());

        primaryStage.show();
    }

    private void generateKtpNumber(TextField ktpNumberField) {
        Random random = new Random();
        StringBuilder ktpNumber = new StringBuilder("32"); // Assuming the first two digits are fixed
        for (int i = 0; i < 14; i++) {
            ktpNumber.append(random.nextInt(10));
        }
        ktpNumberField.setText(ktpNumber.toString());
    }

    private void registerUser(TextField nameField, TextField addressField, DatePicker birthDatePicker, TextField ktpNumberField, String name, String address, String ktpNumber) {
        if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || birthDatePicker.getValue() == null || ktpNumberField.getText().isEmpty()) {
            showAlert("Kolom Masih Kosong", "Semua kolom wajib diisi");
        } else {
            String birthDate = birthDatePicker.getValue().toString();
            User newUser = new User(name, address, birthDate, ktpNumber);
            userList.add(newUser);
            saveUserDataToFile(); // Simpan data ke file setelah pendaftaran
            showAlert("Pendaftaran Berhasil", "Pendaftaran KTP berhasil!");
        }
    }

    private void deleteSelectedUser(TableView<User> tableView) {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tableView.getItems().remove(selectedIndex);
            saveUserDataToFile(); // Save data to file after deletion
        } else {
            showAlert("Pilih pengguna untuk dihapus.", "Pendaftaran KTP berhasil!");
        }
    }

    private void showAlert(String message, String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUserDataFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 4) {
                    userList.add(new User(userData[0], userData[1], userData[2], userData[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserDataToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : userList) {
                bw.write(user.getName() + "," + user.getAddress() + "," + user.getBirthDate() + "," + user.getKtpNumber());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class User {
        private final SimpleStringProperty name;
        private final SimpleStringProperty address;
        private final SimpleStringProperty birthDate;
        private final SimpleStringProperty ktpNumber;

        public User(String name, String address, String birthDate, String ktpNumber) {
            this.name = new SimpleStringProperty(name);
            this.address = new SimpleStringProperty(address);
            this.birthDate = new SimpleStringProperty(birthDate);
            this.ktpNumber = new SimpleStringProperty(ktpNumber);
        }

        public String getName() {
            return name.get();
        }

        public String getAddress() {
            return address.get();
        }

        public String getBirthDate() {
            return birthDate.get();
        }

        public String getKtpNumber() {
            return ktpNumber.get();
        }

    }

}





