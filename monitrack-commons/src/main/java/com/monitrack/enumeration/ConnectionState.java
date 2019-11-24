package com.monitrack.enumeration;

public enum ConnectionState {

	FAIL (0,"The connection failed !", "La connexion a �chou� !"),
	NO_CONNECTION(1, "There is no connection left ! Please try later.", "Absence de connexion disponible ! Veuillez r�essayer plus tard."),
	TRY(2, "Trying the connection, please wait...", "Connexion au serveur en cours, veuillez patienter..."),
	SUCCESS(3,"The connection succeeded", "Connexion au serveur r�ussie"),	
	RESERVED_CONNECTION(4, "The connection has been reserved", "La connexion a �t� r�serv�e"),
	DEPRECATED_VERSION(5,"The version of the application you are using is deprecated", "Vous utilisez une version obsel�te de l'application");
	
	private Integer code;
	private String englishLabel;
	private String frenchLabel;
	
	ConnectionState(Integer code, String englishLabel, String frenchLabel) {
		this.code=code;
		this.englishLabel=englishLabel;
		this.frenchLabel=frenchLabel;
	}

	public Integer getCode() {
		return code;
	}

	/**
	 * @return the englishLabel
	 */
	public String getEnglishLabel() {
		return englishLabel;
	}

	/**
	 * @return the frenchLabel
	 */
	public String getFrenchLabel() {
		return frenchLabel;
	}
	
	
	
}
