package me.cbhud.resources;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Profile;
import me.cbhud.model.ProfileMovie;
import me.cbhud.repository.ProfileRepository;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;

import static me.cbhud.resources.FileUploadForm.UPLOAD_DIR;

@Path("/profile/")
public class ProfileResource {
    @Inject
    private ProfileRepository profileRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAll")
    public Response getAllProfiles() {
        List<Profile> p = profileRepository.getAllProfiles();
        return Response.ok().entity(p).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public Response getProfileByName(@QueryParam("username") String username) {
        List<Profile> p;
        try {
            p = profileRepository.getProfileByUsername(username);
        } catch (ProfileException e) {
            return Response.ok().entity(e.getMessage()).build();
        }
        return Response.ok().entity(p).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public Response getProfileById(@QueryParam("id") int id) {
        Profile p;
        try {
            p = profileRepository.getProfileById(id);
            File slika = new File(p.getFilePath());
        } catch (ProfileException e) {
            return Response.ok().entity(e.getMessage()).build();
        }
        return Response.ok().entity(p).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    public Response createProfile(Profile profile) {
        Profile p = profileRepository.createProfile(profile);
        return Response.ok().entity(p).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("createProfileMovie")
    public Response createPm(ProfileMovie pm) {
        ProfileMovie profileMovie =  profileRepository.createProfileMovie(pm);
        return Response.ok().entity(profileMovie).build();
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("uploadAvatar")
    public Response uploadProfileFile(@MultipartForm FileUploadForm form, @QueryParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing 'id' query parameter.").build();
        }
        if (form == null || form.file == null || form.filename == null || form.filename.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("File and filename must be provided.").build();
        }

        Profile profile;
        try {
            profile = profileRepository.getProfileById(id);
        } catch (ProfileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Profile with id " + id + " not found.").build();
        }

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            java.nio.file.Path filePath = Paths.get(UPLOAD_DIR, form.filename);
            try (OutputStream out = new FileOutputStream(filePath.toFile())) {
                form.file.transferTo(out);
            }

            profile.setFilePath(filePath.toAbsolutePath().toString());
            profile.setFileName(form.filename);
            profileRepository.createProfile(profile);

            return Response.ok("File uploaded successfully.").build();
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to upload file.").build();
        }
    }

}
