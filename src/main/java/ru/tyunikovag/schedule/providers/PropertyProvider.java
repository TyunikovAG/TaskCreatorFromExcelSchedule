package ru.tyunikovag.schedule.providers;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PropertyProvider {

    private static final Properties properties = new Properties();
    private final File propertyFile = new File("settings.prp");

    public Properties getProperties() {
        if (!propertyFile.exists()) {
            createProperties();
        } else {
            System.out.println("property file exist");
            loadProperties();
            ferifyProps(PropertyName.SCHEDULE_FILE_NAME);
            ferifyProps(PropertyName.TASK_BLANK_FILE_NAME);
        }
        return properties;
    }

    private void createProperties() {
        System.out.println("creating property file");
        Path propertyPath = Paths.get("settings.prp");
        try {
            Files.createFile(propertyPath);
        } catch (IOException e) {
            System.out.println("can't CREATE property file settings.prp");
            e.printStackTrace();
        }
    }

    private void loadProperties() {
        try {
            FileInputStream inputStream = new FileInputStream(propertyFile);
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("can't LOAD property file settings.prp");
            e.printStackTrace();
        }
    }

    private void ferifyProps(PropertyName propertyName) {
        if (!properties.containsKey(propertyName.toString())){
            setFileForProperty(propertyName);
        } else {
            File propertyFile = new File(properties.getProperty(propertyName.toString()));
            if (!propertyFile.exists()) {
                setFileForProperty(propertyName);
            }
        }
    }

    public void saveProperties() {
        try {
            FileOutputStream fos = new FileOutputStream(propertyFile);
            String currentDate = new SimpleDateFormat("dd.MM.yyyy - HH.mm").format(new Date());
            properties.store(fos, currentDate);
        } catch (IOException e) {
            System.out.println("can't SAVE property file");
        }
    }

    public String get(PropertyName key) throws IllegalArgumentException{
        switch (key) {
            case SCHEDULE_FILE_NAME: {
                return getScheduleFileName();
            }
            case TASK_BLANK_FILE_NAME: {
                return getTaskBlankFileName();
            }
            default:
                throw new IllegalArgumentException();
        }
    }

    private String getTaskBlankFileName() {
        return properties.getProperty(PropertyName.TASK_BLANK_FILE_NAME.toString());
    }

    private String getScheduleFileName() {
        return properties.getProperty(PropertyName.SCHEDULE_FILE_NAME.toString());
    }

    public void setFileForProperty(PropertyName key) {
        String rootFolder = properties.getProperty(PropertyName.LAST_FOLDER.toString());
        if (rootFolder == null) {
            rootFolder = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        }
        JFileChooser chooser = new JFileChooser(rootFolder);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Excel files", "xls", "xlsx");
        chooser.setFileFilter(filter);
        switch (key) {
            case TASK_BLANK_FILE_NAME: {
                chooser.setDialogTitle("Укажите файл с бланком наряда");
                break;
            }
            case SCHEDULE_FILE_NAME: {
                chooser.setDialogTitle("Укажите файл с графиками");
            }
        }

        int returnValue = chooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            properties.setProperty(key.toString(), selectedFile.getAbsolutePath());
            properties.setProperty(PropertyName.LAST_FOLDER.toString(), selectedFile.getParent());

            JOptionPane.showMessageDialog(null, "Сохранение настроек", "InfoBox", JOptionPane.INFORMATION_MESSAGE);

        }

    }

    public enum PropertyName {
        SCHEDULE_FILE_NAME,
        TASK_BLANK_FILE_NAME,
        LAST_FOLDER,
    }
}
