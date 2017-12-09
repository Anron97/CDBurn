package CDBurn;

import CDBurn.Services.ExecuteCommandService;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class CDBurn {

    private List<File> listFiles = new LinkedList<>();

    public void addFile(File file) {
        listFiles.add(file);
    }

    public long getSizeAllFiles() {
       return (listFiles.stream().map(file -> file.length()))
               .reduce(0L,(sum, length) -> sum += length);
    }

    private void copyFiles() {
        for (File file : listFiles) {
            ExecuteCommandService.Execute("cp -R " + file.getAbsolutePath() + " /home/ptaxom/DiskBuffer");
        }
    }

    private void createDirectory() {
        ExecuteCommandService.Execute("mkdir /home/ptaxom/DiskBuffer");
    }

    private void umountDisk() {
        ExecuteCommandService.Execute("umount /dev/sr0");
    }

    private void createISO() {
        ExecuteCommandService.Execute("mkisofs -v -J -o Disk.iso /home/ptaxom/DiskBuffer");
    }

    private void formatDisk() {
        ExecuteCommandService.Execute("cdrecord -dev=/dev/sr0 -v -blank=fast");
    }

    private void writeFilesToDisk() {
        ExecuteCommandService.Execute("cdrecord -dev=/dev/sr0 -speed=16 -eject -v Disk.iso");
    }

    private void deleteISO() {
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
        }
    }

}

