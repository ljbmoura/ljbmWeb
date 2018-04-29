package br.com.ljbm.fp.rest;

import static br.com.ljbm.fp.modelo.Corretora.cnpjAgora;
import static br.com.ljbm.fp.modelo.Corretora.cnpjBB;
import static com.jayway.restassured.RestAssured.basePath;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import br.com.ljbm.fp.modelo.Corretora;
import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.modelo.TipoFundoInvestimento;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FundoInvestimentoResourceRESTServiceTest {

	private static Corretora BB;
	private static Corretora AGORA;
	private static FundoInvestimento Agora_NTNB_2035;

	private static Logger log;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log = LogManager.getFormatterLogger(FundoInvestimentoResourceRESTServiceTest.class);
		BB = new Corretora();
		BB.setIde(1l);
		AGORA = new Corretora();
		AGORA.setIde(2l);
		Agora_NTNB_2035 = new FundoInvestimento(cnpjAgora, "Agora NTNB-2035", new BigDecimal("0.15"),
				TipoFundoInvestimento.TesouroDireto, AGORA);

		basePath = "http://localhost:9080/";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test01InclusaoFundosInvestimento() {

		Stream

				.of(Agora_NTNB_2035)

				.forEach(fundo -> {
					Response response = enviaPost(fundo);
					log.debug(response.body().asString());

					Assert.assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());
					String location = response.getHeader("Location");
					log.debug(location);

					JsonPath retorno = enviaGet(location);
					retorno.prettyPrint();
					FundoInvestimento resourceCriado = retorno.getObject("", FundoInvestimento.class);
					log.info(resourceCriado);
					assertThat(resourceCriado.getIde(), notNullValue());
					assertThat(resourceCriado.getNome(), org.hamcrest.Matchers.equalTo(fundo.getNome()));
					assertThat(resourceCriado.getCNPJ(), org.hamcrest.Matchers.equalTo(fundo.getCNPJ()));
					assertThat(resourceCriado.getTipoFundoInvestimento(),
							org.hamcrest.Matchers.equalTo(fundo.getTipoFundoInvestimento()));
					assertThat(resourceCriado.getTaxaImpostoRenda(),
							org.hamcrest.Matchers.equalTo(fundo.getTaxaImpostoRenda()));
				});
	}

	@Test
	@Ignore
	public void test02ExcluirFundosInvestimento() {
		Stream<FundoInvestimento> fundosParaCriar = Stream.of(
				new FundoInvestimento(cnpjAgora, "Agora Prefixado 2019", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, AGORA),
				new FundoInvestimento(cnpjBB, "BB Prefixado 2023", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, AGORA),
				new FundoInvestimento(cnpjBB, "BB NTNB-2024", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, AGORA),
				Agora_NTNB_2035);
	}

	//FIXME uso de basePath não funcionando
	private static Response enviaPost(FundoInvestimento fundoInvestimento) {
		return given().header("Accept", "application/json").contentType("application/json").body(fundoInvestimento)
				.when().post(basePath + "ljbmWeb/rest/fundosInvestimento").andReturn();
	}

	private static JsonPath enviaGet(String location) {
		return given().header("Accept", "application/json").when().get(location).andReturn().jsonPath();
	}
}
