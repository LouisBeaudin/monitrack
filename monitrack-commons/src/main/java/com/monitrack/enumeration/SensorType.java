package com.monitrack.enumeration;


public enum SensorType {
	
	//FIXME - Cheikna : Why do not I remove the english label and make a "this.toString().toLowerCase()" in the constructor ?
	SMOKE("Smoke", "Fumée", false),	
	FLOW("Flow", "Présence", false),	
	DOOR("Door", "Porte", false),	
	TEMPERATURE("Temperature", "Température", false),	
	WINDOW("Window", "Fenêtre", false),	
	HUMIDITY("Humidity", "Humidité", false),	
	LIGHT("Light", "Lumière", false),
	GAS("Gas", "Gaz", false),
	GLASS_BREAKAGE("Glass breakage", "Bris de vitre", false),
	ACOUSTIC("Acoustic", "Sonore", false),
	MANUAL_TRIGGER("Manuel trigger", "Déclencheur d'alarme manuel", true),
	ACCESS_CONTROL("Access controle", "Contrôle d'accès", true),
	FLOOD("Flood", "Inondation", false);
	
	
	private String englishLabel;
	private String frenchLabel;	
	private boolean isManual;
	
	/**
	 * @param englishLabel
	 * @param frenchLabel
	 */
	SensorType(String englishLabel, String frenchLabel, boolean isManual) {
		this.englishLabel = englishLabel;
		this.frenchLabel = frenchLabel;
		this.isManual = isManual;
	}

	public static SensorType getSensorType(String sensorType)
	{
		SensorType[] values = SensorType.values();
		for(SensorType value : values)
		{
			if(value.toString().equalsIgnoreCase(sensorType))
				return value;
		}
		return null;
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

	public boolean isManual() {
		return isManual;
	}
}
