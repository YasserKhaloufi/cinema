-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Apr 17, 2024 alle 20:53
-- Versione del server: 10.4.32-MariaDB
-- Versione PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_cinema_2024`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `film`
--

CREATE TABLE `film` (
  `CodFilm` int(32) NOT NULL,
  `Titolo` varchar(32) NOT NULL,
  `AnnoProduzione` year(4) NOT NULL,
  `Nazionalità` char(3) NOT NULL,
  `Regista` varchar(32) NOT NULL,
  `Genere` varchar(32) NOT NULL,
  `Durata` time NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `film`
--

INSERT INTO `film` (`CodFilm`, `Titolo`, `AnnoProduzione`, `Nazionalità`, `Regista`, `Genere`, `Durata`, `Immagine`) VALUES
(5, 'Sonic', '2020', 'USA', 'Jeff Fowler', 'Azione', '00:01:20', 'locandina.jpg'),
(6, 'Jurassic World - Il Dominio', '2022', 'USA', 'Colin Trevorrow', 'Azione', '00:01:30', 'locandinapg2.jpg'),
(7, 'Godzilla Minus One', '2023', 'USA', 'Takashi Yamazaki', 'Azione', '00:01:40', 'locandina (1).jpg'),
(8, 'Dragon Ball Super: Broly', '2019', 'Gia', 'Tatsuya Nagamine', 'Azione', '00:02:10', 'dragonballsuper-broly-1200px-1.jpeg'),
(9, 'Spider-Man: Across the Spider-Ve', '2023', 'USA', 'Joaquim Dos Santos', 'Drammatico', '00:02:10', 'unnamed.png'),
(12, 'Una notte da leoni', '2009', 'USA', 'Todd Phillips', 'Commedia', '00:01:20', 'una_notte_da_leoni.jpg'),
(13, 'Fast & Furious 6', '2013', 'USA', 'Justin Lin', 'Corsa', '00:02:10', 'image.jpg');

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE `utenti` (
  `ID` int(11) NOT NULL,
  `Username` varchar(32) NOT NULL,
  `Password` char(32) NOT NULL,
  `Admin` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`ID`, `Username`, `Password`, `Admin`) VALUES
(1, 'Yasser', '21232f297a57a5a743894a0e4a801fc3', 1),
(2, 'Test', '3a8f84f28514a9f0c6d9ce7a539cce23', 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `film`
--
ALTER TABLE `film`
  ADD PRIMARY KEY (`CodFilm`);

--
-- Indici per le tabelle `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `film`
--
ALTER TABLE `film`
  MODIFY `CodFilm` int(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT per la tabella `utenti`
--
ALTER TABLE `utenti`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
