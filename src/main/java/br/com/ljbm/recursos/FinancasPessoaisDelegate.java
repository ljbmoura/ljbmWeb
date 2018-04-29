/*
 * FinancasPessoaisDelegate.java
 *
 * @author luciano
 */
package br.com.ljbm.recursos;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import br.com.ljbm.fp.modelo.Corretora;
import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.servico.FPDominio;
import br.com.ljbm.fp.servico.FPException;

@ApplicationScoped
public class FinancasPessoaisDelegate {

//	@PostConstruct
//	public void init()  {
//		Context context;
//		try {
//			context = new InitialContext();
//			System.out.println("--Looking-up FPDominio... ");
//			Object obj = context.lookup("java:comp/env/ejb/FPDominioImpl"); // usando <ejb-ref> no web.xml
//			System.out.println("--Narrowing FPDominio... ");		
//			model = (FPDominio)obj;
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
		
//	@EJB(name = "ejb/FPDominio") // usando <ejb-ref> no web.xml
	@EJB(lookup = "java:global/ljbmEAR/ljbmEJB/FPDominioImpl!br.com.ljbm.fp.servico.FPDominio")
	FPDominio model;
	
	public List<FundoInvestimento> getAllFundoInvestimento() throws FPException {
		return model.getAllFundoInvestimento();
	}

	public FundoInvestimento incluiFundoInvestimento(FundoInvestimento fundoInvestimento) throws FPException {
		return model.addFundoInvestimento(fundoInvestimento);
	}
	
	public List<FundoInvestimento> recuperaFundosInvestimentoPorName() {
		return model.retrieveFundosInvestimentoOrderedByName();
	}

	public FundoInvestimento recuperaFundosInvestimentoPorIde(Long ide) throws FPException {
		return model.getFundoInvestimento(ide);
	}
	
	public Corretora recuperaCorretoraPorIde(Long ide) throws FPException {
		return model.getCorretora(ide);
	}
}
