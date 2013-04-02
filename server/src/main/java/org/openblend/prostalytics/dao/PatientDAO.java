package org.openblend.prostalytics.dao;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.openblend.prostalytics.domain.Patient;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@Path("/patient")
public interface PatientDAO {

	@POST
	@Path("/")
	public long savePatient(Patient patient);

    @GET
    @Path("/{id}")
    public Patient loadPatient(@PathParam("id") long id);
    
    @DELETE
    @Path("/{id}")
    public void deletePatient(@PathParam("id") long id);

    @GET
    @Path("/find")
    public List<Patient> findPatients(@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("externalId") String externalId);
}
