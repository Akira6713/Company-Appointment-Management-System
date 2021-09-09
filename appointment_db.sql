DROP DATABASE IF EXISTS `appointment_db`;
CREATE DATABASE `appointment_db`;
USE `appointment_db`;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
CREATE TABLE `appointments` (
  `appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50)  NOT NULL,
  `description` varchar(50) NOT NULL,
  `Location` varchar(50) NOT NULL,
  `Type` varchar(50) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `last_update` timestamp NOT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  `customer_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `contact_id` int(11) NOT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `APPOINTMENT_TYPE` (`Type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
INSERT INTO `appointments` VALUES (1,'title','description','location','Planning Session','2020-05-28 12:00:00','2020-05-28 13:00:00','2021-02-23 12:36:18','script','2021-02-23 07:06:18','script',1,1,3),(2,'title','description','location','De-Briefing','2020-05-29 12:00:00','2020-05-29 13:00:00','2021-02-23 12:36:19','script','2021-02-23 07:06:19','script',2,2,2);
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;

CREATE TABLE `contacts` (
  `contact_id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(50)  NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
INSERT INTO `contacts` VALUES (1,'Anika Costa','acoasta@company.com'),(2,'Daniel Garcia','dgarcia@company.com'),(3,'Li Lee','llee@company.com');
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
CREATE TABLE `countries` (
  `country_id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `last_update` timestamp NOT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
INSERT INTO `countries` VALUES (1,'U.S','2021-02-22 07:27:47','script','2021-02-22 01:57:47','script'),(2,'UK','2021-02-22 07:27:47','script','2021-02-22 01:57:47','script'),(3,'Canada','2021-02-22 07:27:47','script','2021-02-22 01:57:47','script');
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `postal_code` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `last_update` timestamp NOT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  `division_id` int(11) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
INSERT INTO `customers` VALUES (1,'Daddy Warbucks','1919 Boardwalk','01291','869-908-1875','2021-02-23 12:36:18','script','2021-02-23 07:06:18','script',29),(2,'Lady McAnderson','2 Wonder Way','AF19B','11-445-910-2135','2021-02-23 12:36:18','script','2021-02-23 07:06:18','script',103),(3,'Dudley Do-Right','48 Horse Manor ','28198','874-916-2671','2021-02-23 12:36:18','script','2021-02-23 07:06:18','script',60);
UNLOCK TABLES;

--
-- Table structure for table `first_level_divisions`
--

DROP TABLE IF EXISTS `first_level_divisions`;
CREATE TABLE `first_level_divisions` (
  `division_id` int(11) NOT NULL AUTO_INCREMENT,
  `division` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `last_update` timestamp NOT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  `country_id` int(11) NOT NULL,
  PRIMARY KEY (`division_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `first_level_divisions`
--

LOCK TABLES `first_level_divisions` WRITE;
INSERT INTO `first_level_divisions` VALUES (1,'Alabama','2021-02-23 12:35:49','script','2021-02-23 07:05:49','script',1),(2,'Arizona','2021-02-23 12:35:49','script','2021-02-23 07:05:49','script',1),(3,'Arkansas','2021-02-23 12:35:49','script','2021-02-23 07:05:49','script',1),(4,'California','2021-02-23 12:35:49','script','2021-02-23 07:05:49','script',1),(5,'Colorado','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(6,'Connecticut','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(7,'Delaware','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(8,'District of Columbia','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(9,'Florida','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(10,'Georgia','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(11,'Idaho','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(12,'Illinois','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(13,'Indiana','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(14,'Iowa','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(15,'Kansas','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(16,'Kentucky','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(17,'Louisiana','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(18,'Maine','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(19,'Maryland','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(20,'Massachusetts','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(21,'Michigan','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(22,'Minnesota','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(23,'Mississippi','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(24,'Missouri','2021-02-23 12:35:51','script','2021-02-23 07:05:51','script',1),(25,'Montana','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(26,'Nebraska','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(27,'Nevada','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(28,'New Hampshire','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(29,'New Jersey','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(30,'New Mexico','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(31,'New York','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(32,'North Carolina','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(33,'North Dakota','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(34,'Ohio','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(35,'Oklahoma','2021-02-23 12:35:52','script','2021-02-23 07:05:52','script',1),(36,'Oregon','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(37,'Pennsylvania','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(38,'Rhode Island','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(39,'South Carolina','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(40,'South Dakota','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(41,'Tennessee','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(42,'Texas','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(43,'Utah','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(44,'Vermont','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(45,'Virginia','2021-02-23 12:35:53','script','2021-02-23 07:05:53','script',1),(46,'Washington','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',1),(47,'West Virginia','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',1),(48,'Wisconsin','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',1),(49,'Wyoming','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',1),(52,'Hawaii','2021-02-23 12:35:50','script','2021-02-23 07:05:50','script',1),(54,'Alaska','2021-02-23 12:35:49','script','2021-02-23 07:05:49','script',1),(60,'Northwest Territories','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(61,'Alberta','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(62,'British Columbia','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(63,'Manitoba','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(64,'New Brunswick','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(65,'Nova Scotia','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(66,'Prince Edward Island','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',3),(67,'Ontario','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',3),(68,'Québec','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',3),(69,'Saskatchewan','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',3),(70,'Nunavut','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',3),(71,'Yukon','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',3),(72,'Newfoundland and Labrador','2021-02-23 12:35:54','script','2021-02-23 07:05:54','script',3),(101,'England','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',2),(102,'Wales','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',2),(103,'Scotland','2021-02-23 12:35:55','script','2021-02-23 07:05:55','script',2),(104,'Northern Ireland','2021-02-23 12:35:56','script','2021-02-23 07:05:56','script',2);
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50)  NOT NULL,
  `password` varchar(50)  NOT NULL,
  `create_date` datetime NOT NULL,
  `created_by` varchar(50)  DEFAULT NULL,
  `last_update` timestamp NOT NULL,
  `last_updated_by` varchar(50)  DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (1,'test','test','2021-02-22 07:27:46','script','2021-02-22 01:57:46','script'),(2,'admin','admin','2021-02-22 07:27:46','script','2021-02-22 01:57:46','script');
UNLOCK TABLES;

