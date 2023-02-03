package com.example.HardMoneyLending.controller.property;

import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;
import com.example.HardMoneyLending.services.property.IPropertyService;
import com.example.HardMoneyLending.services.shared_file.ISharedFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/property")
public class PropertyFileController {

    @Value("${server.host.name}")
    private String domainUrl;

    private static final String PDF = "pdf";
    private final IPropertyService propertyService;
    private final ISharedFileService sharedFileService;

    public PropertyFileController(final IPropertyService service, IPropertyService propertyService, ISharedFileService sharedFileService) {
        this.propertyService = propertyService;
        this.sharedFileService = sharedFileService;
    }

    /**
     * Upload process
     */
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("files") MultipartFile[] multipartFiles,
                                 @RequestParam("fileType") String fileType,
                                 @RequestParam("propertyId") String propertyId,
                                 Principal principal)
            throws BadRequestException, EntityNotFoundException {
        List<String> m = propertyService.storeImage(multipartFiles, fileType, propertyId);
        return ResponseEntity.ok().body(m);
    }

    /**
     * Download process
     * */
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request,
                                           @RequestParam("filename") String filename,
                                           @RequestParam("type") String type, Principal principal)
            throws BadRequestException, EntityNotFoundException, MalformedURLException {

        String[] filenameSplit = filename.split("[.]");
        String contentType = filenameSplit[1];
        MediaType mediaType = null;
        switch (contentType) {
            case PDF:
                mediaType = MediaType.APPLICATION_PDF;
                break;
            default:
                throw new BadRequestException("File type is not supported");
        }

        isFileAccessibleForThisRequest(request, principal);

        byte[] data = sharedFileService.getFile(filename, type);
        if(data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(data.length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                    .body(data);
    }

    private void isFileAccessibleForThisRequest(HttpServletRequest request, Principal principal) throws EntityNotFoundException, MalformedURLException, BadRequestException {
        String requestQueryString = request.getQueryString();
        String urlWithSpaces = requestQueryString.replace("%20"," ");
        String documentUrl = domainUrl+ "/api/v1/property/download?" + urlWithSpaces;

        // Checking if the logged in user has this url
        boolean isExistInPropertyForBorrower = propertyService.isExistsInProperty(principal.getName(), documentUrl);
        if(!isExistInPropertyForBorrower) {
            boolean isExistInDocumentSharingDoc = sharedFileService.isDocumentUrlExistsForLoggedInUser(principal.getName(), documentUrl);
            if (!isExistInDocumentSharingDoc) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Document Access");
            }
        }
    }
}

