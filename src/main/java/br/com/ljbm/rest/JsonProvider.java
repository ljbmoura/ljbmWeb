package br.com.ljbm.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JsonProvider extends JacksonJsonProvider {

	public JsonProvider() {
		
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new Hibernate5Module())
				.registerModule(new JavaTimeModule()) 	// new module, NOT JSR310Module
//				   .registerModule(new ParameterNamesModule())
//				   .registerModule(new Jdk8Module())
			; 
//		SimpleDateFormat formatoDataBR = new SimpleDateFormat("dd/MM/yyyy");
//		objectMapper.setDateFormat(formatoDataBR);

		// objectMapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(
				// TODO INVESTIGAR NOTAÇÃO EQUIAVALENTE (@JsonRootName NÃO FUNCIONA)
				 SerializationFeature.WRAP_ROOT_VALUE
				 , SerializationFeature.WRITE_ENUMS_USING_INDEX
				 , SerializationFeature.INDENT_OUTPUT
				 );
//				, SerializationFeature.FAIL_ON_EMPTY_BEANS
//				, SerializationFeature.WRAP_EXCEPTIONS
//				, SerializationFeature.FAIL_ON_SELF_REFERENCES

		objectMapper.enable(
				 DeserializationFeature.UNWRAP_ROOT_VALUE 
				 , DeserializationFeature.READ_ENUMS_USING_TO_STRING
				 );
		
		objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

		setMapper(objectMapper);
	}
}