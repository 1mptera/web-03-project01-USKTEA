package models;

import java.util.HashMap;

public class ImagePath {
    private HashMap<String, String> imagePaths;

    public ImagePath(HashMap<String, String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String path(Service service) {
        return imagePaths.get(service.name());
    }
}
