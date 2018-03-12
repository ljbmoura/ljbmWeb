package br.com.ljbm.fp.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ljbm.fp.modelo.ComparacaoInvestimentoVersusSELIC;
import br.com.ljbm.fp.servico.AvaliadorInvestimentoRemote;

/**
 * the class that allows us to create new FundoInvestimento from the JSF page
 * 
 * @author luc
 * @since 28/03/2012
 * 
 */
@Stateful
// This bean requires transactions as it needs to write to the database. Making
// this an EJB gives us access to declarative transactions - much simpler than
// manual transaction control.
// The @Stateful annotation eliminates the need for manual transaction
// demarcation
@Model
// Stereotypes, such as @Model allow grouping of common functionality. Here we
// use the built in @Model stereotype to give us a request scoped, named bean.
// The @Model stereotype is a convenience mechanism to make this a
// request-scoped bean that has an EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
public class AvaliadorInvestimentoManagedBean {

	// Seam Solder is a swiss army knife for any CDI based application. It
	// offers some basic additions to the CDI programming model (such as an
	// injectable, type-safe, logger) as well as utilities for developing CDI
	// extensions. You can read more on the Solder project page.
	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private AvaliadorInvestimentoRemote avaliadorInvestimento;

	private List<ComparacaoInvestimentoVersusSELIC> avaliacao;

	@Produces
	@Named
	public List<ComparacaoInvestimentoVersusSELIC> getAvaliacao() {
		return avaliacao;
	}
	
	public String posicaoInvestimentosFrenteSELIC() throws Exception {

		log.info("executando posicaoInvestimentosFrenteSELIC");
		
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO,
				"Posição atual em", "Posicao Investimentos BB Frente a SELIC"));
		// TODO: PARA DAR ERRO MESMO, IMPLEMENTAR A OBTENÇÃO DA DATA POSTERIORMENTE
		avaliacao = avaliadorInvestimento.comparaInvestimentosComSELIC("");
		return "labEJB";
	}

//	@PostConstruct
//	public void initNewFundoInvestimento() {
//		newFundoInvestimento = new FundoInvestimento();
//	}
}
