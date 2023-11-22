package co.ke.tucode.admin.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import co.ke.tucode.admin.entities.ProjectInfo;
import co.ke.tucode.admin.entities.ProjectLocation;
import co.ke.tucode.admin.entities.ProjectUpload;
import co.ke.tucode.admin.payloads.ProjectLocationPayload;
import co.ke.tucode.admin.payloads.ProjectUploadPayload;
import co.ke.tucode.admin.repositories.ProjectLocationRepo;
import co.ke.tucode.admin.repositories.ProjectUploadRepo;
import co.ke.tucode.admin.services.ProjectInfoService;

@RestController
@RequestMapping("/project_api/v1")
public class ProjectController {

    @Autowired
    private ProjectInfoService service;
    @Autowired
    private ProjectLocationRepo locationRepoService;
    @Autowired
    private ProjectUploadRepo uploadRepoService;

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_info", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_service(@ModelAttribute ProjectInfo projectInfo,
            UriComponentsBuilder builder) {
        if (service.existsByName(projectInfo.getProjectname()))
            return new ResponseEntity(HttpStatus.CONFLICT);
        else {
            projectInfo.setProjectLocation(new ProjectLocation(null, null, null, projectInfo.getUser_signature()));
            projectInfo.setProjectUpload(new ProjectUpload(null, null, null, projectInfo.getUser_signature()));
            service.save(projectInfo);
            return new ResponseEntity(projectInfo, HttpStatus.OK);
        }
    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_loc", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_loc(@ModelAttribute ProjectLocationPayload locationPayload,
            UriComponentsBuilder builder) {
        ProjectLocation projectLocation = null;
        ProjectInfo projectInfo = null;
        if (service.existsByName(locationPayload.getProjectname())) {
            projectLocation = locationRepoService
                    .findById(service.findByName(locationPayload.getProjectname()).get(0).getProjectLocationID())
                    .stream().collect(Collectors.toList()).get(0);
            projectLocation.setMap_keyword(locationPayload.getKeyword());
            projectLocation.setMap_location(locationPayload.getProjlocation());
            projectInfo = service.findByName(locationPayload.getProjectname()).get(0);
            projectInfo.setProjectLocation(projectLocation);
            service.save(projectInfo);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(locationPayload.getProjectname(), HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_put_file upload db
     * data.............................
     */
    @RequestMapping(value = "/post_file", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> put_file(@RequestParam("projectname") String projectname,
            @RequestParam("file") MultipartFile file) {
        ProjectUpload projectUpload = null;
        ProjectInfo projectInfo = null;
        if (service.existsByName(projectname)) {
            projectUpload = uploadRepoService
            .findById(service.findByName(projectname).get(0).getProjectUploadID())
            .stream().collect(Collectors.toList()).get(0);
            if (!file.isEmpty()) {
                try {
                    Byte[] bytes = new Byte[file.getBytes().length];
                    
                    int i = 0;
                    for (Byte b : file.getBytes()) {
                        bytes[i++] = b;
                    }

                    projectUpload.setImage(bytes);
                } catch (IOException e) {
                    return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
                }
                projectInfo = service.findByName(projectname).get(0);
                projectInfo.setProjectUpload(projectUpload);
                service.save(projectInfo);
                return new ResponseEntity(HttpStatus.OK);
            } else
                return new ResponseEntity("kindly choose a file",
                        HttpStatus.EXPECTATION_FAILED);
        }

        else
            return new ResponseEntity(projectname, HttpStatus.BAD_REQUEST);

    }

        /*
     * .......................obr_put_file upload db
     * data.............................
     */
    @RequestMapping(value = "/post_url", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> put_url(@ModelAttribute ProjectUploadPayload uploadPayload) {
        ProjectUpload projectUpload = null;
        ProjectInfo projectInfo = null;
        if (service.existsByName(uploadPayload.getProjectname())) {
            projectUpload = uploadRepoService
            .findById(service.findByName(uploadPayload.getProjectname()).get(0).getProjectUploadID())
            .stream().collect(Collectors.toList()).get(0);

                    projectUpload.setSrcurl(uploadPayload.getSrcurl());
                projectInfo = service.findByName(uploadPayload.getProjectname()).get(0);
                projectInfo.setProjectUpload(projectUpload);
                service.save(projectInfo);
                return new ResponseEntity(HttpStatus.OK);
}

        else
            return new ResponseEntity(uploadPayload.getProjectname(), HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_get_service retrieve all db
     * data.............................
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> get_service() {
        List<ProjectInfo> projectInfos = service.findAll();

        if (projectInfos.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(projectInfos, HttpStatus.OK);
    }

    // /*.......................obr_get_file_preview download db
    // data.............................*/
    // @RequestMapping(value = "/get_file_preview/{id}", method = RequestMethod.GET,
    // produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    // public ResponseEntity<?> get_file_preview(@PathVariable("id") long id,
    // HttpServletResponse response){
    // Africana_User certificate = service.findById(id);

    // if (certificate == null)
    // return new ResponseEntity("no data found for"+certificate.getChild_name(),
    // HttpStatus.NOT_FOUND);

    // else {
    // OutputStream stream = null;

    // byte[] bytes = new byte[certificate.getFathers_id().length];

    // try {
    // stream = new
    // FileOutputStream(FilePreview.TempFile.tempFilePath(certificate));

    // int i = 0;
    // for (Byte aByte: certificate.getFathers_id()){
    // bytes[i++] = aByte.byteValue();
    // }

    // stream.write(bytes);
    // stream.close();

    // response.sendRedirect("/obr_service/get_file_download/"+id);
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // return new ResponseEntity("done"+certificate.getChild_name(), HttpStatus.OK);
    // }

    // /*.......................obr_get_file_download download db
    // data.............................*/
    // @RequestMapping(value = "/get_file_download/{id}", method =
    // RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    // public ResponseEntity<?> get_file_download(@PathVariable("id") Long id){
    // String filename = FilePreview.TempFile.tempFilePath(service.findById(id));

    // Resource resource = null;

    // try {
    // resource = new UrlResource(Paths.get(filename).toUri());
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // }

    // if (resource.exists()){
    // return ResponseEntity.ok()
    // .header(HttpHeaders.CONTENT_DISPOSITION, "found")
    // .body(resource);

    // }

    // return new ResponseEntity("done"+service.findById(id).getChild_name(),
    // HttpStatus.OK);
    // }

    /*
     * .......................obr_put_service update db
     * data.............................
     */

    /*
     * .......................obr_delete_all_service update db
     * data.............................
     */
    @RequestMapping(value = "/delete_all_service", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllUsers() {
        service.deleteAll();

        return new ResponseEntity("db data erased", HttpStatus.NO_CONTENT);
    }
}
