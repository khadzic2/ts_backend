package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Image;
import ba.unsa.etf.ts.backend.repository.ImageRepository;
import ba.unsa.etf.ts.backend.util.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String deleteImage(final Integer id) {
        imageRepository.findById(id).orElseThrow(()->new NotFoundException("Image by id:"+id+" does not exist."));
        imageRepository.deleteById(id);
        return "Successfully deleted!";
    }

    public Image getImage(Integer id){
        Image image = imageRepository.findById(id).orElse(null);
        if(image == null) throw new NotFoundException("Image by id:"+id+" does not exist.");
        image.setImageData(ImageUtil.decompressImage(image.getImageData()));
        return image;
    }

    public Image addImage(MultipartFile file) throws IOException {
        Image pImage = new Image();
        pImage.setImageData(ImageUtil.compressImage(file.getBytes()));
        pImage.setName(file.getOriginalFilename());
        pImage.setType(file.getContentType());
        return imageRepository.save(pImage);
    }
}
