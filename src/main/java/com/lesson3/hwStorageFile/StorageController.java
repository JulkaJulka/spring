package com.lesson3.hwStorageFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class StorageController {

    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;

    @RequestMapping(method = RequestMethod.POST, value = "/putFile", produces = "text/plain")
    public @ResponseBody
    String put(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fId = req.getParameter("idFile");
        Long idFile = Long.parseLong(fId);

        String sId = req.getParameter("idStorage");
        Long idStorage = Long.parseLong(sId);

        try {
            Storage storage = storageService.findObjectById(idStorage);
            File file = fileService.findObjectById(idFile);

            return fileService.put(storage, file).toString();

        } catch (BadRequestException | HibernateException e) {

            e.printStackTrace();
            resp.getWriter().println("Saving unsuccessful " + e.getMessage());
            return "Saving unsuccessful " + e.getMessage();
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteFile", produces = "text/plain")
    protected @ResponseBody
    String deleteFile(HttpServletRequest req, HttpServletRequest res) throws IOException {

        String fId = req.getParameter("idFile");
        Long idFile = Long.parseLong(fId);

        try {

            Storage storage = convertJSONStringToStorage(req);
            File file = fileService.findObjectById(idFile);
            fileService.delete(storage, file);
            return "File id " + file.getId() + " was deleted successfully";

        } catch (BadRequestException | HibernateException e) {
            e.printStackTrace();
            return "Deleting unsuccessful " + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transferFile", produces = "text/plain")
    public @ResponseBody
    String transferFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fId = req.getParameter("idFile");
        Long idFile = Long.parseLong(fId);

        String strsFromId = req.getParameter("idStorageFrom");
        Long idStorageFrom = Long.parseLong(strsFromId);

        String strsToId = req.getParameter("idStorageTo");
        Long idStorageTo = Long.parseLong(strsToId);

        try {
            Storage storageFrom = storageService.findObjectById(idStorageFrom);
            Storage storageTo = storageService.findObjectById(idStorageTo);

            File transferFiles = fileService.transferFile(storageFrom, storageTo, idFile);

            return transferFiles.toString();

        } catch (BadRequestException | HibernateException e) {

            e.printStackTrace();
            resp.getWriter().println("Transfer file doesn't successfully " + e.getMessage());
            return "Transfer file doesn't successfully " + e.getMessage();
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/transferAll", produces = "text/plain")
    public @ResponseBody
    String transferAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String strsFromId = req.getParameter("idStorageFrom");
        Long idStorageFrom = Long.parseLong(strsFromId);

        String strsToId = req.getParameter("idStorageTo");
        Long idStorageTo = Long.parseLong(strsToId);

        try {
            Storage storageFrom = storageService.findObjectById(idStorageFrom);
            Storage storageTo = storageService.findObjectById(idStorageTo);
            List<File> trList = fileService.transferAll(storageFrom, storageTo);

            return trList.toString();

        } catch (BadRequestException | HibernateException e) {

            e.printStackTrace();
            resp.getWriter().println("TransferAll unsuccessfully " + e.getMessage());
            return "TransferAll unsuccessful " + e.getMessage();
        }

    }

    private Storage convertJSONStringToStorage(HttpServletRequest req) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            Storage storage = mapper.readValue(is, Storage.class);

            return storage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
