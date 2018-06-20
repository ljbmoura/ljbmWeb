package br.com.ljbm.fp.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

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
	static private CacheControl SEM_CACHE;
	static {
		SEM_CACHE = new CacheControl();
		SEM_CACHE.setNoCache(true);
	}
	
	@EJB(name = "ejb/FPDominio") // usando <ejb-ref> no web.xml
	FPDominio model;

	@GET
	@Path("/{ide:[0-9][0-9]*}")
	@Produces(value = {APPLICATION_JSON, APPLICATION_XML})
	public Response buscaCorretoraPorIde(@PathParam("ide") Long ide) throws FPException {
		try {
			Corretora reg = model.getCorretora(ide);
			return Response.ok().entity(reg).cacheControl(SEM_CACHE).build();
		} catch (FPException e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity(e.getLocalizedMessage()).build();
		}		
	}

	@GET
	@Path("/{ide:[0-9][0-9]*}/fundos")
	@Produces(value = { APPLICATION_JSON, APPLICATION_XML })
	public Response buscaFundosDaCorretoraPorIde(@PathParam("ide") Long ide) throws FPException {
		try {
			Corretora reg = model.getFundosCorretora(ide);
			return Response.ok().entity(reg).cacheControl(SEM_CACHE).build();
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
