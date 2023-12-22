package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.model.Image;
import ba.unsa.etf.ts.backend.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

//    @GetMapping
//    public ResponseEntity<List<ImageDTO>> getAllPictures() {
//        return ResponseEntity.ok(imageService.findAll());
//    }

//    @GetMapping("/image/{id}")
//    public ResponseEntity<byte[]> getPicture(@PathVariable Integer id){
//        Image image = imageService.getImage(id);
//        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(image.getImageData());
//    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Image> getPicture(@PathVariable Integer id){
        Image image = imageService.getImage(id);
        return ResponseEntity.ok().body(image);
    }

    @PostMapping(value = "/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadPicture(@RequestPart("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(imageService.addImage(file), HttpStatus.CREATED);
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity<Object> deletePicture(@PathVariable final Integer id) {
        return ResponseEntity.ok(imageService.deleteImage(id));
    }

//    @DeleteMapping
//    public ResponseEntity<String> deleteAll() {
//        imageService.deleteAll();
//        return ResponseEntity.ok("Successfully deleted!");
//    }
}