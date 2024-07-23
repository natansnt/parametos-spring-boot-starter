package br.com.natandev.parametros.managers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import br.com.natandev.parametros.Parametro;
import br.com.natandev.parametros.ParametrosProperties;

public class ParametroManager {

	private final String prefixo;
	private final ParametroDAO dao;
	
	public ParametroManager(final ParametrosProperties props, final ParametroDAO dao) {
		this.dao = requireNonNull(dao);
		this.prefixo = requireNonNull(props).getAgenciaVirtual().prefixo();
	}

	public Parametro<String> buscar(String secao, String nome) {
		return this.buscar(secao, nome, Function.identity());
	}
	
	@Cacheable
	public <R> Parametro<R> buscar(final String secao, final String nome, final Function<String, R> parser) {
		return buscar(Parametro.with(secao, nome), parser, false);
	}
	
	@Cacheable
	public <R> Parametro<R> buscar(final Parametro<R> parametro, final Function<String, R> parser) {
		return buscar(parametro, parser, false);
	}

	public <R> Parametro<R> buscarParaAtualizar(final Parametro<R> parametro, final Function<String, R> parser) {
		return buscar(parametro, parser, true);
	}
	
	@SuppressWarnings("preview")
	private <R> Parametro<R> buscar(final Parametro<R> parametro, final Function<String, R> parser, boolean withLock) {
		
		requireNonNull(parser, "O conversor deve ser fornecido.");
		requireNonNull(parametro, "A seção e o nome do parâmetro devem ser fornecidos.");
		
		final var nomeNormalized = parametro.getNome();
		final var nomeWithPrefix = STR."\{prefixo}_\{nomeNormalized}";

		var resultWithPrefix = dao.buscar(parametro.getSecao(), nomeWithPrefix, withLock)
				.flatMap(value -> Optional.of(parser.apply(value)));
		
		if (resultWithPrefix.isPresent()) {
			return Parametro.with(parametro.getSecao(), nomeWithPrefix).withValor(resultWithPrefix);
		}
		
		return dao.buscar(parametro, withLock).convert(parser);
	}
	
	@CacheEvict
	public void atualizar(final Parametro<String> parametro) {
		atualizar(parametro, Function.identity());
	}
	
	@CacheEvict
	public <R> void atualizar(final Parametro<R> parametro, final Function<R, String> parser) {
		requireNonNull(parser, "O conversor deve ser fornecido.");
		requireNonNull(parametro, "O parâmetro deve ser fornecido.");
		
		dao.atualizar(parametro.convert(parser));
	}
	
}
