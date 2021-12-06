# Bachelorarbeit

1) Configuring the Database
   - Install the mySQL Workbench https://dev.mysql.com/downloads/installer/
   - Execute the SQL Queries from the file "MySQLDatabase" to create all tables with their entries
   - Change the e-mail-accounts in the entries from the table "Employee" as it must be E-Mails, where you have access

2) Connection between IntelliJ and MySQL
   - Add in the file "application.properties" your mySQL url, username and password where the placeholder *** are:
        -->spring.datasource.url=***
        -->spring.datasource.username=***
        -->spring.datasource.password=***
   - Change the values in the class "DBConnection" accordingly:
        -->String jdbURL = *** ;
        -->String username = *** ;
        -->String password = *** ;
3) E-Mail for administration
   - create an e-mail-account in gmail; 
   - activate "less secure app access" in the account
   - add the e-mail settings to the file "application.properties where the placeholder *** are:
         -->spring.mail.username = ***
         -->spring.mail.password = ***
4) Execute the tool
   - run the class "Main" or run manually "Run Maven Build" to get executable Jar-File
