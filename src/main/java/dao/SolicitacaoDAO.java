package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bd.Conexao;
import model.Solicitacoes;
import model.Usuario;

public class SolicitacaoDAO {

	private static final String SELECT = "select s.*, u.nome as usuario_nome, c.nome as categoria_nome, "
			+ "t.nome as tecnico_nome from solicitacoes s "
			+ "join usuarios u on u.id = s.usuario_id "
			+ "join categorias c on c.id = s.categoria_id "
			+ "left join usuarios t on t.id = s.tecnico_id ";

	public void abrir(Solicitacoes s) throws SQLException {
		String sql = "insert into solicitacoes (titulo, descricao, local_ocorrencia, prioridade, status, "
				+ "data_abertura, usuario_id, categoria_id) values (?, ?, ?, ?, 'ABERTA', ?, ?, ?)";

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setString(1, s.getTitulo());
			comando.setString(2, s.getDescricao());
			comando.setString(3, s.getLocal_ocorrencia());
			comando.setString(4, s.getPrioridade());
			comando.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			comando.setInt(6, s.getUsuario_id());
			comando.setInt(7, s.getCategoria_id());
			comando.executeUpdate();
		}
	}

	public List<Solicitacoes> listar(Usuario usu) throws SQLException {
		String filtro = switch (usu.getPerfil()) {
			case "SOLICITANTE" -> "where s.usuario_id = ? ";
			case "TECNICO" -> "where s.status = 'ABERTA' or s.tecnico_id = ? ";
			default -> "";
		};
		List<Solicitacoes> lista = new ArrayList<>();

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(
					SELECT + filtro + "order by s.data_abertura desc, s.id desc")) {

			if (!filtro.isEmpty()) {
				comando.setInt(1, usu.getId());
			}
			try (ResultSet resultado = comando.executeQuery()) {
				while (resultado.next()) {
					lista.add(ler(resultado));
				}
			}
		}
		return lista;
	}

	public Solicitacoes buscar(int id) throws SQLException {
		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(SELECT + "where s.id = ?")) {

			comando.setInt(1, id);
			try (ResultSet resultado = comando.executeQuery()) {
				return resultado.next() ? ler(resultado) : null;
			}
		}
	}

	public boolean assumir(int id, int tecnicoId) throws SQLException {
		String sql = "update solicitacoes set tecnico_id = ?, status = 'EM_ANDAMENTO' "
				+ "where id = ? and status = 'ABERTA'";

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setInt(1, tecnicoId);
			comando.setInt(2, id);
			return comando.executeUpdate() == 1;
		}
	}

	public boolean finalizar(int id) throws SQLException {
		String sql = "update solicitacoes set status = 'FINALIZADA', data_fechamento = ? "
				+ "where id = ? and status <> 'FINALIZADA'";

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			comando.setInt(2, id);
			return comando.executeUpdate() == 1;
		}
	}

	private Solicitacoes ler(ResultSet r) throws SQLException {
		Solicitacoes s = new Solicitacoes();
		s.setId(r.getInt("id"));
		s.setTitulo(r.getString("titulo"));
		s.setDescricao(r.getString("descricao"));
		s.setLocal_ocorrencia(r.getString("local_ocorrencia"));
		s.setPrioridade(r.getString("prioridade"));
		s.setStatus(r.getString("status"));
		s.setData_abertura(r.getTimestamp("data_abertura"));
		s.setData_fechamento(r.getTimestamp("data_fechamento"));
		s.setUsuario_id(r.getInt("usuario_id"));
		s.setTecnico_id(r.getInt("tecnico_id"));
		s.setCategoria_id(r.getInt("categoria_id"));
		s.setUsuario_nome(r.getString("usuario_nome"));
		s.setCategoria_nome(r.getString("categoria_nome"));
		s.setTecnico_nome(r.getString("tecnico_nome"));
		return s;
	}

}
