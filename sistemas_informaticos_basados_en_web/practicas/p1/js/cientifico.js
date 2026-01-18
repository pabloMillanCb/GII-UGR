var sendCommentButton = document.getElementById("enviar-comentario");
var nombreComment = document.getElementById("name");
var mailComment = document.getElementById("mail");
var textoComment = document.getElementById("texto-comentario");
var buttonComment = document.getElementById("boton-comentario");

const taboo = ["diantres", "caracoles", "canastos", "retruecanos", "corcholis", "caramba",
                "mequetrefe", "albricias", "caspita", "maldicion"]


// Función para abrir la pestaña de comentarios
function changeComments() {

    var comentarios = document.getElementById("seccion-comentarios")

    if (comentarios.style.display == "none") {
        comentarios.style.display = "block";
    }
    else {
        comentarios.style.display = "none";
    }
}

// Función para comprobar que un formulario de comentario se ha rellenado correctamente
function checkComment() {

    var nombreCorrecto = false
    var emailCorrecto = false
    var comentarioCorrecto = false

    // Comprobamos que el nombre no esté vacío
    if (nombreComment.value != ""){
        nombreCorrecto = true
    }

    // Comprobamos que el correo tenga un formato correcto
    var aux = 0
    console.log(mailComment.value.length)
    for (let i = 0; i < mailComment.value.length; i++){

        if(aux == 3) {
            emailCorrecto = true
        }

        if (mailComment.value[i] == "@" && aux == 0){
            aux++
        }
        if (mailComment.value[i] == "." && aux == 1){
            aux++
        }
        if (aux == 2){
            aux++
        }
    }

    // Comprobamos que el texto no esté vacío
    if (textoComment.value != "") {
        comentarioCorrecto = true
    }

    if (nombreCorrecto && emailCorrecto && comentarioCorrecto) {
        sendComment()
    }
    else{
        if (!nombreCorrecto || !comentarioCorrecto) {
            document.getElementById("error-comment").textContent = "Te faltan campos por rellenar"
        }
        else if (!emailCorrecto){
            console.log("ERROR EMAIL")
            document.getElementById("error-comment").textContent = "El email debe tener formato xxxxx@nnn.sss"
        }
        modal.style.display = "block";
    }

}

// Función para insertar el comentario correcto en el HTML
function sendComment() {

    // Creamos el div
    var div = document.createElement("div")
    div.className = "comentario";

    // Creamos el nombre
    var user = document.createElement("H2")
    const textUser = document.createTextNode(document.getElementById("name").value);
    user.appendChild(textUser)

    // Creamos la fecha
    var fecha = document.createElement("H3")
    var date = new Date();
    const textDate = document.createTextNode(date.toLocaleDateString()+ " - " + date.getHours() + ":" + date.getMinutes()
    );
    fecha.appendChild(textDate)

    // Creamos el texto del comentario
    var texto = document.createElement("p")
    texto.textContent = "\"" + document.getElementById("texto-comentario").value + "\""

    // Lo apilamos todo y lo insertamos en el HTML
    div.appendChild(user)
    div.appendChild(fecha)
    div.appendChild(texto)

    const list = document.getElementById("seccion-comentarios");
    list.insertBefore(div, list.children[list.children.length-1]);

    // Limpiamos los campos del formulario
    document.getElementById("name").value = ""
    document.getElementById("mail").value = ""
    document.getElementById("texto-comentario").value = ""

}

// Sacado de https://bobbyhadz.com/blog/javascript-string-replace-character-at-index
function replaceCharacter(string, index, replacement) {
    return (
      string.slice(0, index) +
      replacement +
      string.slice(index + replacement.length)
    );
  }


function _1984() {

    console.log(textoComment.value.length)
    
    var word = ""
    var index_word = 0
    var texto = textoComment.value
    for (let i = 0; i < textoComment.value.length; i++){
        console.log(word)
        if (textoComment.value[i] == " "){
            word = ""
            index_word = i+1
        }
        else {
            word += textoComment.value[i]
            if (taboo.includes(word.toLocaleLowerCase())) {
                for (let j = index_word; j <= i; j++){
                    textoComment.value = replaceCharacter(textoComment.value, j, "*")
                }
                console.log("1984")
                console.log(index_word)
                console.log(i)
                console.log(texto)
            }
        }
    }

}

sendCommentButton.onclick = checkComment;
buttonComment.onclick = changeComments;

textoComment.onkeyup = _1984;

// Get the modal
var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}