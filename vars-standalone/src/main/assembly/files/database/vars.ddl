-- Timestamp: 2005-11-08 08:23:27.85
-- Source database is: VARS
-- Connection URL is: jdbc:derby://oyashio.shore.mbari.org:1527/VARS;user=varsuser;password=vars0sourceforge
-- appendLogs: false

-- ----------------------------------------------
-- DDL Statements for schemas
-- ----------------------------------------------

CREATE SCHEMA "VARSUSER";

-- ----------------------------------------------
-- DDL Statements for tables
-- ----------------------------------------------

CREATE TABLE "VARSUSER"."CONCEPTDELEGATE" ("ID" BIGINT NOT NULL, "CONCEPTID_FK" BIGINT, "USAGEID_FK" BIGINT);

CREATE TABLE "VARSUSER"."VIDEOARCHIVE" ("ID" BIGINT NOT NULL, "VIDEOARCHIVESETID_FK" BIGINT, "TAPENUMBER" SMALLINT, "STARTTIMECODE" VARCHAR(11), "VIDEOARCHIVENAME" VARCHAR(20));

CREATE TABLE "VARSUSER"."SECTIONINFO" ("ID" BIGINT NOT NULL, "CONCEPTDELEGATEID_FK" BIGINT NOT NULL, "HEADER" VARCHAR(30) NOT NULL, "LABEL" VARCHAR(50) NOT NULL, "INFORMATION" VARCHAR(5000));

CREATE TABLE "VARSUSER"."PREFS" ("NODENAME" VARCHAR(255) NOT NULL, "PREFKEY" VARCHAR(50) NOT NULL, "PREFVALUE" VARCHAR(255) NOT NULL);

CREATE TABLE "VARSUSER"."ASSOCIATION" ("ID" BIGINT NOT NULL, "OBSERVATIONID_FK" BIGINT, "LINKNAME" VARCHAR(50), "TOCONCEPT" VARCHAR(50), "LINKVALUE" VARCHAR(100));

CREATE TABLE "VARSUSER"."HISTORY" ("ID" BIGINT NOT NULL, "CONCEPTDELEGATEID_FK" BIGINT, "APPROVALDTG" TIMESTAMP, "CREATIONDTG" TIMESTAMP, "CREATORNAME" VARCHAR(50), "APPROVERNAME" VARCHAR(50), "FIELD" VARCHAR(50), "OLDVALUE" VARCHAR(2048), "NEWVALUE" VARCHAR(2048), "ACTION" VARCHAR(16), "COMMENT" VARCHAR(2048), "REJECTED" SMALLINT NOT NULL);

CREATE TABLE "VARSUSER"."CONCEPTNAME" ("CONCEPTNAME" VARCHAR(50) NOT NULL, "CONCEPTID_FK" BIGINT, "AUTHOR" VARCHAR(255), "NAMETYPE" VARCHAR(10), "ID" BIGINT NOT NULL);

CREATE TABLE "VARSUSER"."MEDIA" ("ID" BIGINT NOT NULL, "CONCEPTDELEGATEID_FK" BIGINT, "URL" VARCHAR(1024), "MEDIATYPE" varchar(15), "PRIMARYMEDIA" SMALLINT DEFAULT 0, "CREDIT" VARCHAR(255), "CAPTION" VARCHAR(1000));

CREATE TABLE "VARSUSER"."UNIQUEID" ("TABLENAME" VARCHAR(200) NOT NULL, "NEXTID" BIGINT);

CREATE TABLE "VARSUSER"."LINKTEMPLATE" ("ID" BIGINT NOT NULL, "CONCEPTDELEGATEID_FK" BIGINT, "LINKNAME" VARCHAR(50), "TOCONCEPT" VARCHAR(50), "LINKVALUE" VARCHAR(255));

CREATE TABLE "VARSUSER"."LINKREALIZATION" ("ID" BIGINT NOT NULL, "CONCEPTDELEGATEID_FK" BIGINT, "PARENTLINKREALIZATIONID_FK" BIGINT, "LINKNAME" VARCHAR(50), "TOCONCEPT" VARCHAR(50), "LINKVALUE" VARCHAR(255));

CREATE TABLE "VARSUSER"."PHYSICALDATA" ("ID" BIGINT NOT NULL, "VIDEOFRAMEID_FK" BIGINT, "DEPTH" REAL, "TEMPERATURE" REAL, "SALINITY" REAL, "OXYGEN" REAL, "LIGHT" REAL, "LATITUDE" REAL, "LONGITUDE" REAL);

CREATE TABLE "VARSUSER"."CAMERADATA" ("ID" BIGINT NOT NULL, "VIDEOFRAMEID_FK" BIGINT, "NAME" VARCHAR(50), "DIRECTION" VARCHAR(50), "ZOOM" INTEGER, "FOCUS" INTEGER, "IRIS" INTEGER, "FIELDWIDTH" DOUBLE, "STILLIMAGEURL" VARCHAR(1024));

CREATE TABLE "VARSUSER"."USAGE" ("ID" BIGINT NOT NULL, "CONCEPTDELEGATEID_FK" BIGINT, "EMBARGOEXPIRATIONDATE" TIMESTAMP, "SPECIFICATION" VARCHAR(1000));

CREATE TABLE "VARSUSER"."CAMERAPLATFORMDEPLOYMENT" ("ID" BIGINT NOT NULL, "VIDEOARCHIVESETID_FK" BIGINT, "SEQNUMBER" INTEGER, "CHIEFSCIENTIST" VARCHAR(50), "USAGESTARTDTG" TIMESTAMP, "USAGEENDDTG" TIMESTAMP);

CREATE TABLE "VARSUSER"."LINKREALIZATION_CHILD_PARENT" ("CHILDLINKREALIZATIONID_FK" BIGINT NOT NULL, "PARENTLINKREALIZATIONID_FK" BIGINT NOT NULL);

CREATE TABLE "VARSUSER"."CONCEPT" ("ID" BIGINT NOT NULL, "PRIMARYCONCEPTNAMEID_FK" BIGINT, "PARENTCONCEPTID_FK" BIGINT, "ORIGINATOR" VARCHAR(255), "STRUCTURETYPE" VARCHAR(10), "REFERENCE" VARCHAR(1024), "NODCCODE" VARCHAR(20), "RANKNAME" VARCHAR(20), "RANKLEVEL" VARCHAR(20), "TAXONOMYTYPE" VARCHAR(20), "CONCEPTDELEGATEID_FK" BIGINT);

CREATE TABLE "VARSUSER"."USERACCOUNT" ("ID" BIGINT NOT NULL, "USERNAME" VARCHAR(50) NOT NULL, "PASSWORD" VARCHAR(50), "ROLE" VARCHAR(10));

CREATE TABLE "VARSUSER"."OBSERVATION" ("ID" BIGINT NOT NULL, "VIDEOFRAMEID_FK" BIGINT, "OBSERVATIONDTG" TIMESTAMP, "OBSERVER" VARCHAR(50), "CONCEPTNAME" VARCHAR(50), "NOTES" VARCHAR(200));

CREATE TABLE "VARSUSER"."VIDEOARCHIVESET" ("ID" BIGINT NOT NULL, "TRACKINGNUMBER" VARCHAR(7), "SHIPNAME" VARCHAR(32), "PLATFORMNAME" VARCHAR(32), "FORMATCODE" VARCHAR(2), "STARTDTG" TIMESTAMP, "ENDDTG" TIMESTAMP);

CREATE TABLE "VARSUSER"."VIDEOFRAME" ("ID" BIGINT NOT NULL, "RECORDEDDTG" TIMESTAMP, "TAPETIMECODE" VARCHAR(11), "HDTIMECODE" VARCHAR(11), "INSEQUENCE" SMALLINT, "CAMERADATAID_FK" BIGINT, "PHYSICALDATAID_FK" BIGINT, "DISPLACER" VARCHAR(50), "DISPLACEDTG" TIMESTAMP, "VIDEOARCHIVEID_FK" BIGINT);

CREATE TABLE "VARSUSER"."ASSOCIATION_CHILD_PARENT" ("CHILD_ASSOCIATIONID" BIGINT NOT NULL, "PARENT_ASSOCIATIONID" BIGINT NOT NULL);

-- ----------------------------------------------
-- DDL Statements for indexes
-- ----------------------------------------------

CREATE INDEX "VARSUSER"."IDX_VIDEOARCHIVENAME" ON "VARSUSER"."VIDEOARCHIVE" ("VIDEOARCHIVENAME");

CREATE INDEX "VARSUSER"."IDX_VIDEOARCHIVESETID_FK2" ON "VARSUSER"."VIDEOARCHIVE" ("VIDEOARCHIVESETID_FK");

CREATE INDEX "VARSUSER"."IDX_ASSOCIATION_CHILD_PARENT" ON "VARSUSER"."ASSOCIATION_CHILD_PARENT" ("CHILD_ASSOCIATIONID");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE4" ON "VARSUSER"."LINKTEMPLATE" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE6" ON "VARSUSER"."SECTIONINFO" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_PRIMARYCONCEPTNAME" ON "VARSUSER"."CONCEPT" ("PRIMARYCONCEPTNAMEID_FK");

CREATE INDEX "VARSUSER"."IDX_PARENT_CONCEPTID" ON "VARSUSER"."CONCEPT" ("PARENTCONCEPTID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE" ON "VARSUSER"."CONCEPT" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_VIDEOARCHIVEID_FK2" ON "VARSUSER"."VIDEOFRAME" ("VIDEOARCHIVEID_FK");

CREATE INDEX "VARSUSER"."IDX_CAMERADATAID_FK" ON "VARSUSER"."VIDEOFRAME" ("CAMERADATAID_FK");

CREATE INDEX "VARSUSER"."IDX_PHYSICALDATAID_FK" ON "VARSUSER"."VIDEOFRAME" ("PHYSICALDATAID_FK");

CREATE INDEX "VARSUSER"."IDX_OBSERVATIONID_FK" ON "VARSUSER"."ASSOCIATION" ("OBSERVATIONID_FK");

CREATE INDEX "VARSUSER"."IDX_TOCONCEPT" ON "VARSUSER"."ASSOCIATION" ("TOCONCEPT");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE3" ON "VARSUSER"."LINKREALIZATION" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE2" ON "VARSUSER"."HISTORY" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_VIDEOFRAMEID_FK3" ON "VARSUSER"."PHYSICALDATA" ("VIDEOFRAMEID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE5" ON "VARSUSER"."MEDIA" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_OBSERVATIONDTG" ON "VARSUSER"."OBSERVATION" ("OBSERVATIONDTG");

CREATE INDEX "VARSUSER"."IDX_CONCEPTNAME" ON "VARSUSER"."OBSERVATION" ("CONCEPTNAME");

CREATE INDEX "VARSUSER"."IDX_VIDEOFRAMEID_FKD" ON "VARSUSER"."OBSERVATION" ("VIDEOFRAMEID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTNAME_CONCEPTID" ON "VARSUSER"."CONCEPTNAME" ("CONCEPTID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTNAME2" ON "VARSUSER"."CONCEPTNAME" ("CONCEPTNAME");

CREATE INDEX "VARSUSER"."IDX_USAGEID" ON "VARSUSER"."CONCEPTDELEGATE" ("USAGEID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTID" ON "VARSUSER"."CONCEPTDELEGATE" ("CONCEPTID_FK");

CREATE INDEX "VARSUSER"."IDX_VIDEOARCHIVESETID_FK" ON "VARSUSER"."CAMERAPLATFORMDEPLOYMENT" ("VIDEOARCHIVESETID_FK");

CREATE INDEX "VARSUSER"."IDX_CONCEPTDELEGATE7" ON "VARSUSER"."USAGE" ("CONCEPTDELEGATEID_FK");

CREATE INDEX "VARSUSER"."IDX_VIDEOFRAMEID_FK" ON "VARSUSER"."CAMERADATA" ("VIDEOFRAMEID_FK");

-- ----------------------------------------------
-- DDL Statements for keys
-- ----------------------------------------------

-- primary/unique
ALTER TABLE "VARSUSER"."VIDEOARCHIVE" ADD CONSTRAINT "PK_VIDEOARCHIVE" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."VIDEOARCHIVESET" ADD CONSTRAINT "PK_VIDEOARCHIVESET" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."ASSOCIATION_CHILD_PARENT" ADD CONSTRAINT "PK_ASSOCIATION_CHILD_PARENT" PRIMARY KEY ("CHILD_ASSOCIATIONID", "PARENT_ASSOCIATIONID");

ALTER TABLE "VARSUSER"."LINKTEMPLATE" ADD CONSTRAINT "PK_LINKTEMPLATE" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."SECTIONINFO" ADD CONSTRAINT "PK_SECTIONINFO" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."USERACCOUNT" ADD CONSTRAINT "PK_USERACCOUNT" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."CONCEPT" ADD CONSTRAINT "CONCEPT_PK" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."VIDEOFRAME" ADD CONSTRAINT "PK_VIDEOFRAME" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."ASSOCIATION" ADD CONSTRAINT "PK_ASSOCIATION" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."LINKREALIZATION" ADD CONSTRAINT "PK_LINKREALIZATION" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."HISTORY" ADD CONSTRAINT "PK_HISTORY" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."PHYSICALDATA" ADD CONSTRAINT "PK_PHYSICALDATA" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."MEDIA" ADD CONSTRAINT "PK_MEDIA" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."OBSERVATION" ADD CONSTRAINT "PK_OBSERVATION" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."CONCEPTNAME" ADD CONSTRAINT "PK_CONCEPTNAME" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."CONCEPTDELEGATE" ADD CONSTRAINT "PK_CONCEPTDELEGATE" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."CAMERAPLATFORMDEPLOYMENT" ADD CONSTRAINT "PK_CAMERAPLATFORMDEPLOYMENT" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."USAGE" ADD CONSTRAINT "PK_USAGE" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."CAMERADATA" ADD CONSTRAINT "PK_CAMERADATA" PRIMARY KEY ("ID");

ALTER TABLE "VARSUSER"."LINKREALIZATION_CHILD_PARENT" ADD CONSTRAINT "PK_LINKREALIZATION_CHILD_PARENT" PRIMARY KEY ("CHILDLINKREALIZATIONID_FK", "PARENTLINKREALIZATIONID_FK");

ALTER TABLE "VARSUSER"."PREFS" ADD CONSTRAINT "PK_PREFS" PRIMARY KEY ("NODENAME", "PREFKEY");

-- ----------------------------------------------
-- DDL Statements for views
-- ----------------------------------------------

SET SCHEMA "VARSUSER";

CREATE VIEW "VARSUSER"."ANNOTATIONS" AS SELECT  
	OBSERVATION.OBSERVATIONDTG AS "OBSERVATIONDATE", 
	OBSERVATION.OBSERVER, 
	OBSERVATION.CONCEPTNAME,  
	OBSERVATION.NOTES, 
	VIDEOFRAME.TAPETIMECODE, 
	VIDEOFRAME.HDTIMECODE,
	VIDEOFRAME.RECORDEDDTG AS "RECORDEDDATE",  
	VIDEOFRAME.INSEQUENCE,          
	VIDEOARCHIVE.VIDEOARCHIVENAME,   
	VIDEOARCHIVESET.TRACKINGNUMBER, 
	VIDEOARCHIVESET.SHIPNAME,       
	VIDEOARCHIVESET.PLATFORMNAME AS "ROVNAME",        
	VIDEOARCHIVESET.FORMATCODE,     
	CAMERAPLATFORMDEPLOYMENT.SEQNUMBER AS "DIVENUMBER",    
	CAMERAPLATFORMDEPLOYMENT.CHIEFSCIENTIST,        
	CAMERAPLATFORMDEPLOYMENT.USAGESTARTDTG AS "DIVESTARTDATE",        
	CAMERAPLATFORMDEPLOYMENT.USAGEENDDTG AS "DIVEENDDATE",         
	CAMERADATA.NAME AS "CAMERANAME",          
	CAMERADATA.DIRECTION AS "CAMERADIRECTION",        
	CAMERADATA.ZOOM,        
	CAMERADATA.FOCUS,   
	CAMERADATA.IRIS,         
	CAMERADATA.FIELDWIDTH,          
	CAMERADATA.STILLIMAGEURL AS "IMAGE",      
	PHYSICALDATA.DEPTH,     
	PHYSICALDATA.TEMPERATURE,       
	PHYSICALDATA.SALINITY,       
	PHYSICALDATA.OXYGEN,    
	PHYSICALDATA.LIGHT,     
	PHYSICALDATA.LATITUDE,  
	PHYSICALDATA.LONGITUDE,         
	OBSERVATION.ID AS "OBSERVATIONID_FK",     
	ASSOCIATION.ID AS "ASSOCIATIONID_FK",          
	ASSOCIATION.LINKNAME,   
	ASSOCIATION.TOCONCEPT,          
	ASSOCIATION.LINKVALUE,          
	ASSOCIATION.LINKNAME || ' | ' || ASSOCIATION.TOCONCEPT || ' | ' || ASSOCIATION.LINKVALUE AS "ASSOCIATIONS",   
	VIDEOFRAME.ID AS "VIDEOFRAMEID_FK",       
	PHYSICALDATA.ID AS "PHYSICALDATAID_FK",   
	CAMERADATA.ID AS "CAMERADATAID_FK"
FROM                
	Association RIGHT OUTER JOIN     
	Observation ON Association.ObservationID_FK = Observation.id LEFT OUTER JOIN         
	PhysicalData RIGHT OUTER JOIN   
	VideoFrame ON PhysicalData.VideoFrameID_FK = VideoFrame.id LEFT OUTER JOIN  
	CameraData ON VideoFrame.id = CameraData.VideoFrameID_FK LEFT OUTER JOIN         
	CameraPlatformDeployment RIGHT OUTER JOIN       
	VideoArchiveSet ON CameraPlatformDeployment.VideoArchiveSetID_FK = VideoArchiveSet.id RIGHT OUTER JOIN       
	VideoArchive ON VideoArchiveSet.id = VideoArchive.VideoArchiveSetID_FK ON       
	VideoFrame.VideoArchiveID_FK = VideoArchive.id ON Observation.VideoFrameID_FK = VideoFrame.id;

-- Reverting back to default schema 'APP'
SET SCHEMA "APP";
