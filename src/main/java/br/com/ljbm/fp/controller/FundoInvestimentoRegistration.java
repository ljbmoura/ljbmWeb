package br.com.ljbm.fp.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.modelo.TipoFundoInvestimento;
import br.com.ljbm.recursos.FinancasPessoaisDelegate;


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
public class FundoInvestimentoRegistration {

	// Seam Solder is a swiss army knife for any CDI based application. It
	// offers some basic additions to the CDI programming model (such as an
	// injectable, type-safe, logger) as well as utilities for developing CDI
	// extensions. You can read more on the Solder project page.
	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private FinancasPessoaisDelegate financasPessoaisDelegate;

	// @Inject
	// private EntityManager em;

	@Inject
	private Event<FundoInvestimento> fundoInvestimentoEventSrc;

	private FundoInvestimento newFundoInvestimento;

	@Produces
	@Named
	public FundoInvestimento getNewFundoInvestimento() {
		return newFundoInvestimento;
	}
	

	public String register() throws Exception {
		// newFundoInvestimento.setId(3l);
		// newFundoInvestimento.setVersion(0);

		newFundoInvestimento.setTipoFundoInvestimento(TipoFundoInvestimento.TesouroDireto);
		log.info("Incluindo na BD " + newFundoInvestimento.toString());
		financasPessoaisDelegate.incluiFundoInvestimento(
				newFundoInvestimento);
		// em.persist(newFundoInvestimento);
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO,
				"Fundo de Investimento Cadastrado!", "Cadastro efetivado"));
		// An event is sent every time a member is updated. This allows other
		// pieces of code (in this example the member list is refreshed) to
		// react to changes in the member list without any coupling to this
		// class
		fundoInvestimentoEventSrc.fire(newFundoInvestimento);
		initNewFundoInvestimento();
		return "labEJB";
	}

	@PostConstruct
	public void initNewFundoInvestimento() {
		newFundoInvestimento = new FundoInvestimento();
	}
}
