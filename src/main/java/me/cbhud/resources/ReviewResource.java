package me.cbhud.resources;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.ProfileException;
import me.cbhud.exception.ReviewException;
import me.cbhud.model.Review;
import me.cbhud.repository.ReviewRepository;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import static me.cbhud.resources.FileUploadForm.UPLOAD_DIR;

@Path("/review/")
public class ReviewResource {
    @Inject
    private ReviewRepository reviewRepository;


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("uploadReview")
        public Response uploadProfileFile(@MultipartForm ReviewUploadForm form, @QueryParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing 'id' query parameter.").build();
        }
        if (form == null || form.file == null || form.filename == null || form.filename.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("File and filename must be provided.").build();
        }

        Review review;
        try {
            review = reviewRepository.getReviewById(id);
        } catch (ReviewException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Profile with id " + id + " not found.").build();
        }

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            java.nio.file.Path filePath = Paths.get(UPLOAD_DIR, form.filename);
            try (OutputStream out = new FileOutputStream(filePath.toFile())) {
                form.file.transferTo(out);
            }

            review.setFilePath(filePath.toAbsolutePath().toString());
            review.setFileName(form.filename);
            review.setFileExtension(form.fileExtension);
            reviewRepository.updateReview(review);

            return Response.ok("File uploaded successfully.").build();
        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to upload file.").build();
        }
    }

}
