package CDBurn;

import CDBurn.Services.ExecuteCommandService;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class CDBurn {

    private List<File> listFiles = new LinkedList<>();
    private StringProperty progressValue;
    private Controller controller;

    public CDBurn(StringProperty progressValue, Controller controller) {
        this.progressValue = progressValue;
        this.controller = controller;

    }

    public void addFile(File file) {
        listFiles.add(file);
    }


    public long getSizeAllFiles() {
       return (listFiles.stream().map(file -> file.length()))
               .reduce(0L,(sum, length) -> sum += length);
    }

    private void copyFiles() {
        Platform.runLater(() -> progressValue.setValue("Copy files to temp directory DiskBuffer"));
        for (File file : listFiles) {
            ExecuteCommandService.Execute("cp -R " + file.getAbsolutePath() + " /home/ptaxom/DiskBuffer");
        }
    }

    private void createDirectory() {
        Platform.runLater(() -> progressValue.setValue("Create temp directory DiskBuffer"));
        ExecuteCommandService.Execute("mkdir /home/ptaxom/DiskBuffer");
    }

    private void umountDisk() {
        Platform.runLater(() -> progressValue.setValue("Umount disk"));
        ExecuteCommandService.Execute("umount /dev/sr0");
    }

    private void createISO() {
        Platform.runLater(() -> progressValue.setValue("Create ISO Disk.iso"));
        ExecuteCommandService.Execute("mkisofs -v -J -o Disk.iso /home/ptaxom/DiskBuffer");
    }

    private void formatDisk() {
        Platform.runLater(() -> progressValue.setValue("Format CD disk"));
        ExecuteCommandService.Execute("cdrecord -dev=/dev/sr0 -v -blank=fast");
    }

    private void writeFilesToDisk() {
        Platform.runLater(() -> progressValue.setValue("Record Disk.iso to CD"));
        ExecuteCommandService.Execute("cdrecord -dev=/dev/sr0 -speed=16 -eject -v Disk.iso");
    }

    private void deleteISO() {
        Platform.runLater(() -> progressValue.setValue("Delete temp files"));
        ExecuteCommandService.Execute("rm /home/ptaxom/Disk.iso");
    }

    private void deleteFiles() {
        ExecuteCommandService.Execute("rm -rf /home/ptaxom/DiskBuffer");
    }

    public void burnFiles() {
        if (!listFiles.isEmpty()) {
            createDirectory();
            copyFiles();
            umountDisk();
            createISO();
            formatDisk();
            writeFilesToDisk();
            deleteISO();
            deleteFiles();
            Platform.runLater(() -> progressValue.setValue("Complete!"));
            controller.switchStateButtons();
            listFiles.clear();
        }
    }

}

