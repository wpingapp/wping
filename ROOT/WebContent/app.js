window.addEventListener("load", function() {
	var socket = new WebSocket('ws://localhost:8080/websocket');
	socket.onmessage = onMessage;
});

function onMessage(event) {
	
	var element = document.querySelector('#content');

	element.innerHTML = element.innerHTML + '<div>' + event.data + '</div>\n';
	
	if (element.children.length > 20) {
		element.removeChild(element.children[0]);
	}
	
}