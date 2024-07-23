package br.com.natandev.parametros.test.util;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.natandev.parametros.managers.ParametroManager;
import br.com.natandev.parametros.util.ParametroSupplier;

public final class Parametros {
	
	private static final Logger log = LoggerFactory.getLogger(Parametros.class);
	
	private final ParametroManager manager;
	private final Local local;
	
	public Parametros(ParametroManager manager) {
		this.manager = manager;
		this.local = new Local();
	}
	
	public Local local() {
		return local;
	}

	ParametroSupplier<String> supplier(final String secao, final String nome) {
		return supplier(secao, nome, Function.identity(), null);
	}
	
	ParametroSupplier<String> supplier(final String secao, final String nome, Supplier<String> valorDefault) {
		return supplier(secao, nome, Function.identity(), valorDefault);
	}
	
	<R> ParametroSupplier<R> supplier(final String secao, final String nome, final Function<String, R> parser) {
		return supplier(secao, nome, parser, null);
	}
	
	<R> ParametroSupplier<R> supplier(final String secao, final String nome, final Function<String, R> parser,
			Supplier<R> valorDefault) {
		return () -> {
			var param = manager.buscar(secao, nome, parser);
			
			if (!param.exists() && valorDefault != null) {
				param = param.withValor(valorDefault.get());
				if (log.isDebugEnabled()) {
					log.debug("Parâmetro {}[{}] não encontrado, retornando valor default.", param.getSecao(),
							param.getNome());
				}
			}
			
			return param; 
		};
	}
	
	public static class Secoes {
		
		public static final String LOCAL = "LOCAL";
		
		private Secoes() {}
	}
	
	/* Parâmetros por Seção */
	
	public class Local {

		/* Declarações */
		public final ParametroSupplier<LocalDate> dataReferencia;
		
		/* Atribuições */
		private Local() {
			dataReferencia = supplier(Secoes.LOCAL, "DATA_REFERENCIA", LocalDate::parse);
		}
	}
	
}
