# HMS


Environment setup for further development or testing:

Recommended IDE: IntelliJ IDEA

JDK: Bellsoft Liberica JDK (11) complete version (included JavaFX)

Database: MySQL (Look in the database properties file for configuration)

List of dependencies: 

ControlFX (11)

MySQL Java connector (5.1)

Apache common logging (1.2), IO (2.8), beanutils (1.8), collections (4.4)

JasperReports (6.15)

IText (2.1)

PDFBox (2.2)


How to get started:


Clone the github repo.

Install the recommended IDE and JDK.

Install MySQL and configure it according to DBConfig.properties.

Run db.sql file to intialize the database.

Download the required dependencies and add them to your project lib or use maven.

Run the program and login using the following test credentials

Receptionist: user: “recep” , pass: “recep”

Clinic doctor: “doc” , “doc”

Lab doctor: “lab” , “lab”

Emergency doctor: “emerg” , “emerg”

