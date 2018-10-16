# CSVData
Java Application that parses a CSV file data and writes into a database.
We can run the java application which is built using maven in eclipse by
- Clone or download zip
- Import the File->Import->General->Existing projects into workspace(next)->browse archive file(downloaded zip)->Finish
- Right click on the application 
- Click on Run As-> Java Application

After successful run, console get populated with the csv data and log report and you may need to refresh(right click->refresh) the project 
for the bad-data-<timestamp>.csv file and databasefile in the project files. 

To solve this problem we first read the file using an instance of BufferedReader. 
After successfully reading a row we need to get individual column values by parsing one line at a time. For that we used regex. 
String[] res = str.split(",(?=([^\"]|\"[^\"]*\")*$)");
The expression ensures that a split occurs only at commas which are followed by an even (or zero) number of quotes (and thus not inside such quotes).
Nevertheless, it may be easier to use a simple non-regex parser.

Then after successfully parsing an entry for respective columns, we check whether the number of column values we are getting matches what we require i.e. 10. 
If the entry has more than 10 columns we put that entry in bad data, which is later printed in bad-data-<timestamp>.csv file. The valid data is saved in an array. 

In next step we needed to write correct entries to sqlite db. 
We first created a table NewTable which contain 10 columns as needed. Then the saved data is added to the db sequentialy. 
