package br.com.ljbm.fp.controller;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.junit.Ignore;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.recursos.SuporteAplicacao;

//@RunWith(Arquillian.class)
public class FundoInvestimentoRegistrationTest {
//	@Deployment
//	public static Archive<?> createTestArchive() {
//		return ShrinkWrap
//				.create(WebArchive.class, "test.war")
//				.addClasses(FundoInvestimento.class,
//						FundoInvestimentoRegistration.class, SuporteAplicacao.class)
//				.addAsResource("META-INF/persistence.xml",
//						"META-INF/persistence.xml")
//				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//	}

	@Inject
	FundoInvestimentoRegistration fundoInvestimentoRegistration;

	@Inject
	Logger log;

	@Test
	@Ignore
	public void testRegister() throws Exception {
		FundoInvestimento newFundoInvestimento = fundoInvestimentoRegistration
				.getNewFundoInvestimento();
		newFundoInvestimento.setNome("BB Ações Const Civil");
		newFundoInvestimento.setCNPJ("096480500001-54");
		newFundoInvestimento.setTaxaImpostoRenda(new BigDecimal("0.15"));
		fundoInvestimentoRegistration.register();
		assertNotNull(newFundoInvestimento.getId());
		log.info(newFundoInvestimento.getNome() + " was persisted with id "
				+ newFundoInvestimento.getId());
	}

}
