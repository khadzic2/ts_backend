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

//    public List<Image> findAll() {
////        return imageRepository.findAll()
////                .stream()
////                .map(image -> mapToDTO(image, new ImageDTO()))
////                .collect(Collectors.toList());
//        return imageRepository.findAll();
//    }

    public String deleteImage(final Integer id) {
        imageRepository.findById(id).orElseThrow(()->new NotFoundException("Image by id:"+id+" does not exist."));
        imageRepository.deleteById(id);
        return "Successfully deleted!";
    }

//    public void deleteAll() {
//        imageRepository.deleteAll();
//    }

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

//    private ImageDTO mapToDTO(final Image image, final ImageDTO imageDTO) {
//        imageDTO.setId(image.getId());
//        imageDTO.setName(image.getName());
//        imageDTO.setType(image.getType());
//        imageDTO.setImageData(ImageUtil.decompressImage(image.getImageData()));
//        return imageDTO;
//    }
//
//    private void mapToEntity(final MultipartFile file, final Image image) throws IOException{
//        image.setName(file.getOriginalFilename());
//        image.setType(file.getContentType());
//        image.setImageData(ImageUtil.compressImage(file.getBytes()));
//    }
}
