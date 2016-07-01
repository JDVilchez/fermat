package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.rest;

import com.bitdubai.fermat_api.layer.all_definition.location_system.NetworkNodeCommunicationDeviceLocation;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInActor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.rest.Actors</code>
 * <p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 29/06/2016.
 *
 * @author  rrequena
 * @version 1.0
 * @since   Java JDK 1.7
 */
@Path("/admin/actors")
public class Actors implements RestFulServices {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(Actors.class));

    /**
     * Represent the daoFactory
     */
    private DaoFactory daoFactory;

    /**
     * Represent the gson
     */
    private Gson gson;

    /**
     * Constructor
     */
    public Actors(){
        super();
        this.daoFactory  = (DaoFactory) NodeContext.get(NodeContextItem.DAO_FACTORY);
        this.gson = GsonProvider.getGson();
    }

    /**
     * Get all check in actors in the  node
     * @param offSet
     * @param max
     * @return Response
     */
    @GET
    @Path("/check_in")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCheckInActors(@QueryParam("offSet") Integer offSet, @QueryParam("max") Integer max){

        LOG.info("Executing getCheckInActors");
        LOG.info("offset = "+offSet);
        LOG.info("max = "+max);

        try {

            List<ActorProfile> actorProfilesRegistered = new ArrayList<>();
            List<CheckedInActor> checkedInActorList = daoFactory.getCheckedInActorDao().findAll(offSet, max);

            for (CheckedInActor checkedInActor :checkedInActorList) {
                actorProfilesRegistered.add(buildActorProfileFromCheckedInActor(checkedInActor));
            }

            LOG.info("actorProfilesRegistered.size() = " + actorProfilesRegistered.size());

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", Boolean.TRUE);
            jsonObject.addProperty("identities", gson.toJson(actorProfilesRegistered));
            jsonObject.addProperty("total", daoFactory.getCheckedInActorDao().getAllCount());

            return Response.status(200).entity(gson.toJson(jsonObject)).build();

        } catch (CantReadRecordDataBaseException e) {

            e.printStackTrace();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", Boolean.FALSE);
            jsonObject.addProperty("details", e.getMessage());

            return Response.status(200).entity(gson.toJson(jsonObject)).build();

        }

    }

    /**
     * Get a ActorProfile from a CheckedInActor
     * @param actor
     * @return ActorProfile
     */
    private ActorProfile buildActorProfileFromCheckedInActor(CheckedInActor actor){

        ActorProfile actorProfile = new ActorProfile();
        actorProfile.setIdentityPublicKey(actor.getIdentityPublicKey());
        actorProfile.setAlias(actor.getAlias());
        actorProfile.setName(actor.getName());
        actorProfile.setActorType(actor.getActorType());
        actorProfile.setPhoto(actor.getPhoto());
        actorProfile.setExtraData(actor.getExtraData());
        actorProfile.setLocation(NetworkNodeCommunicationDeviceLocation.getInstance(actor.getLatitude(), actor.getLongitude()));

        return actorProfile;
    }

}
