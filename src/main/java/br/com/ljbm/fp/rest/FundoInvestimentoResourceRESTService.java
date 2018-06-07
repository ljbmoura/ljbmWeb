package br.com.ljbm.fp.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;

import br.com.ljbm.fp.interceptador.HttpInterceptor;
import br.com.ljbm.fp.interceptador.Logged;
import br.com.ljbm.fp.modelo.Aplicacao;
import br.com.ljbm.fp.modelo.ComparacaoInvestimentoVersusSELIC;
import br.com.ljbm.fp.modelo.Corretora;
import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.servico.AvaliadorInvestimento;
import br.com.ljbm.fp.servico.FPDominio;
import br.com.ljbm.fp.servico.FPException;

/**
 * Produces a RESTful service to read the contents of the FundoInvestimento
 * table.
 * 
 * @author luc
 * @since 28/03/2012
 * 
 */
// @Path annotation tells JAX-RS that this class provides a REST endpoint mapped
// to rest/fundosInvestimento (concatenating the path from the activator with
// the path for this endpoint
@Path("/fundosInvestimento")
@RequestScoped
@Interceptors(value={HttpInterceptor.class})
public class FundoInvestimentoResourceRESTService {


	public static final String FUNDOS_INVESTIMENTO_RESOURCE_BASE = "/fundosInvestimento";
	
	@EJB (lookup="java:global/ljbmEAR/ljbmEJB/AvaliadorInvestimentoImpl!br.com.ljbm.fp.servico.AvaliadorInvestimento")
	private AvaliadorInvestimento avaliadorInvestimento;

	@EJB(name = "ejb/FPDominio") // usando <ejb-ref> no web.xml
	FPDominio model;
	
	@Inject
	private Logger log;
	

	public FundoInvestimentoResourceRESTService() {
//		java.util.logging.Logger restLogger = java.util.logging.LogManager.getLogManager()
//				.getLogger("br.com.ljbm.fp.rest.ResourceRESTServices");
//		ResourceConfig config = new ResourceConfig(FundoInvestimentoResourceRESTService.class);
//		config.register(new LoggingFeature(restLogger , LoggingFeature.Verbosity.PAYLOAD_ANY));
//		config.register(LoggingFeature.class);
	}

	@GET
	@Path("/{ide:[0-9][0-9]*}")
	@Produces(value= {APPLICATION_JSON, APPLICATION_XML})
	public Response lookupFundoInvestimentoById(
			@PathParam("ide") Long ide) throws FPException {
		try {
			FundoInvestimento fundoInvestimento = model.getFundoInvestimento(ide);
			CacheControl cc = new CacheControl();
			cc.setNoCache(true);
			return Response.ok().entity(fundoInvestimento).cacheControl(cc).build();
		} catch (FPException e) {
			String msg = e.getLocalizedMessage();
			log.warn(msg);
			return Response.status(HttpStatus.SC_NOT_FOUND).entity(msg).build();
		}
	}

	@GET
	@Produces(value= {APPLICATION_JSON, APPLICATION_XML})
	public Response listFundosInvestimento(
			@QueryParam("agente") String agente, @QueryParam("titulo") String titulo) throws FPException {
		GenericEntity<List<FundoInvestimento>> ents;
		List<FundoInvestimento> results;
		log.info(agente);
		if (agente == null || titulo == null) {
			results = model.retrieveFundosInvestimentoOrderedByName();
		} else {
			results = model.getFundoInvestimentoByAgenteCustodiaETitulo(agente, titulo);
		}
		if (results.isEmpty()) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Nenhum Fundo de investimento encontrado").build();
		}
		ents = new GenericEntity<List<FundoInvestimento>>(results) {};
		CacheControl cc = new CacheControl();
		cc.setNoCache(true);
		return Response.ok().entity(ents).cacheControl(cc).build();
	}

	@POST
	@Consumes(value= {APPLICATION_JSON, APPLICATION_XML})
	public Response inclui(FundoInvestimento fundoInvestimento) {

		try {
			Corretora c = model.getCorretora(fundoInvestimento.getCorretora().getIde());
			fundoInvestimento.setCorretora(c);
			FundoInvestimento ret = model.addFundoInvestimento(fundoInvestimento);
			log.debug(String.format("fundo de investimento %d criado.", ret.getIde()));
			URI uri = URI.create("/fundosInvestimento/" + ret.getIde());
			return Response.created(uri).build();
		} catch (FPException e) {
			log.error(e.getLocalizedMessage());
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	
	
	@Path("/{ide:[0-9][0-9]*}")
	@DELETE
	public Response excluiFundoInvestimentoById(
			@PathParam("ide") Long ide) throws FPException {
		try {
			FundoInvestimento fundoInvestimento = model.getFundoInvestimento(ide);
			model.deleteFundoInvestimento(fundoInvestimento);
			return Response.noContent().build();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@POST
	@Logged
	@Path("/{ide:[0-9][0-9]*}/aplicacoes")
	@Consumes(value= {APPLICATION_JSON, APPLICATION_XML})
	public Response inclui(@PathParam("ide") Long ide, Aplicacao aplicacao) {

		log.info("aplicacoes de " + ide );
		try {
			FundoInvestimento fundoInvestimento = model.getFundoInvestimento(ide);
			aplicacao.setFundoInvestimento(fundoInvestimento);
			Aplicacao ret = model.addAplicacao(aplicacao);
			log.debug(String.format("aplicação %d criada.", ret.getIde()));
			URI uri = URI.create("/fundosInvestimento/" + ide + "/aplicacao/" + ret.getIde());
			return Response.created(uri).build();
		} catch (FPException e) {
			log.error(e.getLocalizedMessage());
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Logged
	@Path("/aplicacoes/{ide:[0-9][0-9]*}")
	@Produces(value= {APPLICATION_JSON, APPLICATION_XML})
	public Response lookupAplicacaoById(
			@PathParam("ide") Long ide) throws FPException {
		try {
			Aplicacao aplicacao = model.getAplicacao(ide);
			CacheControl cc = new CacheControl();
			cc.setNoCache(true);
			return Response.ok().entity(aplicacao).cacheControl(cc).build();
		} catch (FPException e) {
			String msg = e.getLocalizedMessage();
			log.warn(msg);
			return Response.status(HttpStatus.SC_NOT_FOUND).entity(msg).build();
		}
	}
	
//	@GET
//	@Produces(value= {APPLICATION_JSON, APPLICATION_XML})
//	// The listAllFundosInvestimento() method is called when the raw endpoint is
//	// accessed (for example rest/fundosInvestimento) and offers up a list of
//	// endpoints. Notice that the object is automatically mapped to XML by JAXB
//	public List<FundoInvestimento> listAllFundosInvestimento() throws Exception {
//		final List<FundoInvestimento> results = model.retrieveFundosInvestimentoOrderedByName();
//		return results;
//	}

	@GET
	@Path("/posicaoInvestimentosFrenteSELIC")
	@Produces("text/plain")
	public List<ComparacaoInvestimentoVersusSELIC> posicaoInvestimentosFrenteSELIC() {

		// TODO: PARA DAR ERRO MESMO, IMPLEMENTAR A OBTENÇÃO DA DATA POSTERIORMENTE
//		return avaliadorInvestimento.comparaInvestimentosComSELIC("");
		return null;
	}

//	@GET
//	@Path("/atualizaBaseInvestimentos_CotacaoSELIC")
//	@Produces("text/plain")
//	public String atualizaBaseInvestimentos_CotacaoSELIC() {
//
//		// TODO: PARA DAR ERRO MESMO, IMPLEMENTAR A OBTENÇÃO DA DATA POSTERIORMENTE
//		avaliadorInvestimento.atualizaBaseInvestimentos_CotacaoSELIC("", "");
//		return "ok";
//
//	}
}
