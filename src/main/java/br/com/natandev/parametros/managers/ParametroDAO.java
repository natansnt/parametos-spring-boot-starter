package br.com.natandev.parametros.managers;

import static java.util.Objects.requireNonNull;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.util.Assert;

import br.com.natandev.parametros.Parametro;
import br.com.natandev.parametros.ParametrosProperties;
import br.com.natandev.parametros.ParametrosProperties.Mapeamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;

public final class ParametroDAO {

	private final Mapeamento mapeamento;
	private final EntityManager entityManager;

	public ParametroDAO(final ParametrosProperties props, final EntityManager entityManager) {
		this.entityManager = requireNonNull(entityManager);
		this.mapeamento = requireNonNull(props).getMapeamento();
	}

	public Parametro<String> buscar(final Parametro<?> parametro, final boolean withLock) {
		requireNonNull(parametro);
		return parametro.withValor(buscar(parametro.getSecao(), parametro.getNome(), withLock));
	}

	public Optional<String> buscar(final String secao, final String nome, final boolean withLock) {
		requireNonNull(secao, "O nome da seção deve ser fornecido.");
		requireNonNull(nome, "O nome do parâmetro deve ser fornecido.");
		
		var query = entityManager
				.createNativeQuery(MessageFormat.format("""
						SELECT {3} FROM {0}
						WHERE {1} = :secao AND {2} = :nome {4}
						""", 
						mapeamento.getTabela(), 
						mapeamento.getColunas().getSecao(),
						mapeamento.getColunas().getNome(), 
						mapeamento.getColunas().getValor(),
						withLock ? "FOR UPDATE" : ""), Tuple.class)
				.setParameter("secao", secao)
				.setParameter("nome", nome);

		try {			
			return Optional.ofNullable(((Tuple) query.getSingleResult()).get(0, String.class));
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
	
	public void atualizar(final Parametro<String> parametro) {
		requireNonNull(parametro);
		Assert.state(
				entityManager
						.createNativeQuery(MessageFormat.format("""
								UPDATE {0} SET {3} = :valor
								WHERE {1} = :secao AND {2} = :nome
								""", 
								mapeamento.getTabela(), 
								mapeamento.getColunas().getSecao(),
								mapeamento.getColunas().getNome(), 
								mapeamento.getColunas().getValor()))
						.setParameter("secao", parametro.getSecao())
						.setParameter("nome", parametro.getNome())
						.setParameter("valor", parametro.getValor().orElse(null))
						.executeUpdate() == 1,
				"Mais de um parâmetro foi atualizado.");
	}

}
