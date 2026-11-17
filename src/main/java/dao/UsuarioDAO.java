package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bd.Conexao;
import model.Usuario;

public class UsuarioDAO {

	public Usuario logar(String email, String senha) throws SQLException {

		String sql = "select id, nome, email, perfil from usuarios " +
		   " where email = ? and senha = ? and ativo = true";

		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement logar = conexao.prepareStatement(sql)){

				logar.setString(1, email);
				logar.setString(2, senha);

				try (ResultSet resultado = logar.executeQuery()){
					if (resultado.next()) {
						Usuario usu = new Usuario();
						usu.setId(resultado.getInt("id"));
						usu.setNome(resultado.getString("nome"));
						usu.setEmail(resultado.getString("email"));
						usu.setPerfil(resultado.getString("perfil"));

						return usu;
					}
				}

		}
		return null;
	}

	private static final String COLUNAS = "select id, nome, email, perfil, ativo, data_cadastro from usuarios ";

	public List<Usuario> listar() throws SQLException {
		List<Usuario> lista = new ArrayList<>();

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(COLUNAS + "order by nome");
			ResultSet resultado = comando.executeQuery()) {

			while (resultado.next()) {
				lista.add(ler(resultado));
			}
		}
		return lista;
	}

	public Usuario buscar(int id) throws SQLException {
		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(COLUNAS + "where id = ?")) {

			comando.setInt(1, id);
			try (ResultSet resultado = comando.executeQuery()) {
				return resultado.next() ? ler(resultado) : null;
			}
		}
	}

	public void salvar(Usuario u) throws SQLException {
		String sql = "insert into usuarios (nome, email, senha, perfil, ativo, data_cadastro) "
				+ "values (?, ?, ?, ?, true, ?)";

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setString(1, u.getNome());
			comando.setString(2, u.getEmail());
			comando.setString(3, u.getSenha());
			comando.setString(4, u.getPerfil());
			comando.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			comando.executeUpdate();
		}
	}

	public void alterar(Usuario u) throws SQLException {
		boolean trocaSenha = u.getSenha() != null && !u.getSenha().isBlank();
		String sql = "update usuarios set nome = ?, email = ?, perfil = ?"
				+ (trocaSenha ? ", senha = ?" : "") + " where id = ?";

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setString(1, u.getNome());
			comando.setString(2, u.getEmail());
			comando.setString(3, u.getPerfil());
			int proximo = 4;
			if (trocaSenha) {
				comando.setString(proximo++, u.getSenha());
			}
			comando.setInt(proximo, u.getId());
			comando.executeUpdate();
		}
	}

	public void definirAtivo(int id, boolean ativo) throws SQLException {
		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement("update usuarios set ativo = ? where id = ?")) {

			comando.setBoolean(1, ativo);
			comando.setInt(2, id);
			comando.executeUpdate();
		}
	}

	private Usuario ler(ResultSet r) throws SQLException {
		Usuario u = new Usuario();
		u.setId(r.getInt("id"));
		u.setNome(r.getString("nome"));
		u.setEmail(r.getString("email"));
		u.setPerfil(r.getString("perfil"));
		u.setAtivo(r.getBoolean("ativo"));
		u.setData_cadastro(r.getTimestamp("data_cadastro"));
		return u;
	}
}
