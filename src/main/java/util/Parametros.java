package util;

public class Parametros {

	public static int idDe(String texto) {
		try {
			return Integer.parseInt(texto);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

}
