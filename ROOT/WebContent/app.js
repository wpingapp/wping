var data = new DataCircle(128);
var lastDrawMaxValue = 100;

window.addEventListener("load", function() {
	var socket = new WebSocket('ws://' + location.host + '/websocket');
	
	var html = '<div id="login">pwd<input type="password" id="pwd" /></div>';
	var body = document.querySelector('body');
	body.innerHTML = html + body.innerHTML;
	document.querySelector('#pwd').onkeyup = function(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				socket.onmessage = onMessage;
				socket.send(this.responseText);
				body.removeChild(document.querySelector('#login'));
				body.innerHTML = body.innerHTML + '<canvas id="canvas"></canvas>';
				body.innerHTML = body.innerHTML + '<div id="times"></div>';
				setInterval(function() {
					draw();
				}, 10);
			}
		};
		xhttp.open("POST", "api/login", true);
		xhttp.setRequestHeader('Content-Type', 'application/json');
		xhttp.send(JSON.stringify({pwd: document.querySelector('#pwd').value}));
		
	};
	
	
});




function onMessage(event) {
	var obj = JSON.parse(event.data);
	
	data.push(obj);
	
	var element = document.querySelector('#times');

	var color = 'red';
	
	if (obj.time != null && obj.time < 40) {
		color = 'yellow';
	}

	if (obj.time != null && obj.time < 20) {
		color = 'green';
	}
	
	var html = '<span class="' + color + '">' + (obj.time != null ? obj.time + 'ms' : 'loss') + '</span>';
	
	element.innerHTML = element.innerHTML + '<div>' + html + '</div>\n';
	
	if (element.children.length > 20) {
		element.removeChild(element.children[0]);
	}
}


function draw() {
	
	
	
	var interval = 5000;
	
	var width = 800;
	var height = 180;
	
	var maxValue = lastDrawMaxValue;
	
	var green = 10;
	var yellow = 30;
	var red = 50;
	

	var canvas = document.getElementById("canvas");
	
	canvas.width = width;
	canvas.height = height;
	canvas.style.width = width;
	canvas.style.height = height;
	
	var ctx = canvas.getContext("2d");
	ctx.translate(0.5, 0);
	
	var gradient = ctx.createLinearGradient(0,0,0,180);
	gradient.addColorStop(0,"#ff6666");
	gradient.addColorStop((maxValue - red) / maxValue,"#ff6666");
	gradient.addColorStop((maxValue - yellow) / maxValue,"#ffff66");
	gradient.addColorStop((maxValue - green) / maxValue,"#88ff66");
	gradient.addColorStop(1,"#88ff66");
	ctx.fillStyle = gradient;
	ctx.fillRect(0,0,800,180); 
	
	
	ctx.fillStyle = "black";
	
	
	var last = null;
	var max = 0;
	
	var currentTime = new Date().getTime();
	
	var drawData = data.getAll();
	
//	var s = '';
	
//	for (var i in drawData) {
//	for (var i = 0; i < 10; i++){
	for (var i = drawData.length - 1; i >= 0; i--){
		
		
		if (drawData[i] == undefined) {
//			s = s + i + ", ";
//			console.log(i);	
			continue;
		} 
		
//		if (drawData[i].timestamp < currentTime - interval) {
//			continue;
//		}
		
			
			
//		console.log(i + " - " + datax.pointer);	
		var x = width - Math.floor((currentTime - drawData[i].timestamp) * (width / interval));
		var y = height - Math.floor(drawData[i].time * (height / maxValue));
		
		if (last != null && (x < 0 || last.x < 0)) {
			continue;
		}
		
		if (drawData[i].time > max) {
			max = drawData[i].time;
		} 
		
		if (drawData[i].time == null) {
			ctx.fillStyle = "red";
			ctx.fillRect(last.x, 0, x - last.x, height);
			last = null;
		}
		
//		console.log ('x: ' + x);
//		console.log ('y: ' + y);
		
		if (last != null){
		
			ctx.fillStyle = "black";
			
			var top = 0;
			var bot = 0;
			if (y > last.y) {
				top = last.y;
				bot = y;
			} else {
				top = y;
				bot = last.y;
			}
			
			ctx.lineWidth = 1;
			ctx.beginPath();
			ctx.moveTo(last.x, top - 4);
			ctx.lineTo(last.x, bot + 4);
			ctx.stroke();

//			console.log(x + " .. " + last.x );
			ctx.beginPath();
			ctx.lineWidth = 8;
			ctx.moveTo(x, y);
			ctx.lineTo(last.x, y);
			ctx.stroke();
		}
		
		last = {x: x, y: y};
	}
	
	lastDrawMaxValue = max > 100 ? max : 100;
	
//	console.log(s);

}

function DataCircle(size) {
	
	var self = this;
	
	this.size = size;
	
	var data = [];
	
//	for (var i = 0; i < data.length; i++){
//		data[i] = 
//	}
	
	this.pointer = 0;
	
	
	this.push = function(obj){
		data[self.pointer] = obj;
		self.pointer++;
		
		if (self.pointer > self.size - 1) {
			self.pointer = 0;
		}
	}
	
	this.getAll = function(){
		var r = [];
		
		var p = self.pointer;
		while (r.length < data.length){
			
			if (p > data.length - 1) {
				p = 0;
			}
			
			r.push(data[p]);
			
			p++;
			
		}
		
		return r;
		
	}
	
//	this.get = function(i) {
//		console.log(i + ": " + (i < self.pointer ? i + self.pointer : i + self.pointer - self.size));
//		return data[i < self.pointer - 1 ? i + self.pointer - 1 : i + self.pointer - 1 - self.size];
//	}
//	
	
	
}
