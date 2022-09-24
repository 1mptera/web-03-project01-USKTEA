package controller;

import models.Service;
import service.ImagePathService;

import java.io.FileNotFoundException;

public class ImagePathController {
    private ImagePathService imagePathService;

    public ImagePathController() throws FileNotFoundException {
        this.imagePathService = new ImagePathService();
    }

    public String getPath(Service service) {
        return imagePathService.getPath(service);
    }
}
