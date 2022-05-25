drop database if exists hms;
create database hms;
ALTER DATABASE hms CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
use hms;
/*
Cascade delete on details tables!
*/

/*
1,الإدارة,0
2,الاستقبال,0
3,المستودع,0
4,الصيدلية,0
5,الطوارئ,0
6,المختبر,1
7,الآشعة,1
8,النساء والتوليد,1
9,الطب العام,1
10,الاطفال,1
11,الباطنة,1
12,الجلدية,1
13,الاسنان,1

insert into facilities (name, isSP,enName) VALUES ('الإدارة',FALSE,'Management');
insert into facilities (name, isSP,enName) VALUES ('الاستقبال',FALSE,'Reception');
insert into facilities (name, isSP,enName) VALUES ('المستودع',FALSE,'Warehouse');
insert into facilities (name, isSP,enName) VALUES ('الصيدلية',FALSE,'Pharmacy');
insert into facilities (name, isSP,enName) VALUES ('الطوارئ',FALSE,'Emergency');
insert into facilities (name, isSP,enName) VALUES ('المختبر',TRUE,'Laboratory');
insert into facilities (name, isSP,enName) VALUES ('الآشعة',TRUE,'Radiology');
insert into facilities (name, isSP,enName) VALUES ('النساء والتوليد',TRUE,'Obs & Gyn');
insert into facilities (name, isSP,enName) VALUES ('الطب العام',TRUE,'General Medicine');
insert into facilities (name, isSP,enName) VALUES ('الاطفال',TRUE,'Kids');
insert into facilities (name, isSP,enName) VALUES ('الباطنة',TRUE,'Internal Medicine');
insert into facilities (name, isSP,enName) VALUES ('الجلدية',TRUE,'Dermatology');
insert into facilities (name, isSP,enName) VALUES ('الاسنان',TRUE,'Dental');

1,الإدارة,0,Management
2,الاستقبال,0,Reception
3,المستودع,0,Warehouse
4,الصيدلية,0,Pharmacy
5,الطوارئ,0,Emergency
6,المختبر,1,Laboratory
7,الآشعة,1,Radiology
8,النساء والتوليد,1,Obs & Gyn
9,الطب العام,1,General Medicine
10,الاطفال,1,Kids
11,الباطنة,1,Internal Medicine
12,الجلدية,1,Dermatology
13,الاسنان,1,Dental

insert into employeestypes (name) values ('طبيب عيادة');
insert into employeestypes (name) values ('طبيب طوارئ');
insert into employeestypes (name) values ('طبيب مختبر');
insert into employeestypes (name) values ('طبيب آشعة');
insert into employeestypes (name) values ('طبيب صيدلية');
insert into employeestypes (name) values ('موظف استقبال');
insert into employeestypes (name) values ('موظف مستودع');
insert into employeestypes (name) values ('مدير');


insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد مدير','a','a',8,1);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد ريسبشن','r','r',6,2);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد مستودع','w','w',7,3);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد صيدلية','p','p',5,4);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد طوارئ','e','e',2,5);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد مختبر','l','l',3,6);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد أشعة','ra','ra',4,7);
insert into employees (name,userName,userPass,employeeTypeID,facilityID) values ('احمد طب عام','d','d',1,9);


*/
CREATE TABLE facilities
(
    id     INT NOT NULL AUTO_INCREMENT,
    name   VARCHAR(50),
    enName VARCHAR(50),
    isSP   BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE medicineUnits
(
    id   INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY (id)
);
CREATE TABLE medicines
(
    id          INT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100),
    barcode     VARCHAR(13) UNIQUE,
    retailPrice INT,
    unitID      INT,
    packSize    INT,
    PRIMARY KEY (id),
    FOREIGN KEY (unitID) REFERENCES medicineUnits (id)
);
CREATE TABLE pharmacyMedicines
(
    id    INT NOT NULL,
    price INT,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES medicines (id)
);
CREATE TABLE emergencyMedicines
(
    id    INT NOT NULL,
    price INT,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES medicines (id)
);

CREATE TABLE patients
(
    id          INT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100),
    nationalID  VARCHAR(50),
    gender      VARCHAR(50),
    dateOfBirth DATE,
    IDScan      MEDIUMBLOB,
    IDFileName  VARCHAR(30),
    address     VARCHAR(150),
    email       VARCHAR(50),
    phone       VARCHAR(50),
    nationality VARCHAR(50),
    marital     VARCHAR(50),
    allergies   VARCHAR(150),
    bloodType   VARCHAR(10),
    birthPlace  VARCHAR(50),
    PRIMARY KEY (id)
);
CREATE TABLE taxFreeNationalities
(
    name varchar(50),
    PRIMARY KEY (name)
);
CREATE TABLE employeesTypes
(
    id   INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY (id)
);
CREATE TABLE employees
(
    id             INT NOT NULL AUTO_INCREMENT,
    name           VARCHAR(100),
    enName         VARCHAR(100),
    nationalID     VARCHAR(50),
    gender         VARCHAR(50),
    dateOfBirth    DATE,
    address        VARCHAR(150),
    email          VARCHAR(50),
    phone          VARCHAR(50),
    nationality    VARCHAR(50),
    marital        VARCHAR(50),
    degree         VARCHAR(100),
    employeeTypeID INT,
    facilityID     INT,
    userName       VARCHAR(50) UNIQUE,
    userPass       VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (facilityID) REFERENCES facilities (id),
    FOREIGN KEY (employeeTypeID) REFERENCES employeesTypes (id)
);


CREATE TABLE serviceTypes
(
    id              INT NOT NULL AUTO_INCREMENT,
    serviceTypeName varchar(50),
    PRIMARY KEY (id)
);
CREATE TABLE services
(
    id            INT NOT NULL AUTO_INCREMENT,
    serviceTypeID INT,
    name          varchar(50),
    facilityID    INT,
    cost          INT,
    PRIMARY KEY (id),
    FOREIGN KEY (serviceTypeID) REFERENCES serviceTypes (id),
    foreign key (facilityID) REFERENCES facilities (id)
);

CREATE TABLE contracts
(
    id   INT NOT NULL AUTO_INCREMENT,
    name varchar(50),
    PRIMARY KEY (id)
);

CREATE TABLE contractsDetails
(
    contractID    INT,
    serviceTypeID INT,
    percent       INT,
    FOREIGN KEY (serviceTypeID) REFERENCES serviceTypes (id),
    FOREIGN KEY (contractID) REFERENCES contracts (id)
);

CREATE TABLE bills
(
    id             INT NOT NULL AUTO_INCREMENT,
    patientID      INT,
    receptionistID INT,
    date           DATE,
    amount         INT,
    paid           INT DEFAULT (0),
    contractID     INT,
    PRIMARY KEY (id),
    FOREIGN KEY (patientID) REFERENCES patients (id),
    FOREIGN KEY (receptionistID) REFERENCES employees (id),
    FOREIGN KEY (contractID) REFERENCES contracts (id)

);
CREATE TABLE providedServices
(
    id           INT NOT NULL AUTO_INCREMENT,
    serviceID    INT,
    patientID    INT,
    employeeID   INT,
    doctorID     INT,
    facilityID   INT,
    registerDate date,
    doneDate     date DEFAULT (NULL),
    notes        VARCHAR(255),
    billID       INT,
    cost         INT,
    PRIMARY KEY (id),
    FOREIGN KEY (serviceID) REFERENCES services (id),
    FOREIGN KEY (patientID) REFERENCES patients (id),
    FOREIGN KEY (employeeID) REFERENCES employees (id),
    FOREIGN KEY (doctorID) REFERENCES employees (id),
    FOREIGN KEY (facilityID) REFERENCES facilities (id),
    FOREIGN KEY (billID) REFERENCES bills (id)
);
CREATE TABLE recipes
(
    id         INT NOT NULL AUTO_INCREMENT,
    patientID  INT,
    doctorID   INT,
    facilityID INT,
    visitID    INT,
    date       DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (doctorID)
        REFERENCES employees (id),
    FOREIGN KEY (facilityID)
        REFERENCES facilities (id),
    FOREIGN KEY (patientID)
        REFERENCES patients (id),
    FOREIGN KEY (visitID)
        REFERENCES providedServices (id)

);
CREATE TABLE recipeDetails
(
    medicineID INT,
    dose       VARCHAR(50),
    recipeID   INT,
    FOREIGN KEY (medicineID)
        REFERENCES medicines (id),
    FOREIGN KEY (recipeID)
        REFERENCES recipes (id)
);

CREATE TABLE suppliers
(
    id      INT NOT NULL,
    name    varchar(100),
    phone   varchar(20),
    address VARCHAR(150),
    email   VARCHAR(50),
    PRIMARY KEY (id)
);
CREATE TABLE warehouseIn
(
    id           INT NOT NULL AUTO_INCREMENT,
    retailAmount INT,
    date         DATE,
    supplierID   INT,
    PRIMARY KEY (id),
    FOREIGN KEY (supplierID) REFERENCES suppliers (id)
);
CREATE TABLE warehouseInDetails
(
    warehouseInID INT,
    medicineID    INT,
    quantity      INT,
    price         INT,
    expireDate    DATE,
    FOREIGN KEY (warehouseInID) REFERENCES warehouseIn (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);
CREATE TABLE warehouseStock
(
    id         INT NOT NULL AUTO_INCREMENT,
    medicineID INT,
    quantity   INT,
    expireDate DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);
CREATE TABLE medicinesRequests
(
    id                  INT NOT NULL AUTO_INCREMENT,
    employeeID          INT,
    warehouseEmployeeID INT,
    isDone              BOOLEAN,
    facilityID          INT,
    date                DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (employeeID) REFERENCES employees (id),
    FOREIGN KEY (warehouseEmployeeID) REFERENCES employees (id),
    FOREIGN KEY (facilityID) REFERENCES facilities (id)
);
CREATE TABLE medicinesRequestsDetails
(
    requestID  INT,
    medicineID INT,
    quantity   INT,
    FOREIGN KEY (requestID) REFERENCES medicinesRequests (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);
CREATE TABLE warehouseOut
(
    id             INT NOT NULL AUTO_INCREMENT,
    employeeID     INT,
    requestID      INT,
    retailAmount   INT,
    customerAmount INT,
    date           DATE,
    facilityID     INT,
    PRIMARY KEY (id),
    FOREIGN KEY (requestID) REFERENCES medicinesRequests (id),
    FOREIGN KEY (facilityID) REFERENCES facilities (id)
);
CREATE TABLE warehouseOutDetails
(
    warehouseOutID INT,
    medicineID     INT,
    quantity       INT,
    expiryDate     date,
    retailPrice    INT,
    price          INT,
    FOREIGN KEY (warehouseOutID) REFERENCES warehouseOut (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)

);

CREATE TABLE emergencyStock
(
    id         INT NOT NULL AUTO_INCREMENT,
    medicineID INT,
    quantity   INT,
    expireDate DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);

CREATE TABLE emergencyOrders
(
    id         INT NOT NULL AUTO_INCREMENT,
    patientID  INT,
    doctorID   INT,
    facilityID INT,
    visitID    INT,
    date       DATE,
    billID     INT,
    medicineID INT,
    dose       VARCHAR(50),
    isDone     BOOLEAN DEFAULT (FALSE),
    PRIMARY KEY (id),
    FOREIGN KEY (doctorID)
        REFERENCES employees (id),
    FOREIGN KEY (facilityID)
        REFERENCES facilities (id),
    FOREIGN KEY (patientID)
        REFERENCES patients (id),
    FOREIGN KEY (visitID)
        REFERENCES providedServices (id),
    FOREIGN KEY (medicineID)
        REFERENCES medicines (id)

);
CREATE TABLE emergencyOutDetails
(
    orderID    INT,
    employeeID INT,
    medicineID INT,
    quantity   INT,
    price      INT,
    expiryDate date,
    FOREIGN KEY (orderID) REFERENCES emergencyOrders (id),
    FOREIGN KEY (employeeID) REFERENCES employees (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);

CREATE TABLE pharmacyStock
(
    id         INT NOT NULL AUTO_INCREMENT,
    medicineID INT,
    quantity   INT,
    expireDate DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);
CREATE TABLE pharmacyBills
(
    id         INT NOT NULL AUTO_INCREMENT,
    employeeID INT,
    patientID  INT,
    recipeID   INT,
    contractID INT,
    amount     INT,
    date       DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (employeeID) REFERENCES employees (id),
    FOREIGN KEY (patientID) REFERENCES patients (id),
    FOREIGN KEY (recipeID) REFERENCES recipes (id),
    FOREIGN KEY (contractID) REFERENCES contracts (id)
);
CREATE TABLE pharmacyBillsDetails
(
    billID     INT,
    medicineID INT,
    quantity   INT,
    price      INT,
    expiryDate date,
    FOREIGN KEY (billID) REFERENCES pharmacyBills (id),
    FOREIGN KEY (medicineID) REFERENCES medicines (id)
);


CREATE TABLE babies
(
    id        INT NOT NULL AUTO_INCREMENT,
    name      varchar(50),
    gender    varchar(10),
    birthTime time,
    birthDate date,
    age       INT,
    height    INT,
    weight    INT,
    headCer   INT,
    hairColor varchar(30),
    birthOpID INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (birthOpID) REFERENCES providedServices (id)

);

CREATE TABLE sickLeaves
(
    id            INT NOT NULL AUTO_INCREMENT,
    patientID     INT,
    doctorID      INT,
    facilityID    INT,
    visitID       INT,
    startDate     date,
    endDate       date,
    enName        varchar(50),
    enNationality varchar(50),
    enEmployer    varchar(50),
    enJob         varchar(50),
    arEmployer    varchar(50),
    arJob         varchar(50),
    PRIMARY KEY (id),
    FOREIGN KEY (patientID) REFERENCES patients (id),
    FOREIGN KEY (doctorID) REFERENCES employees (id),
    FOREIGN KEY (visitID) REFERENCES providedservices (id)
);


