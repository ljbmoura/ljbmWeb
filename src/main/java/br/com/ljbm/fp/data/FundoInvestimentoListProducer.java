package br.com.ljbm.fp.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.servico.FPException;
import br.com.ljbm.recursos.FinancasPessoaisDelegate;

/**
 * Responsible for building the list of FundoInvestimento, ordered by name.
 * 
 * @author luc
 * @since 27/03/2012
 * 
 */
@RequestScoped
// This bean is request scoped, meaning that any fields (ex: fundosInvestimento)
// will be stored for the entire request
public class FundoInvestimentoListProducer {
	// @Inject
	// private EntityManager em;
	@Inject
	private FinancasPessoaisDelegate financasPessoaisDelegate;

	private List<FundoInvestimento> fundosInvestimento;

	@Produces
	// The list of fundoInvestimento is exposed as a producer method
	@Named
	// @Named provides access the return value via the EL variable name
	// "fundosInvestimento" in the UI (e.g., Facelets or JSP view)
	public List<FundoInvestimento> getFundosInvestimento() {
		return fundosInvestimento;
	}

	// The observer method is notified whenever a FundoInvestimento is created,
	// removed, or updated. This allows us to refresh the list of members
	// whenever they are needed. This is a good approach as it allows us to
	// cache the list of members, but keep it up to date at the same time
	public void onMemberListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final FundoInvestimento fundoInvestimento) throws FPException {
		retrieveAllFundosInvestimentoOrderedByName();
	}

	@PostConstruct
	public void retrieveAllFundosInvestimentoOrderedByName() {
		// TODO: estudar as opções abaixo, ou seja:
		// modo injeção de dependência
		// fundosInvestimento = model.retrieveFundosInvestimentoOrderedByName();

		// ou

		// modo padrão delegate + singleton, instanciando as classes normalmente
		fundosInvestimento = financasPessoaisDelegate
				.recuperaFundosInvestimentoPorName();
		// try {
		// FinancasPessoaisDelegate fpd =
		// FinancasPessoaisDelegate.getInstance();
		// fundosInvestimento = fpd
		// .recuperaFundosInvestimentoPorName();
		// } catch (NamingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
