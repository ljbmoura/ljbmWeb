package br.com.ljbm.fp.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;

import br.com.ljbm.fp.modelo.ComparacaoInvestimentoVersusSELIC;
import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.servico.AvaliadorInvestimento;
import br.com.ljbm.fp.servico.FPException;
import br.com.ljbm.recursos.FinancasPessoaisDelegate;

/**
 * Produces a RESTful service to read the contents of the FundoInvestimento
 * table.
 * 
 * @author luc
 * @since 28/03/2012
 * 
 */
@Path("/fundosInvestimento")
// @Path annotation tells JAX-RS that this class provides a REST endpoint mapped
// to rest/fundosInvestimento (concatenating the path from the activator with
// the path for this endpoint
@RequestScoped
public class FundoInvestimentoResourceRESTService {

	@EJB (lookup="java:global/ljbmEAR/ljbmEJB/AvaliadorInvestimentoImpl!br.com.ljbm.fp.servico.AvaliadorInvestimento")
	private AvaliadorInvestimento avaliadorInvestimento;

	@Inject
	private FinancasPessoaisDelegate financasPessoaisDelegate;
	
	@Inject
	private Logger log;

	@POST
	@Consumes(APPLICATION_JSON)
	public Response inclui(FundoInvestimento fundoInvestimento) {

		try {
			FundoInvestimento ret = financasPessoaisDelegate.incluiFundoInvestimento(fundoInvestimento);
			log.debug(String.format("fundo de investimento %d criado.", ret.getIde()));
	        URI uri = URI.create("/fundosInvestimento/" + ret.getIde());
	        return Response.created(uri).build();
		} catch (FPException e) {
			log.error(e.getLocalizedMessage());
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces({APPLICATION_JSON,"text/xml"})
	// The lookupMemberById() method is called when the endpoint is accessed
	// with a member id parameter appended (for example
	// rest/fundosInvestimento/1). Again, the object is automatically mapped to
	// XML by JAXB
	public Response lookupFundoInvestimentoById(
			@PathParam("id") long id) throws Exception {
		FundoInvestimento fundoInvestimento = financasPessoaisDelegate
				.recuperaFundosInvestimentoPorIde(id);
		return Response.ok().entity(fundoInvestimento).build();
	}
	
	@GET
	@Produces("text/xml")
	// The listAllFundosInvestimento() method is called when the raw endpoint is
	// accessed (for example rest/fundosInvestimento) and offers up a list of
	// endpoints. Notice that the object is automatically mapped to XML by JAXB
	public List<FundoInvestimento> listAllFundosInvestimento() throws Exception {
		final List<FundoInvestimento> results = financasPessoaisDelegate.recuperaFundosInvestimentoPorName();
		return results;
	}

	@GET
	@Path("/posicaoInvestimentosFrenteSELIC")
	@Produces("text/plain")
	public List<ComparacaoInvestimentoVersusSELIC> posicaoInvestimentosFrenteSELIC() {

		// TODO: PARA DAR ERRO MESMO, IMPLEMENTAR A OBTENÇÃO DA DATA POSTERIORMENTE
		return avaliadorInvestimento.comparaInvestimentosComSELIC("");

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
