package me.cbhud.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Profile;
import me.cbhud.repository.ProfileRepository;

import java.util.List;


@Path("/profile/")
public class ProfileResource {

    @Inject
    private ProfileRepository profileRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    public Response createStudent(Profile profile) {
        Profile p = profileRepository.createProfile(profile);
        return Response.ok().entity(p).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAll")
    public Response getProfileAllProfiles(){
        List<Profile> p = profileRepository.getAllProfiles();

        return Response.ok().entity(p).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public Response getProfileByName(@QueryParam("username") String username){
        List<Profile> p;
        try {
            p = profileRepository.getProfileByUsername(username);
        } catch (ProfileException e) {
            return Response.ok().entity(e.getMessage()).build();
        }
        return Response.ok().entity(p).build();
    }

//    @Scheduled(every = "10s")

}
