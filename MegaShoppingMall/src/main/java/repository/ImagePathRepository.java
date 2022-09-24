package repository;

import infrastructure.FileManager;
import models.ImagePath;
import models.Service;

import java.io.FileNotFoundException;

public class ImagePathRepository {
    FileManager fileManager;
    ImagePath imagePath;

    public ImagePathRepository() throws FileNotFoundException {
        fileManager = new FileManager("imagePaths.csv");
        imagePath = fileManager.imagePaths();
    }

    public String getPath(Service service) {
        return imagePath.path(service);
    }
}
