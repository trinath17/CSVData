//holds the data of an instance
package com.shaga.CSVData;

public class Data {
	public String data[] =  new String[10];
	public Data(String[] arr)
	{
		for(int i = 0; i < 10; i++)
		{
			data[i] = arr[i];
		}
	}	
}

