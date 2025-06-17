package me.cbhud.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import java.io.*;


@jakarta.ws.rs.Path("/profile/file")

    public class FileUploadForm {

    public static final String UPLOAD_DIR = "uploads";

    @FormParam("file")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public InputStream file;

        @FormParam("filename")
        @PartType(MediaType.TEXT_PLAIN)
        public String filename;

    }