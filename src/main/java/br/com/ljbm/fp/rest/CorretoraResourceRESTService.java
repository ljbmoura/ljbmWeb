package br.com.ljbm.fp.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.ljbm.fp.modelo.Corretora;
import br.com.ljbm.fp.servico.FPDominio;
import br.com.ljbm.fp.servico.FPException;

/**
 * Produces a RESTful service to read the contents of the Corretora table.
 * 
 * @author luc
 * 
 */
@Path("/corretoras")
@RequestScoped
public class CorretoraResourceRESTService {

	@EJB(name = "ejb/FPDominio") // usando <ejb-ref> no web.xml
	FPDominio model;

//	@Inject
//	private Logger log;

	@GET
	@Path("/{ide:[0-9][0-9]*}")
	@Produces(value = { APPLICATION_JSON, APPLICATION_XML })
	public Response lookupCorretoraById(@PathParam("ide") Long ide) throws FPException {
		try {
			Corretora reg = model.getCorretora(ide);
			return Response.ok().entity(reg).build();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@GET
	@Path("/{ide:[0-9][0-9]*}/fundos")
	@Produces(value = { APPLICATION_JSON, APPLICATION_XML })
	public Response lookupFundosDaCorretoraById(@PathParam("ide") Long ide) throws FPException {
		try {
			Corretora reg = model.getFundosCorretora(ide);
			return Response.ok().entity(reg).build();
//			List<Corretora> reg = model.getFundosCorretora(ide);
//			GenericEntity<List<FundoInvestimento>> ents = new GenericEntity<List<FundoInvestimento>>(reg) {
//			};
//			return Response.ok().entity(ents).build();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
