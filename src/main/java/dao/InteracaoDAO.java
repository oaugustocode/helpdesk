package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bd.Conexao;
import model.Interacoes;

public class InteracaoDAO {

	public List<Interacoes> listarPorSolicitacao(int solicitacaoId) throws SQLException {
		String sql = "select i.*, u.nome as usuario_nome from interacoes i "
				+ "join usuarios u on u.id = i.usuario_id "
				+ "where i.solicitacao_id = ? order by i.data_interacao, i.id";
		List<Interacoes> lista = new ArrayList<>();

		try (
			Connection conexao = Conexao.obterConexao();
			PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setInt(1, solicitacaoId);
			try (ResultSet r = comando.executeQuery()) {
				while (r.next()) {
					Interacoes i = new Interacoes();
					i.setId(r.getInt("id"));
					i.setMensagem(r.getString("mensagem"));
					i.setData_interacao(r.getTimestamp("data_interacao"));
					i.setSolicitacao_id(r.getInt("solicitacao_id"));
					i.setUsuario_id(r.getInt("usuario_id"));
					i.setUsuario_nome(r.getString("usuario_nome"));
					lista.add(i);
				}
			}
		}
		return lista;
	}

	public boolean inserir(Interacoes i, String novoStatus) throws SQLException {
		String sqlStatus = "update solicitacoes set status = ? where id = ? "
				+ "and status in ('EM_ANDAMENTO', 'AGUARDANDO_RESPOSTA')";
		String sqlMensagem = "insert into interacoes (mensagem, data_interacao, solicitacao_id, usuario_id) "
				+ "values (?, ?, ?, ?)";

		try (Connection conexao = Conexao.obterConexao()) {
			conexao.setAutoCommit(false);

			try (
				PreparedStatement status = conexao.prepareStatement(sqlStatus);
				PreparedStatement mensagem = conexao.prepareStatement(sqlMensagem)) {

				status.setString(1, novoStatus);
				status.setInt(2, i.getSolicitacao_id());
				if (status.executeUpdate() == 0) {
					conexao.rollback();
					return false;
				}

				mensagem.setString(1, i.getMensagem());
				mensagem.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				mensagem.setInt(3, i.getSolicitacao_id());
				mensagem.setInt(4, i.getUsuario_id());
				mensagem.executeUpdate();

				conexao.commit();
				return true;
			} catch (SQLException e) {
				conexao.rollback();
				throw e;
			}
		}
	}

}
