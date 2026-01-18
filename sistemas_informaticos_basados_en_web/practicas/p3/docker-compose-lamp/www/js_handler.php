<?php
    include("bd.php");

    if (isset($_POST['postComentario'])) {
        echo postComentario($_POST['idCientifico'], $_POST['nombre'], $_POST['mail'], $_POST['fecha'], $_POST['texto']);
        //echo "SIIIII";
    }
    else{
        return false;
    }
?>