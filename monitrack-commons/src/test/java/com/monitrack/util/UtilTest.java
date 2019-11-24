package com.monitrack.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {
	
	
	@Test
	public void checkDatabaseInformation()
	{
		assertEquals(Util.getPropertyValueFromPropertiesFile("database"), "monitrack");
		assertEquals(Util.getPropertyValueFromPropertiesFile("driver"), "mysql");
	}
	
	@Test
	public void capitalize()
	{
		String strCapitalized = Util.capitalize("                hHVDhkdHJGDfhksd               ");
		assertEquals(strCapitalized, "Hhvdhkdhjgdfhksd");
	}

}
