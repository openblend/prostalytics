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

    /*
     * curl -XPOST -d "{\"code\":\"39458\",\"name\":\"John\",\"surname\":\"Doe\",\"externalId\":\"123\",\"birthDate\":\"1990-08-01T23:14:00.000Z\"}" -H "Content-Type: application/json" -H "Cookie: JSESSIONID=g0iWpZ4uZY2LE+68S8icQFe7" -H "Accept: application/json" http://localhost:8080/prostalytics/rest/patient
     */
	@POST
	@Path("/")
	public String savePatient(Patient patient);

    /*
     * curl -H "Cookie: JSESSIONID=g0iWpZ4uZY2LE+68S8icQFe7" -H "Accept: application/json"  http://localhost:8080/prostalytics/rest/patient/1
     */
    @GET
    @Path("/{id}")
    public Patient loadPatient(@PathParam("id") String id);

    /*
    * curl -XDELETE -H "Cookie: JSESSIONID=g0iWpZ4uZY2LE+68S8icQFe7" http://localhost:8080/prostalytics/rest/patient/1
    */
    @DELETE
    @Path("/{id}")
    public void deletePatient(@PathParam("id") String id);

    /*
     * curl -H "Cookie: JSESSIONID=g0iWpZ4uZY2LE+68S8icQFe7" -H "Accept: application/json" "http://localhost:8080/prostalytics/rest/patient/find?name=John&surname=Doe"
     */
    @GET
    @Path("/find")
    public List<Patient> findPatients(@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("externalId") String externalId);
}
