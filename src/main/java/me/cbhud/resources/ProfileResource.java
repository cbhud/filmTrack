package me.cbhud.resources;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.dto.FileUploadForm;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Profile;
import me.cbhud.repository.ProfileRepository;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;

import static me.cbhud.dto.FileUploadForm.UPLOAD_DIR;

@Path("/profile/")
@Authenticated
public class ProfileResource {
    @Inject
    private ProfileRepository profileRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAll")
    @RolesAllowed("admin")
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
        } catch (ProfileException e) {
            return Response.ok().entity(e.getMessage()).build();
        }
        return Response.ok().entity(p).build();
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


        if (
                !form.filename.endsWith(".png") &&
                        !form.filename.endsWith(".jpg") &&
                        !form.filename.endsWith(".jpeg") &&
                        !form.filename.endsWith(".webp") &&
                        !form.filename.endsWith(".gif")
        ) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Only png, jpg, jpeg, webp, and gif files are allowed.")
                    .build();
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

            profile.setAvatarUrl(filePath.toString());

            profileRepository.createProfile(profile);

            System.out.println("Profile with id " + id + " has been uploaded successfully.");
            return Response.ok("File uploaded successfully.").build();
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to upload file.").build();
        }
    }

    @GET
    @Path("avatar")
    @Produces({"image/png", "image/jpeg", "image/jpg"})
    public Response getProfileImage(@QueryParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing 'id' query parameter").build();
        }

        Profile profile;
        try {
            profile = profileRepository.getProfileById(id);
        } catch (ProfileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Profile not found").build();
        }

        String filename = profile.getAvatarUrl(); // pretpostavimo da postoji getter za ime fajla u profilu

        if (filename == null || filename.isBlank()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Profile has no avatar uploaded").build();
        }

        File file = new File(filename);
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Avatar image file not found").build();
        }

        String mimeType = guessMimeType(filename);

        return Response.ok(file, mimeType).build();
    }

    private String guessMimeType(String filename) {
        if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else if (filename.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream"; // podrazumevani tip
    }




}
