CREATE TABLE STORAGE
   (	ID NUMBER NOT NULL,
	FORMATS_SUPPORTED VARCHAR2(20) NOT NULL,
	STORAGE_COUNTRY VARCHAR2(20) NOT NULL,
	STORAGE_SIZE NUMBER NOT NULL,
	 CONSTRAINT STORAGE_PK PRIMARY KEY (ID)
);