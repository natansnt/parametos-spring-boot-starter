package br.com.natandev.parametros;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.natandev.parametros.managers.ParametroDAO;
import jakarta.persistence.EntityManager;

@Configuration
@EnableAutoConfiguration
class ParametroDaoConfig {
	@Bean ParametroDAO dao(EntityManager entityManager) {
		return new ParametroDAO(new ParametrosProperties(), entityManager);
	}
}
