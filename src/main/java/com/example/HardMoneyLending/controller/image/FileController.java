package com.example.HardMoneyLending.controller.image;


import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;
import com.example.HardMoneyLending.services.image.IImageService;
import com.example.HardMoneyLending.utils.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Locale;

/**
 * This controller creates the file urls and saved into the corresponding entities.
 * NOTE: If you change the url of the controller's class level or method level than
 * you have to also modify the Constants.DOWNLOAD_PROFILE_PIC_URL which is the saved
 * images' URL.
 *
 * @author Qasim Ali
 * */
@RestController
@RequestMapping("api/v1/file")
public class FileController {

    private static final String S_JPG = "jpg";
    private static final String C_JPG = "JPG";
    private static final String S_JPEG = "jpeg";
    private static final String C_JPEG = "JPEG";
    private static final String S_PNG = "png";
    private static final String C_PNG = "PNG";
    private static final String S_PDF = "pdf";
    private static final String C_PDF = "PDF";

    @Autowired
    private IImageService service;

    /**
     * Upload process
     */
    @PostMapping("/upload")
    public ResponseEntity upload(@NotNull @RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam("propertyTypeId") String propertyTypeId,
                                 Principal principal)
            throws BadRequestException{
        if(!MediaType.valueOf(multipartFile.getContentType()).getType().equalsIgnoreCase("image"))
            throw new BadRequestException("Provided file is not an image.");
        Message m = service.storeImage(multipartFile, propertyTypeId, principal);
        return ResponseEntity.status(m.getStatus()).body(m);
    }

    /**
     * Download process
     * */
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("filename") String filename, @RequestParam("type") String type, Principal principal)
            throws BadRequestException {
        String[] filenameSplit = filename.split("[.]");
        String contentType = filenameSplit[1];
        MediaType mediaType = null;
        switch (contentType) {
            case S_JPG:
            case S_JPEG:
            case C_JPG:
            case C_JPEG:
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case S_PNG:
            case C_PNG:
                mediaType = MediaType.IMAGE_PNG;
                break;
            case C_PDF:
            case S_PDF:
                mediaType = MediaType.APPLICATION_PDF;
                break;
            default:
                throw new BadRequestException("Image type is not supported");
        }

        byte[] data = service.getImage(filename, type);
        if(data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(data.length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                    .body(data);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(String userType, Principal principal) throws BadRequestException {
        Message m = service.deleteProfileImage(userType, principal);
        return ResponseEntity.status(m.getStatus()).body(m);
    }
}
