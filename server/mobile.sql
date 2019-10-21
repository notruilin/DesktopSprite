-- MySQL Script generated by MySQL Workbench
-- Sat Oct 19 13:04:38 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mobile
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mobile` ;

-- -----------------------------------------------------
-- Schema mobile
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mobile` DEFAULT CHARACTER SET utf8 ;
USE `mobile` ;

-- -----------------------------------------------------
-- Table `mobile`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mobile`.`user` ;

CREATE TABLE IF NOT EXISTS `mobile`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  `nick_name` VARCHAR(45) NULL,
  `pswd` VARCHAR(45) NULL,
  `phone` VARCHAR(20) NULL,
  `email` VARCHAR(45) NULL,
  `register_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mobile`.`clock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mobile`.`clock` ;

CREATE TABLE IF NOT EXISTS `mobile`.`clock` (
  `id` INT NOT NULL,
  `uid` INT NOT NULL,
  `time` TIME NULL,
  `repeat` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `uid_idx` (`uid` ASC) ,
  CONSTRAINT `idclock`
    FOREIGN KEY (`uid`)
    REFERENCES `mobile`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mobile`.`record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mobile`.`record` ;

CREATE TABLE IF NOT EXISTS `mobile`.`record` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uid` INT NULL,
  `steps` INT NULL,
  `time` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `user_idx` (`uid` ASC) ,
  CONSTRAINT `idrecord`
    FOREIGN KEY (`uid`)
    REFERENCES `mobile`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mobile`.`pet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mobile`.`pet` ;

CREATE TABLE IF NOT EXISTS `mobile`.`pet` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uid` INT NULL,
  `nickname` VARCHAR(45) NULL,
  `life` DECIMAL(5,2) NULL DEFAULT 100,
  `starve` DECIMAL(5,2) NULL DEFAULT 100,
  `clean` DECIMAL(5,2) NULL DEFAULT 100,
  PRIMARY KEY (`id`),
  INDEX `user_idx` (`uid` ASC) ,
  CONSTRAINT `idpet`
    FOREIGN KEY (`uid`)
    REFERENCES `mobile`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;