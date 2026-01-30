package com.javarush.barenkov.crypto.client;

import com.javarush.barenkov.crypto.service.CryptoService;
import com.javarush.barenkov.crypto.service.Mode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.File;

public class FxGui extends Application {
    private TextField inputFileField, outputFileField, shiftField;
    private File inputFile, outputFile;
    private Button inputBtn,  outputBtn, processButton;
    private Label statusLabel;
    private ComboBox<String> algorithmCombo;
    private final CryptoService cryptoService = new CryptoService();
    private static final double FIELD_WIDTH = 300;
    private static final double SHIFT_FIELD_WIDTH = 130;
    private static final String[] ALGORITHMS = {
            "CAESAR_DECRYPTOR", "CAESAR_ENCRYPTOR",
            "CAESAR_BRUTEFORCE", "CAESAR_ANALYZE"
    };
    private static final String DEFAULT_ALGORITHM = "CAESAR_DECRYPTOR";
    private static final String DEFAULT_FILENAME = "decrypted_result.txt";

    @Override
    public void start(Stage stage) {
        initUI(stage);
        setupEventHandlers(stage);
        stage.show();
    }

    private Mode getSelectMode() {
        return switch (algorithmCombo.getValue()) {
            case "CAESAR_DECRYPTOR" -> Mode.CAESAR_DECRYPTOR;
            case "CAESAR_ENCRYPTOR" -> Mode.CAESAR_ENCRYPTOR;
            case "CAESAR_BRUTEFORCE" -> Mode.CAESAR_BRUTEFORCE;
            case "CAESAR_ANALYZE" -> Mode.CAESAR_STATIST_ANALIS_DECOD;
            default -> Mode.CAESAR_DECRYPTOR;
        };
    }

    private void initUI(Stage stage) {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));

        inputBtn = new Button("📁 Выбрать файл");

        Label inputLabel = new Label("Входной файл:");
        inputFileField = new TextField();
        inputFileField.setEditable(false);
        inputFileField.setPrefWidth(FIELD_WIDTH);

        Label algoLabel = new Label("Алгоритм:");
        algorithmCombo = new ComboBox<>();
        algorithmCombo.getItems().addAll(ALGORITHMS);
        algorithmCombo.setValue(DEFAULT_ALGORITHM);

        Label shiftLabel = new Label("Сдвиг (Цезарь):");
        shiftField = new TextField("3");
        shiftField.setPrefWidth(SHIFT_FIELD_WIDTH);
        shiftField.setDisable(false);

        Label outputLabel = new Label("Файл результата:");
        outputFileField = new TextField();
        outputFileField.setEditable(false);
        outputFileField.setPrefWidth(FIELD_WIDTH);

        outputBtn = new Button("💾 Выбрать файл для сохранения");

        processButton = new Button("▶ Выполнить");
        statusLabel = new Label("Готов к работе");

        formGrid.add(inputLabel, 0, 0);
        formGrid.add(inputFileField, 1, 0);
        formGrid.add(inputBtn, 2, 0);

        formGrid.add(algoLabel, 0, 1);
        formGrid.add(algorithmCombo, 1, 1);
        formGrid.add(shiftLabel, 2, 1);
        formGrid.add(shiftField, 3, 1);

        formGrid.add(outputLabel, 0, 2);
        formGrid.add(outputFileField, 1, 2);
        formGrid.add(outputBtn, 2, 2);

        VBox root = new VBox(20, formGrid, processButton, statusLabel);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 800, 300);
        stage.setTitle("Криптоанализ файлов");
        stage.setScene(scene);
    }

    private void setupEventHandlers(Stage stage) {
        FileChooser openChooser = createFileChooser(
                "Выберите файл для обработки",
                new FileChooser.ExtensionFilter("Текстовый файл", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        FileChooser saveChooser = createFileChooser(
                "Выберите файл для сохранения результата",
                new FileChooser.ExtensionFilter("Текстовый файл", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        saveChooser.setInitialFileName(DEFAULT_FILENAME);

        inputBtn.setOnAction(e -> {
            inputFile = openChooser.showOpenDialog(stage);
            if (inputFile != null) {
                inputFileField.setText(inputFile.getAbsolutePath());
                statusLabel.setText("Выбран файл: " + inputFile.getName());

                if (outputFile != null) {
                    outputFileField.setText(outputFile.getAbsolutePath());
                }
            }
        });

        outputBtn.setOnAction(e -> {
            outputFile = saveChooser.showSaveDialog(stage);
            if (outputFile != null) {
                outputFileField.setText(outputFile.getAbsolutePath());
                statusLabel.setText("Файл сохранения: " + outputFile.getName());
            }
        });

        processButton.setOnAction(e -> processSelectedFile());

        algorithmCombo.setOnAction(e -> {
            String selected = algorithmCombo.getValue();
            shiftField.setDisable(!isNeedShift(selected));

            updateProcessButtonText(selected);
        });
    }

    private void updateProcessButtonText(String algorithm) {
        switch (algorithm) {
            case "CAESAR_DECRYPTOR":
            case "CAESAR_BRUTEFORCE":
            case "CAESAR_ANALYZE":
                processButton.setText("🔓 Расшифровать");
                break;
            case "CAESAR_ENCRYPTOR":
                processButton.setText("🔐 Зашифровать");
                break;
            default:
                processButton.setText("▶ Выполнить");
        }
    }

    private boolean isNeedShift(String algorithm) {
        return algorithm.equals("CAESAR_DECRYPTOR") || algorithm.equals("CAESAR_ENCRYPTOR");
    }

    private void processSelectedFile() {
        if (!validateInput()) {
            return;
        }

        try {
            Mode mode = getSelectMode();
            int shift = getShiftValue();

            cryptoService.processFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), mode, shift);

            statusLabel.setText("✅ Операция выполнена успешно!");
        } catch (NumberFormatException e) {
            statusLabel.setText("❌ Неверное значение сдвига!");
            showErrorDialog("Ошибка ввода", "Укажите корректное числовое значение сдвига");
        } catch (Exception e) {
            statusLabel.setText("❌ Неизвестная ошибка!");
            showErrorDialog("Ошибка","Произошла непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Произошла ошибка");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (inputFile == null) {
            errors.append("• Выберите входной файл\n");
        } else if (!inputFile.exists()) {
            errors.append("• Входной файл не существует\n");
        } else if (!inputFile.canRead()) {
            errors.append("• Нет прав на чтение входного файла\n");
        }

        if (outputFile == null) {
            errors.append("• Выберите файл для сохранения\n");
        } else if (outputFile.exists() && !outputFile.canWrite()) {
            errors.append("• Нет прав на запись в выходной файл\n");
        }

        if (!errors.isEmpty()) {
            showErrorDialog("Ошибка валидации", errors.toString());
            statusLabel.setText("❌ Исправьте ошибки в форме");
            return false;
        }

        return true;
    }

    private int getShiftValue() {
        if (shiftField.isDisabled()) {
            return 0;
        }
        return Integer.parseInt(shiftField.getText());
    }


    private FileChooser createFileChooser(String title, FileChooser.ExtensionFilter... filters) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        if (filters.length > 0) {
            chooser.getExtensionFilters().addAll(filters);
        }
        return chooser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
