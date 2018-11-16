CREATE DATABASE `test` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `students` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nameStd` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `groupNum` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;


CREATE TABLE `testResults` (
  `student_id` int(11) NOT NULL,
  `test_name` varchar(140) NOT NULL,
  `result` varchar(40) NOT NULL,
  KEY `student_id_idx` (`student_id`),
  CONSTRAINT `student_id` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;