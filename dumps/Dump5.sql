CREATE DATABASE  IF NOT EXISTS `movies` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `movies`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: movies
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apartment_num` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `street_address` varchar(255) NOT NULL,
  `zip_code` int NOT NULL,
  `state_code` enum('AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` int NOT NULL,
  `bookingNum` int DEFAULT NULL,
  `customerId` int DEFAULT NULL,
  `showId` int DEFAULT NULL,
  `promoId` int DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customerId` (`customerId`),
  KEY `showId` (`showId`),
  KEY `promoId` (`promoId`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `users` (`id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`showId`) REFERENCES `showtimes` (`id`),
  CONSTRAINT `bookings_ibfk_3` FOREIGN KEY (`promoId`) REFERENCES `promotions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cast` varchar(255) DEFAULT NULL,
  `director` varchar(255) DEFAULT NULL,
  `movie_title` varchar(255) DEFAULT NULL,
  `producer` varchar(255) DEFAULT NULL,
  `rating` varchar(255) DEFAULT NULL,
  `reviews` varchar(255) DEFAULT NULL,
  `synop` varchar(2000) DEFAULT NULL,
  `trailer_photo` varchar(255) DEFAULT NULL,
  `trailer_video` varchar(255) DEFAULT NULL,
  `now_playing` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `full_text_search_idx` (`movie_title`,`now_playing`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'Jaoquin Phoenix, Lady Gaga','Todd Phillips','Joker: Folie à Deux','Bradley Cooper','R','https://www.rottentomatoes.com/m/joker_folie_a_deux','Joker: Folie à Deux is an upcoming American musical psychological thriller film slated to be a sequel to the 2019 film Joker.','https://static0.cbrimages.com/wordpress/wp-content/uploads/2024/01/joker-folie-a-deux-2024.jpg','https://www.youtube.com/embed/03ymBj144ng?si=776k2iZ-34FMkE3e','Now Showing'),(2,'Kingsley Ben-Adir, Lashana Lynch, Michael Gandolfini','Reinaldo Marcus Green','Bob Marley: One Love','Ziggy Marley','PG-13','https://www.rottentomatoes.com/m/bob_marley_one_love','Bob Marley: One Love is a 2024 biographical music-drama film directed by Reinaldo Marcus Green. The film covers three years of Bob Marley\'s life, from 1976 to 1978, and includes archive footage. Kingsley Ben-Adir portrays Marley, and Lashana Lynch plays his wife, Rita',NULL,'https://www.youtube.com/embed/ajw425Kuvtw?si=9FG_SeN3pix5xv2_','Now Showing'),(3,'Lea Seydoux, George MacKay','Bertrand Bonello','La Bête','Xavier Dolan','PG-13','https://www.rottentomatoes.com/m/the_beast_2023','In the near future where emotions have become a threat, Gabrielle finally decides to purify her DNA in a machine that will immerse her in her previous lives and rid her of any strong feelings. She then meets Louis and feels a powerful connection, as if she has known him forever.',NULL,'https://www.youtube.com/embed/SYDxOkp19kQ?si=Gop-0I1YLrVjKIyO','Coming Soon'),(4,'Adele Exarchopoulous, Francois Civil','Gilles Lellouche','L\'Amour Ouf','Alain Attal','PG-13','https://www.imdb.com/title/tt27490099/fullcredits','Beating Hearts is a 2024 French movie directed by Gilles Lellouche. It is about a rebellious teenager named Clotaire and his classmate Jackie whose bond is tested when Clotaire gets caught up in gang violence and ends up in prison on trumped-up murder charges.',NULL,'https://www.youtube.com/embed/93JHxbzzzjU?si=B-5TVmoY9PDlOUzj','Coming Soon');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_info`
--

DROP TABLE IF EXISTS `payment_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `card_name` varchar(255) NOT NULL,
  `card_num` varchar(255) NOT NULL,
  `exp_date` varchar(255) NOT NULL,
  `sec_code` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `billing_addr_state` varchar(255) NOT NULL,
  `billing_addr_street` varchar(255) NOT NULL,
  `billing_addr_zip` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_info`
--

LOCK TABLES `payment_info` WRITE;
/*!40000 ALTER TABLE `payment_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `id` int NOT NULL,
  `promoCode` varchar(255) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `percentage` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` int NOT NULL,
  `numSeats` int DEFAULT NULL,
  `roomTitle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,30,'Theater One'),(2,28,'Theater Two'),(3,32,'Theater Three'),(4,35,'Theater Four'),(5,25,'Theater Five'),(6,20,'Theater Seven');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seats`
--

DROP TABLE IF EXISTS `seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seats` (
  `id` int NOT NULL,
  `showId` int DEFAULT NULL,
  `seatStatus` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `showId` (`showId`),
  CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`showId`) REFERENCES `showtimes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seats`
--

LOCK TABLES `seats` WRITE;
/*!40000 ALTER TABLE `seats` DISABLE KEYS */;
/*!40000 ALTER TABLE `seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `showtimes`
--

DROP TABLE IF EXISTS `showtimes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `showtimes` (
  `id` int NOT NULL,
  `movieId` int DEFAULT NULL,
  `roomId` int DEFAULT NULL,
  `period` varchar(255) DEFAULT NULL,
  `showDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `movieId` (`movieId`),
  KEY `roomId` (`roomId`),
  CONSTRAINT `showtimes_ibfk_1` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`),
  CONSTRAINT `showtimes_ibfk_2` FOREIGN KEY (`roomId`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `showtimes`
--

LOCK TABLES `showtimes` WRITE;
/*!40000 ALTER TABLE `showtimes` DISABLE KEYS */;
/*!40000 ALTER TABLE `showtimes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `id` int NOT NULL,
  `bookingId` int DEFAULT NULL,
  `seatId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bookingId` (`bookingId`),
  KEY `seatId` (`seatId`),
  CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`id`),
  CONSTRAINT `tickets_ibfk_2` FOREIGN KEY (`seatId`) REFERENCES `seats` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_status`
--

DROP TABLE IF EXISTS `user_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_status` (
  `id` int NOT NULL,
  `userStatus` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_status`
--

LOCK TABLES `user_status` WRITE;
/*!40000 ALTER TABLE `user_status` DISABLE KEYS */;
INSERT INTO `user_status` VALUES (1,'active'),(2,'inactive'),(3,'suspended');
/*!40000 ALTER TABLE `user_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_types`
--

DROP TABLE IF EXISTS `user_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_types` (
  `id` int NOT NULL,
  `userType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_types`
--

LOCK TABLES `user_types` WRITE;
/*!40000 ALTER TABLE `user_types` DISABLE KEYS */;
INSERT INTO `user_types` VALUES (1,'customer'),(2,'admin');
/*!40000 ALTER TABLE `user_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userStatus` int DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `verCode` varchar(255) DEFAULT NULL,
  `subscription` bit(1) NOT NULL,
  `user_type` tinyint NOT NULL,
  `address_id` bigint DEFAULT NULL,
  `user_password` varchar(255) NOT NULL,
  `ver_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `UK_hbvhqvjgmhd5omxyo67ynvbyp` (`address_id`),
  CONSTRAINT `FKditu6lr4ek16tkxtdsne0gxib` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `users_chk_1` CHECK ((`user_type` between 0 and 1))
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'movies'
--

--
-- Dumping routines for database 'movies'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-15  2:14:37
