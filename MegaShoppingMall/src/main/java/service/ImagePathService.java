package service;

import models.Service;
import repository.ImagePathRepository;

import java.io.FileNotFoundException;

public class ImagePathService {
    ImagePathRepository imagePathRepository;

    public ImagePathService() throws FileNotFoundException {
        this.imagePathRepository = new ImagePathRepository();
    }

    public String getPath(Service service) {
        return imagePathRepository.getPath(service);
    }
}
