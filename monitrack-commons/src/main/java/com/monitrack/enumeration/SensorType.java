package com.monitrack.enumeration;


public enum SensorType {
	
	//FIXME - Cheikna : Why do not I remove the english label and make a "this.toString().toLowerCase()" in the constructor ?
	SMOKE("Smoke", "Fum�e", false),	
	FLOW("Flow", "Pr�sence", false),	
	DOOR("Door", "Porte", false),	
	TEMPERATURE("Temperature", "Temp�rature", false),	
	WINDOW("Window", "Fen�tre", false),	
	HUMIDITY("Humidity", "Humidit�", false),	
	LIGHT("Light", "Lumi�re", false),
	GAS("Gas", "Gaz", false),
	GLASS_BREAKAGE("Glass breakage", "Bris de vitre", false),
	ACOUSTIC("Acoustic", "Sonore", false),
	MANUAL_TRIGGER("Manuel trigger", "D�clencheur d'alarme manuel", true),
	ACCESS_CONTROL("Access controle", "Contr�le d'acc�s", true),
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
