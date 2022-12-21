package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    String ImagePath = "images";

    public Map uploadImage (MultipartFile file){
        File directory = new File(ImagePath);
        if (!directory.exists()) {
            try {
                directory.mkdir();
            } catch (SecurityException se) {
                return null;
            }
        }
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        final String path = ImagePath + File.separator + fileName;
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            byte[] buf = new byte[1024];
            int numRead = 0;
            while ((numRead = inputStream.read(buf)) >= 0) {
                fileOutputStream.write(buf, 0, numRead);
            }
        } catch (Exception e) {
            return null;
        }
        Map responseResult = new HashMap<>();
        responseResult.put("ProfilePhoto", fileName);
        return responseResult;
    }

    public byte[] getPhotoByUserID(Long id) {
        return imageRepository.findById(id).get().getImage();
    }



}
