<?php
  function getCientifico($idEv) {
  
    $mysqli = new mysqli("database", "pablo", "tiger", "SIBW");
    if ($mysqli->connect_errno) {
      echo ("Fallo al conectar: " . $mysqli->connect_error);
    }

    $cientifico = array(
      'id' => 'XXX',
      'nombre' => 'XXX',
      'fecha' => 'XXX',
      'origen' => 'XXX',
      'texto' => 'XXX',
      'foto1' => 'XXX',
      'foto2' => 'XXX',
      'pie1' => 'XXX',
      'pie2' => 'XXX'
    );

    //$idEv = $mysqli->real_escape_string($idEv);

    $res = $mysqli->query("SELECT id, nombre, fecha, origen, texto, foto1, foto2, pie1, pie2 FROM cientificos WHERE id=" . (int)$idEv);

    if ($res->num_rows > 0) {
      $row = $res->fetch_assoc();
      
      $cientifico = array(
      'id' => $row['id'], 
      'nombre' => $row['nombre'], 
      'fecha' => $row['fecha'],
      'origen' => $row['origen'],
      'texto' => $row['texto'],
      'foto1' => $row['foto1'],
      'foto2' => $row['foto2'],
      'pie1' => $row['pie1'],
      'pie2' => $row['pie2']
      );
    }
    
    return $cientifico;
  }

  function getAllCientificos(){

    $mysqli = new mysqli("database", "pablo", "tiger", "SIBW");
    if ($mysqli->connect_errno) {
      echo ("Fallo al conectar: " . $mysqli->connect_error);
    }

    $res = $mysqli->query("SELECT id, nombre, fecha, origen, texto, foto1, foto2, pie1, pie2 FROM cientificos");

    $cientificos = array();

    for ($i = 0; $i < $res->num_rows; $i++) {
      $res->data_seek($i);
      $row = $res->fetch_assoc();

      $cientificos[$i] = array(
        'id' => $row['id'], 
        'nombre' => $row['nombre'], 
        'fecha' => $row['fecha'],
        'origen' => $row['origen'],
        'texto' => $row['texto'],
        'foto1' => $row['foto1'],
        'foto2' => $row['foto2'],
        'pie1' => $row['pie1'],
        'pie2' => $row['pie2']
        );
    }

    return $cientificos;
  }

  function getAllComentarios($idCientifico) {

    $mysqli = new mysqli("database", "pablo", "tiger", "SIBW");
    if ($mysqli->connect_errno) {
      echo ("Fallo al conectar: " . $mysqli->connect_error);
    }

    $res = $mysqli->query("SELECT id_comentario, nombre, fecha, texto FROM comentarios WHERE id_cientifico=" . $idCientifico);

    $comentarios = array();

    if ($res->num_rows > 0) {
      for ($i = 0; $i < $res->num_rows; $i++) {
        $res->data_seek($i);
        $row = $res->fetch_assoc();
  
        $comentarios[$i] = array(
          'id_comentario' => $row['id_comentario'],
          'nombre' => $row['nombre'], 
          'fecha' => $row['fecha'], 
          'texto' => $row['texto']
        );
      }
    }

    return $comentarios;
  }

  function postComentario($idCientifico, $nombre, $mail, $fecha, $texto) {

    $mysqli = new mysqli("database", "pablo", "tiger", "SIBW");
    if ($mysqli->connect_errno) {
      echo ("Fallo al conectar: " . $mysqli->connect_error);
    }
    echo $idCientifico;
                                                                                                        //".$idCientifico .", '".$nombre ."', '".$fecha ."', '".$mail ."', '".$texto .")");

    $res = $mysqli->query("INSERT INTO comentarios (id_cientifico, nombre, fecha, correo, texto) VALUES ($idCientifico, '$nombre', '$mail', '$fecha', '$texto')");

    if ($mysqli->query($res) === TRUE) {
      $return = true;
    } else {
      $return = false;
    }
    
    //$conn->close();
    return $return;
  }

  function prueba($numero) {
    return 'El numero es el ' .$numero;
  }
?>