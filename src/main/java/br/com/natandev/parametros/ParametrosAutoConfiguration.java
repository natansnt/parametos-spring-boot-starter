package br.com.natandev.parametros;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import br.com.natandev.parametros.managers.ParametroDAO;
import br.com.natandev.parametros.managers.ParametroManager;
import jakarta.persistence.EntityManager;

@AutoConfiguration
@EnableConfigurationProperties(ParametrosProperties.class)
public class ParametrosAutoConfiguration {

	@Bean
	ParametroManager parametroManager(EntityManager manager, ParametrosProperties properties) {
		return new ParametroManager(properties, new ParametroDAO(properties, manager));
	}
	
}
