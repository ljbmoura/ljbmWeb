package br.com.ljbm.fp.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JsonProvider extends JacksonJsonProvider {

	public JsonProvider() {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new Hibernate5Module());

		// objectMapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
		objectMapper.enable(
				SerializationFeature.FAIL_ON_EMPTY_BEANS
				, SerializationFeature.WRAP_ROOT_VALUE
				, SerializationFeature.WRAP_EXCEPTIONS
				, SerializationFeature.FAIL_ON_SELF_REFERENCES);
		
		objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

		setMapper(objectMapper);
	}
}