<?php
  require_once "/usr/local/lib/php/vendor/autoload.php";
  include("bd.php");

  $loader = new \Twig\Loader\FilesystemLoader('templates');
  $twig = new \Twig\Environment($loader);

  if (isset($_GET['ct'])) {
    $idCt = $_GET['ct'];
  } else {
    $idCt = -1;
  }

  $cientifico = getCientifico($idCt);

  if ($cientifico['nombre'] === 'XXX') {
    echo $twig->render('not_found.html');
  }
  else {

    $comentarios = getAllComentarios($idCt);

    echo $twig->render('cientifico.html', 
    ['id' => $cientifico['id'],
    'nombre' => $cientifico['nombre'],
    'fecha' => $cientifico['fecha'],
    'pais' => $cientifico['origen'],
    'texto' => $cientifico['texto'],
    'foto1' => $cientifico['foto1'],
    'foto2' => $cientifico['foto2'],
    'pie1' => $cientifico['pie1'],
    'pie2' => $cientifico['pie2'],
    'comentarios' => $comentarios
    ]);
  }
?>
