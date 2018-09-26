package com.lesson3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Controller
public class StorageController {
    @Autowired
    private GeneralService generalService;

   /* @RequestMapping(method = RequestMethod.GET, value = "/showFile", produces = "text/plain")
    public @ResponseBody String showFile(@RequestBody String fileStr){
        try {
            // Storage storage1 = convertJSONStringToObjectStorage(storage);
            File file = convertJSONStringToObjectFile(fileStr);

            generalService.save(convertJSONStringToObjectStorage(storageStr),file);

            return file.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Saving unsuccessful " + e.getMessage();
        }

    }*/

    @RequestMapping(method = RequestMethod.POST, value = "/createFile", produces = "text/plain")
    public @ResponseBody
    String createFileInStorage(HttpServletRequest req, HttpServletResponse resp) throws IOException , HibernateException{
        String sId = req.getParameter("idStorage");
        Long idStorage = Long.parseLong(sId);
        try {

            // Storage storage1 = convertJSONStringToObjectStorage(storage);
           //
            File file = convertJSONStringToFile(req);
            Storage storage = generalService.findStorageById(idStorage);
            generalService.save(storage,file );
            return file.toString();
        } catch (BadRequestException | IOException e) {
            e.printStackTrace();
           // resp.getWriter().println("Saving unsuccessful " + e.getMessage());
            return "Saving unsuccessful " + e.getMessage();
        }

    }

    private File convertJSONStringToFile(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            File file = mapper.readValue(is, File.class);

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Storage convertJSONStringToObjectStorage(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            Storage storage= mapper.readValue(is, Storage.class);

            return storage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
