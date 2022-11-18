-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema database1
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema database1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `database1` ;

-- -----------------------------------------------------
-- Table `database1`.`STUDENTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `database1`.`STUDENTS` (
  `STUDENTNAME` VARCHAR(45) NOT NULL,
  `SUBJECT` VARCHAR(45) NOT NULL,
  `ASSIGNMENTCATEGORY` VARCHAR(45) NOT NULL,
  `DATE` VARCHAR(45) NULL DEFAULT 0,
  `MARKS` INT NULL,
  PRIMARY KEY (`STUDENTNAME`, `SUBJECT`, `ASSIGNMENTCATEGORY`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database1`.`LAB`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `database1`.`LAB` (
  `STUDENTNAME` VARCHAR(45) NOT NULL,
  `SUBJECT` VARCHAR(45) NOT NULL,
  `ASSIGNMENTCATEGORY` VARCHAR(45) NOT NULL,
  `SEQUENCE` INT NULL DEFAULT 1,
  `MARKS` INT NULL DEFAULT 0,
  PRIMARY KEY (`STUDENTNAME`, `SUBJECT`),
  CONSTRAINT `fk_LAB_STUDENTS1`
    FOREIGN KEY (`STUDENTNAME` , `SUBJECT`)
    REFERENCES `database1`.`STUDENTS` (`STUDENTNAME` , `SUBJECT`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database1`.`PROJECT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `database1`.`PROJECT` (
  `STUDENTNAME` VARCHAR(45) NOT NULL,
  `SUBJECT` VARCHAR(45) NOT NULL,
  `ASSIGNMENTCATEGORY` VARCHAR(45) NOT NULL,
  `SEQUENCE` INT NULL DEFAULT 1,
  `MARKS` INT NULL DEFAULT 0,
  PRIMARY KEY (`STUDENTNAME`, `SUBJECT`),
  CONSTRAINT `fk_PROJECT_STUDENTS1`
    FOREIGN KEY (`STUDENTNAME` , `SUBJECT`)
    REFERENCES `database1`.`STUDENTS` (`STUDENTNAME` , `SUBJECT`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database1`.`TEST`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `database1`.`TEST` (
  `STUDENTNAME` VARCHAR(45) NOT NULL,
  `SUBJECT` VARCHAR(45) NOT NULL,
  `ASSIGNMENTCATEGORY` VARCHAR(45) NOT NULL,
  `SEQUENCE` INT NULL DEFAULT 1,
  `MARKS` INT NULL DEFAULT 0,
  PRIMARY KEY (`STUDENTNAME`, `SUBJECT`),
  CONSTRAINT `fk_TEST_STUDENTS1`
    FOREIGN KEY (`STUDENTNAME` , `SUBJECT`)
    REFERENCES `database1`.`STUDENTS` (`STUDENTNAME` , `SUBJECT`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database1`.`QUIZ`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `database1`.`QUIZ` (
  `STUDENTNAME` VARCHAR(45) NOT NULL,
  `SUBJECT` VARCHAR(45) NOT NULL,
  `ASSIGNMENTCATEGORY` VARCHAR(45) NOT NULL,
  `SEQUENCE` INT NULL DEFAULT 1,
  `MARKS` INT NULL DEFAULT 0,
  PRIMARY KEY (`STUDENTNAME`, `SUBJECT`),
  CONSTRAINT `fk_QUIZ_STUDENTS1`
    FOREIGN KEY (`STUDENTNAME` , `SUBJECT`)
    REFERENCES `database1`.`STUDENTS` (`STUDENTNAME` , `SUBJECT`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `database1`.`OVERALLRATING`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `database1`.`OVERALLRATING` (
  `STUDENTNAME` VARCHAR(45) NOT NULL,
  `SUBJECT` VARCHAR(45) NOT NULL,
  `TESTSCORE` DOUBLE NULL DEFAULT 0,
  `QUIZSCORE` DOUBLE NULL DEFAULT 0,
  `LABSCORE` DOUBLE NULL DEFAULT 0,
  `PROJECTSCORE` DOUBLE NULL DEFAULT 0,
  `OVERALLSCORE` DOUBLE NULL DEFAULT 0,
  PRIMARY KEY (`STUDENTNAME`, `SUBJECT`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
