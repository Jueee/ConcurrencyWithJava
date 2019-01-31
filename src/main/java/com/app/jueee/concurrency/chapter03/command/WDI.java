package com.app.jueee.concurrency.chapter03.command;

/**
 * Class that stores an item of data of the World Development Indicators
 * @author author
 *
 */
public class WDI {

	public final static short FIRST_YEAR=1960;
			
	private String countryName;
	private String countryCode;
	private String indicatorName;
	private String indicatorCode;
	private Double values[];
	
	public void setData(String[] data) throws Exception {
		if (data.length!=59) {
			throw new Exception("Data length is not correct: "+data.length);
		}
		values=new Double[55];
		countryName=getString(data[0]);
		countryCode=getString(data[1]);
		indicatorName=getString(data[2]);
		indicatorCode=getString(data[3]);
		values[0]=getValue(data[4]);
		values[1]=getValue(data[5]);
		values[2]=getValue(data[6]);
		values[3]=getValue(data[7]);
		values[4]=getValue(data[8]);
		values[5]=getValue(data[9]);
		values[6]=getValue(data[10]);
		values[7]=getValue(data[11]);
		values[8]=getValue(data[12]);
		values[9]=getValue(data[13]);
		values[10]=getValue(data[14]);
		values[11]=getValue(data[15]);
		values[12]=getValue(data[16]);
		values[13]=getValue(data[17]);
		values[14]=getValue(data[18]);
		values[15]=getValue(data[19]);
		values[16]=getValue(data[20]);
		values[17]=getValue(data[21]);
		values[18]=getValue(data[22]);
		values[19]=getValue(data[23]);
		values[20]=getValue(data[24]);
		values[21]=getValue(data[25]);
		values[22]=getValue(data[26]);
		values[23]=getValue(data[27]);
		values[24]=getValue(data[28]);
		values[25]=getValue(data[29]);
		values[26]=getValue(data[30]);
		values[27]=getValue(data[31]);
		values[28]=getValue(data[32]);
		values[29]=getValue(data[33]);
		values[30]=getValue(data[34]);
		values[31]=getValue(data[35]);
		values[32]=getValue(data[36]);
		values[33]=getValue(data[37]);
		values[34]=getValue(data[38]);
		values[35]=getValue(data[39]);
		values[36]=getValue(data[40]);
		values[37]=getValue(data[41]);
		values[38]=getValue(data[42]);
		values[39]=getValue(data[43]);
		values[40]=getValue(data[44]);
		values[41]=getValue(data[45]);
		values[42]=getValue(data[46]);
		values[43]=getValue(data[47]);
		values[44]=getValue(data[48]);
		values[45]=getValue(data[49]);
		values[46]=getValue(data[50]);
		values[47]=getValue(data[51]);
		values[48]=getValue(data[52]);
		values[49]=getValue(data[53]);
		values[50]=getValue(data[54]);
		values[51]=getValue(data[55]);
		values[52]=getValue(data[56]);
		values[53]=getValue(data[57]);
		values[54]=getValue(data[58]);		
	}

	private String getString(String string) {
		if (string.startsWith("\"")) {
			return string.substring(1,string.length()-1);
		}
		return string;
	}

	private Double getValue(String string) {
		if (string.trim().length()==0) {
			return 0.0d;
		}
		return Double.valueOf(string);
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public Double getValue(short year) {
		if ((year < FIRST_YEAR ) || (year >= FIRST_YEAR+values.length)) {
			throw new IllegalArgumentException("No data for that year");
		}
		
		short index=(short)(year-FIRST_YEAR);
		return values[index];
	}
	
	public Double[] getValues() {
		return values;
	}

}
