document.addEventListener("DOMContentLoaded", function () {
    const socket = new WebSocket(document.location.protocol + '//' + document.domain + ':' + location.port + '/socket');
    socket.addEventListener("message", (event) => {
        document.getElementById("message").innerHTML = event.data;
    });
    document.getElementById('form').onsubmit = function () {
        var input = document.getElementById('input');
        console.log(input.value);
        socket.send(input.value);
        return false;
    };
});