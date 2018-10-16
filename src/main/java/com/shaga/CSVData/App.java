package com.shaga.CSVData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class App {

	public static final String FILE_NAME = "ms3Interview.csv";

	public static void main(String[] args) throws IOException {

		ArrayList<Data> arData = new ArrayList<Data>();
		ArrayList<String> errors = new ArrayList<String>();	//saved for error log
		BufferedReader bf = new BufferedReader(new FileReader(FILE_NAME)); // Input CSV file location
																			
		String s;
		int error = 0;
		int total = 0;
		//Input csv file is read line by line
		while ((s = bf.readLine()) != null) {
			System.out.println(s);
			//separating column values using regex
			String[] s2 = s.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			String[] newData = new String[10];
			boolean wrong = false;
			//Check if it has more than 10 column values
			for (int i = 10; i < s2.length; i++) {
				if (s2[i].length() > 0)
				{
					//System.out.println(s + s2.length + " " + s2[i]);
					wrong = true;
				}
			}
			if ( wrong == true) {
				error++;
			errors.add(s);
			} else if(s2.length < 10)
			{ 
				
			}
			else {
				
				for (int i = 0; i < 10; i++)
					newData[i] = s2[i];
				
				Data d = new Data(newData);
				arData.add(d);
			}
			total++;

		}
		bf.close();
		//Printing log
		//System.out.println(error);
		System.out.println("# of records received " + total);
		System.out.println("# of records successful " + (total - error));
		System.out.println("# of records failed " + error);
		
		Date dt = new Date();
		String dtString = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(dt);
		String fileName = "bad-data-" + dtString + ".csv";
		Path currentRelativePath = Paths.get("");
		String ss = currentRelativePath.toAbsolutePath().toString();

		String path = ss + "\\" + fileName;
		File file = new File(path);

		// If file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < error; i++)
			bw.write(errors.get(i) + "\n");
		bw.close();
		//Writing to database. 
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:mydb.db");
			stmt = c.createStatement();
			String drop_sql = "DROP TABLE IF EXISTS NewTable";
			stmt.executeUpdate(drop_sql);
			String create_sql = "CREATE TABLE NewTable " + "(A     CHAR(20) , " + // first
																					// name
					"B CHAR(20) ," + // last name
					"C CHAR(50) ," + // email
					"D CHAR(20) ," + // gender
					"E CHAR(2000) ," + // data
					"F  CHAR(50)   , " + // string
					"G CHAR(20) ," + // amount
					"H CHAR(20) ," + // boolean 1
					"I CHAR(20) ," + // boolean 2
					"J  CHAR(50)  )"; // string

			stmt.executeUpdate(create_sql);

			int i = 0, count = arData.size();
			while (i < count) {
				String[] col= arData.get(i).data;
				String var1 = col[0];
				String var2 = col[1];
				String var3 = col[2];
				String var4 = col[3];
				String var5 = col[4];
				String var6 = col[5];
				String var7 = col[6];
				String var8 = col[7];
				String var9 = col[8];
				String var10 = col[9];
				String query = "INSERT INTO NewTable VALUES (" + "'" + var1 + "', " + "'" + var2 + "', " + "'" + var3
						+ "', " + "'" + var4 + "', " + "'" + var5 + "', " + "'" + var6 + "', " + "'" + var7 + "', "
						+ "'" + var8 + "', " + "'" + var9 + "', " + "'" + var10 + "')";
				stmt.addBatch(query);
				i++;
			}
			stmt.executeBatch();
			stmt.close();
			c.close();
		} catch (Exception e) {
			//System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.exit(0);
		}

	}
}