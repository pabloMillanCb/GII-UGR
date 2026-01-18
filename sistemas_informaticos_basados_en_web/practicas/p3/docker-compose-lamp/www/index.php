<?php
  require_once "/usr/local/lib/php/vendor/autoload.php";
  include("bd.php");

  $loader = new \Twig\Loader\FilesystemLoader('templates');
  $twig = new \Twig\Environment($loader);

  $nombreCientifico = "Cientifico1";
  $salida = array('nombre' => "Cientifico100");

  $cientificos = getAllCientificos();
  
  echo $twig->render('portada.html', ['cientificos'=>$cientificos]);
?>
