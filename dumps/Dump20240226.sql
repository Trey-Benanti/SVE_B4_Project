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
  `now_playing` int NOT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `full_text_search_idx` (`movie_title`,`cast`,`director`,`producer`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'Jaoquin Phoenix, Lady Gaga','Todd Phillips','Joker: Folie à Deux','Bradley Cooper','R','https://www.rottentomatoes.com/m/joker_folie_a_deux','Joker: Folie à Deux is an upcoming American musical psychological thriller film slated to be a sequel to the 2019 film Joker.','https://static0.cbrimages.com/wordpress/wp-content/uploads/2024/01/joker-folie-a-deux-2024.jpg','https://www.youtube.com/embed/03ymBj144ng?si=776k2iZ-34FMkE3e',0),(2,'Kingsley Ben-Adir, Lashana Lynch, Michael Gandolfini','Reinaldo Marcus Green','Bob Marley: One Love','Ziggy Marley','PG-13','https://www.rottentomatoes.com/m/bob_marley_one_love','Bob Marley: One Love is a 2024 biographical music-drama film directed by Reinaldo Marcus Green. The film covers three years of Bob Marley\'s life, from 1976 to 1978, and includes archive footage. Kingsley Ben-Adir portrays Marley, and Lashana Lynch plays his wife, Rita',NULL,'https://www.youtube.com/embed/ajw425Kuvtw?si=9FG_SeN3pix5xv2_',1),(3,'Lea Seydoux, George MacKay','Bertrand Bonello','La Bête','Xavier Dolan','PG-13','https://www.rottentomatoes.com/m/the_beast_2023','In the near future where emotions have become a threat, Gabrielle finally decides to purify her DNA in a machine that will immerse her in her previous lives and rid her of any strong feelings. She then meets Louis and feels a powerful connection, as if she has known him forever.',NULL,'https://www.youtube.com/embed/SYDxOkp19kQ?si=Gop-0I1YLrVjKIyO',1),(4,'Adele Exarchopoulous, Francois Civil','Gilles Lellouche','L\'Amour Ouf','Alain Attal','PG-13','https://www.imdb.com/title/tt27490099/fullcredits','Beating Hearts is a 2024 French movie directed by Gilles Lellouche. It is about a rebellious teenager named Clotaire and his classmate Jackie whose bond is tested when Clotaire gets caught up in gang violence and ends up in prison on trumped-up murder charges.',NULL,'https://www.youtube.com/embed/93JHxbzzzjU?si=B-5TVmoY9PDlOUzj',0);
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-26  3:37:42
