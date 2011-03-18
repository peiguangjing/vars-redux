/*
Script generated by Aqua Data Studio 9.0.1 on Jan-30-2011 10:11:33 PM
Database: VARS
Schema: dbo
Objects: TABLE, VIEW, INDEX
*/
ALTER TABLE dbo.VideoFrame
	DROP CONSTRAINT VideoFrame_FK1
GO
ALTER TABLE dbo.VideoArchive
	DROP CONSTRAINT VideoArchive_FK1
GO
ALTER TABLE dbo.PhysicalData
	DROP CONSTRAINT PhysicalData_FK1
GO
ALTER TABLE dbo.Observation
	DROP CONSTRAINT Observation_FK1
GO
ALTER TABLE dbo.CameraPlatformDeployment
	DROP CONSTRAINT CameraDeployment_FK1
GO
ALTER TABLE dbo.CameraData
	DROP CONSTRAINT CameraData_FK1
GO
ALTER TABLE dbo.Association
	DROP CONSTRAINT Association_FK1
GO
ALTER TABLE dbo.VideoArchive
	DROP CONSTRAINT uc_VideoArchiveName
GO
ALTER TABLE dbo.PhysicalData
	DROP CONSTRAINT uc_PhysicalData_FK1
GO
ALTER TABLE dbo.CameraData
	DROP CONSTRAINT uc_VideoFrameID_FK
GO
DROP INDEX dbo.VideoFrame.idx_VideoFrame_LUT
GO
DROP INDEX dbo.VideoFrame.idx_VideoFrame_FK1
GO
DROP INDEX dbo.VideoArchive.idx_VideoArchive_name
GO
DROP INDEX dbo.VideoArchive.idx_VideoArchive_id
GO
DROP INDEX dbo.VideoArchive.idx_VideoArchive_LUT
GO
DROP INDEX dbo.VideoArchive.idx_VideoArchive_FK1
GO
DROP INDEX dbo.VideoArchiveSet.idx_VideoArchiveSet_id
GO
DROP INDEX dbo.VideoArchiveSet.idx_VideoArchiveSet_LUT
GO
DROP INDEX dbo.PhysicalData.idx_PhysicalData_LUT
GO
DROP INDEX dbo.PhysicalData.idx_PhysicalData_FK1
GO
DROP INDEX dbo.Observation.idx_Observation_LUT
GO
DROP INDEX dbo.Observation.idx_Observation_FK1
GO
DROP INDEX dbo.Observation.idx_Observation_ConceptName
GO
DROP INDEX dbo.CameraPlatformDeployment.idx_CameraPlatformDeployment_LUT
GO
DROP INDEX dbo.CameraPlatformDeployment.idx_CameraDeployment_FK1
GO
DROP INDEX dbo.CameraData.idx_CameraData_LUT
GO
DROP INDEX dbo.CameraData.idx_CameraData_FK1
GO
DROP INDEX dbo.Association.idx_Association_ToConcept
GO
DROP INDEX dbo.Association.idx_Association_LUT
GO
DROP INDEX dbo.Association.idx_Association_FK1
GO
DROP VIEW dbo.Annotations
GO
DROP TABLE dbo.VideoFrame
GO
DROP TABLE dbo.VideoArchiveSet
GO
DROP TABLE dbo.VideoArchive
GO
DROP TABLE dbo.UniqueID
GO
DROP TABLE dbo.PhysicalData
GO
DROP TABLE dbo.Observation
GO
DROP TABLE dbo.EXPDMergeStatus
GO
DROP TABLE dbo.CameraPlatformDeployment
GO
DROP TABLE dbo.CameraData
GO
DROP TABLE dbo.Association
GO

CREATE TABLE dbo.Association  ( 
	id               	bigint NOT NULL,
	ObservationID_FK 	bigint NULL,
	LinkName         	varchar(50) NULL,
	ToConcept        	varchar(50) NULL,
	LinkValue        	varchar(100) NULL,
	LAST_UPDATED_TIME	datetime NULL,
	CONSTRAINT Association_PK PRIMARY KEY NONCLUSTERED(id)
)
GO
CREATE TABLE dbo.CameraData  ( 
	id               	bigint NOT NULL,
	VideoFrameID_FK  	bigint NULL,
	Name             	varchar(50) NULL,
	Direction        	varchar(50) NULL,
	Zoom             	int NULL,
	Focus            	int NULL,
	Iris             	int NULL,
	FieldWidth       	float NULL,
	StillImageURL    	varchar(1024) NULL,
	LogDTG           	datetime NULL,
	LAST_UPDATED_TIME	datetime NULL,
	X                	float NULL,
	Y                	float NULL,
	Z                	float NULL,
	PITCH            	float NULL,
	ROLL             	float NULL,
	XYUNITS          	varchar(50) NULL,
	ZUNITS           	varchar(50) NULL,
	HEADING          	float NULL,
	VIEWHEIGHT       	float NULL,
	VIEWWIDTH        	float NULL,
	VIEWUNITS        	varchar(50) NULL,
	CONSTRAINT CameraData_PK PRIMARY KEY(id)

WITH (ALLOW_ROW_LOCKS = OFF)
	)
GO
CREATE TABLE dbo.CameraPlatformDeployment  ( 
	id                  	bigint NOT NULL,
	VideoArchiveSetID_FK	bigint NULL,
	SeqNumber           	int NULL,
	ChiefScientist      	varchar(50) NULL,
	UsageStartDTG       	datetime NULL,
	UsageEndDTG         	datetime NULL,
	LAST_UPDATED_TIME   	datetime NULL,
	CONSTRAINT CameraDeployment_PK PRIMARY KEY NONCLUSTERED(id)
)
GO
CREATE TABLE dbo.EXPDMergeStatus  ( 
	VideoArchiveSetID_FK	bigint NOT NULL,
	MergeDate           	datetime NOT NULL,
	IsNavigationEdited  	smallint NOT NULL,
	StatusMessage       	varchar(512) NOT NULL,
	VideoFrameCount     	int NOT NULL,
	IsMerged            	smallint NOT NULL,
	DateSource          	varchar(4) NOT NULL,
	CONSTRAINT EXPDMergeStatus_PK PRIMARY KEY(VideoArchiveSetID_FK)
)
GO
CREATE TABLE dbo.Observation  ( 
	id               	bigint NOT NULL,
	VideoFrameID_FK  	bigint NULL,
	ObservationDTG   	datetime NULL,
	Observer         	varchar(50) NULL,
	ConceptName      	varchar(50) NULL,
	Notes            	varchar(200) NULL,
	LAST_UPDATED_TIME	datetime NULL,
	X                	float NULL,
	Y                	float NULL,
	CONSTRAINT Observation_PK PRIMARY KEY(id)
)
GO
CREATE TABLE dbo.PhysicalData  ( 
	id               	bigint NOT NULL,
	VideoFrameID_FK  	bigint NULL,
	Depth            	real NULL,
	Temperature      	real NULL,
	Salinity         	real NULL,
	Oxygen           	real NULL,
	Light            	real NULL,
	Latitude         	double NULL,
	Longitude        	double NULL,
	LogDTG           	datetime NULL,
	LAST_UPDATED_TIME	datetime NULL,
	CONSTRAINT PhysicalData_PK PRIMARY KEY NONCLUSTERED(id)
)
GO
CREATE TABLE dbo.UniqueID  ( 
	tablename	varchar(200) NOT NULL,
	nextid   	bigint NULL 
	)
GO
CREATE TABLE dbo.VideoArchive  ( 
	id                  	bigint NOT NULL,
	VideoArchiveSetID_FK	bigint NULL,
	TapeNumber          	smallint NULL,
	StartTimeCode       	varchar(11) NULL,
	videoArchiveName    	varchar(1024) NULL,
	LAST_UPDATED_TIME   	datetime NULL,
	CONSTRAINT VideoArchive_PK PRIMARY KEY(id)
)
GO
CREATE TABLE dbo.VideoArchiveSet  ( 
	id               	bigint NOT NULL,
	TrackingNumber   	varchar(7) NULL,
	ShipName         	varchar(32) NULL,
	PlatformName     	varchar(32) NULL,
	FormatCode       	varchar(2) NULL,
	StartDTG         	datetime NULL,
	EndDTG           	datetime NULL,
	LAST_UPDATED_TIME	datetime NULL,
	CONSTRAINT VideoArchiveSet_PK PRIMARY KEY(id)
)
GO
CREATE TABLE dbo.VideoFrame  ( 
	id               	bigint NOT NULL,
	RecordedDtg      	datetime NULL,
	TapeTimeCode     	varchar(11) NULL,
	HDTimeCode       	varchar(11) NULL,
	InSequence       	bit NULL,
	Displacer        	varchar(50) NULL,
	DisplaceDtg      	datetime NULL,
	VideoArchiveID_FK	bigint NULL,
	LAST_UPDATED_TIME	datetime NULL,
	CONSTRAINT VideoFrame_PK PRIMARY KEY(id)
)
GO
CREATE VIEW dbo.Annotations
AS 
SELECT TOP 100 PERCENT 
    obs.ObservationDTG AS ObservationDate,
    obs.Observer,
    obs.ConceptName,
    obs.Notes,
    obs.X AS XPixelInImage,
    obs.Y AS yPixelInImage,
    vf.TapeTimeCode,
    vf.RecordedDtg AS RecordedDate ,
    va.videoArchiveName,
    vas.TrackingNumber,
    vas.ShipName,
    vas.PlatformName AS RovName,
    vas.FormatCode AS AnnotationMode,
    cpd.SeqNumber AS DiveNumber,
    cpd.ChiefScientist,
    cd.NAME AS CameraName,
    cd.Direction AS CameraDirection,
    cd.Zoom,
    cd.Focus,
    cd.Iris,
    cd.FieldWidth,
    cd.StillImageURL AS Image,
    cd.X AS CameraX,
    cd.Y AS CameraY,
    cd.Z AS CameraZ,
    cd.Pitch AS CameraPitchRadians,
    cd.Roll AS CameraRollRadians,
    cd.Heading AS CameraHeadingRadians,
    cd.XYUnits AS CameraXYUnits,
    cd.ZUnits AS CameraZUnits,
    cd.VIEWWIDTH as CameraViewWidth,
    cd.ViewHeight AS CameraViewHeight,
    cd.ViewUnits AS CameraViewUnits,
    pd.DEPTH,
    pd.Temperature,
    pd.Salinity,
    pd.Oxygen,
    pd.Light,
    pd.Latitude,
    pd.Longitude,
    obs.id AS ObservationID_FK,
    ass.id AS AssociationID_FK,
    ass.LinkName,
    ass.ToConcept,
    ass.LinkValue,
    ass.LinkName + ' | ' + ass.ToConcept + ' | ' + ass.LinkValue AS Associations,
    vf.HDTimeCode AS HighdefTimeCode,
    expd.IsNavigationEdited,
    vf.id AS VideoFrameID_FK,
    pd.id AS PhysicalDataID_FK,
    cd.id AS CameraDataID_FK,
    va.id AS VideoArchiveID_FK,
    vas.id AS VideoArchiveSetID_FK 
FROM 
    VideoArchiveSet vas 
        LEFT OUTER JOIN CameraPlatformDeployment cpd 
        ON cpd.VideoArchiveSetID_FK = vas.id 
        LEFT OUTER JOIN VideoArchive va 
        ON vas.id = va.VideoArchiveSetID_FK 
        LEFT OUTER JOIN VideoFrame vf 
        ON va.id = vf.VideoArchiveID_FK 
        LEFT OUTER JOIN CameraData cd 
        ON cd.VideoFrameID_FK = vf.id 
        LEFT OUTER JOIN PhysicalData pd 
        ON pd.VideoFrameID_FK = vf.id 
        LEFT OUTER JOIN Observation obs 
        ON obs.VideoFrameID_FK = vf.id 
        LEFT OUTER JOIN Association ass 
        ON ass.ObservationID_FK = obs.id 
        LEFT OUTER JOIN EXPDMergeStatus expd 
        ON expd.VideoArchiveSetID_FK = vas.id
GO
CREATE INDEX idx_Association_FK1
	ON dbo.Association(ObservationID_FK)
GO
CREATE INDEX idx_Association_LUT
	ON dbo.Association(LAST_UPDATED_TIME)
GO
CREATE INDEX idx_Association_ToConcept
	ON dbo.Association(ToConcept)
GO
CREATE INDEX idx_CameraData_FK1
	ON dbo.CameraData(VideoFrameID_FK)
GO
CREATE INDEX idx_CameraData_LUT
	ON dbo.CameraData(LAST_UPDATED_TIME)
GO
CREATE INDEX idx_CameraDeployment_FK1
	ON dbo.CameraPlatformDeployment(VideoArchiveSetID_FK)
GO
CREATE INDEX idx_CameraPlatformDeployment_LUT
	ON dbo.CameraPlatformDeployment(LAST_UPDATED_TIME)
GO
CREATE INDEX idx_Observation_ConceptName
	ON dbo.Observation(ConceptName)
GO
CREATE INDEX idx_Observation_FK1
	ON dbo.Observation(VideoFrameID_FK)
GO
CREATE INDEX idx_Observation_LUT
	ON dbo.Observation(LAST_UPDATED_TIME)
GO
CREATE INDEX idx_PhysicalData_FK1
	ON dbo.PhysicalData(VideoFrameID_FK)
GO
CREATE INDEX idx_PhysicalData_LUT
	ON dbo.PhysicalData(LAST_UPDATED_TIME)
GO
CREATE INDEX idx_VideoArchiveSet_LUT
	ON dbo.VideoArchiveSet(LAST_UPDATED_TIME)
GO
CREATE UNIQUE INDEX idx_VideoArchiveSet_id
	ON dbo.VideoArchiveSet(id)
GO
CREATE INDEX idx_VideoArchive_FK1
	ON dbo.VideoArchive(VideoArchiveSetID_FK)
GO
CREATE INDEX idx_VideoArchive_LUT
	ON dbo.VideoArchive(LAST_UPDATED_TIME)
GO
CREATE UNIQUE INDEX idx_VideoArchive_id
	ON dbo.VideoArchive(id)
GO
CREATE INDEX idx_VideoArchive_name
	ON dbo.VideoArchive(videoArchiveName)
GO
CREATE INDEX idx_VideoFrame_FK1
	ON dbo.VideoFrame(VideoArchiveID_FK)
GO
CREATE INDEX idx_VideoFrame_LUT
	ON dbo.VideoFrame(LAST_UPDATED_TIME)
GO

ALTER TABLE dbo.CameraData
	ADD CONSTRAINT uc_VideoFrameID_FK
	UNIQUE (VideoFrameID_FK)
GO
ALTER TABLE dbo.PhysicalData
	ADD CONSTRAINT uc_PhysicalData_FK1
	UNIQUE CLUSTERED (VideoFrameID_FK)
GO
ALTER TABLE dbo.VideoArchive
	ADD CONSTRAINT uc_VideoArchiveName
	UNIQUE (videoArchiveName)
GO
ALTER TABLE dbo.Association
	ADD CONSTRAINT Association_FK1
	FOREIGN KEY(ObservationID_FK)
	REFERENCES dbo.Observation(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
	NOT FOR REPLICATION 
GO
ALTER TABLE dbo.CameraData
	ADD CONSTRAINT CameraData_FK1
	FOREIGN KEY(VideoFrameID_FK)
	REFERENCES dbo.VideoFrame(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
	NOT FOR REPLICATION 
GO
ALTER TABLE dbo.CameraPlatformDeployment
	ADD CONSTRAINT CameraDeployment_FK1
	FOREIGN KEY(VideoArchiveSetID_FK)
	REFERENCES dbo.VideoArchiveSet(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
GO
ALTER TABLE dbo.Observation
	ADD CONSTRAINT Observation_FK1
	FOREIGN KEY(VideoFrameID_FK)
	REFERENCES dbo.VideoFrame(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
	NOT FOR REPLICATION 
GO
ALTER TABLE dbo.PhysicalData
	ADD CONSTRAINT PhysicalData_FK1
	FOREIGN KEY(VideoFrameID_FK)
	REFERENCES dbo.VideoFrame(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
	NOT FOR REPLICATION 
GO
ALTER TABLE dbo.VideoArchive
	ADD CONSTRAINT VideoArchive_FK1
	FOREIGN KEY(VideoArchiveSetID_FK)
	REFERENCES dbo.VideoArchiveSet(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
	NOT FOR REPLICATION 
GO
ALTER TABLE dbo.VideoFrame
	ADD CONSTRAINT VideoFrame_FK1
	FOREIGN KEY(VideoArchiveID_FK)
	REFERENCES dbo.VideoArchive(id)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION 
	NOT FOR REPLICATION 
GO