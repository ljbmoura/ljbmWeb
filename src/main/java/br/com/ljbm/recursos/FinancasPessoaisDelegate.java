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

//	private static FinancasPessoaisDelegate instance = new FinancasPessoaisDelegate();

	@EJB
	FPDominio model;
	
//	public static FinancasPessoaisDelegate getInstance() {
//		return instance;
//	}
//
	public List<FundoInvestimento> getAllFundoInvestimento() throws FPException {
		return model.getAllFundoInvestimento();
	}

	public void incluiFundoInvestimento(FundoInvestimento fundoInvestimento) throws FPException {
		model.addFundoInvestimento(fundoInvestimento);
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
