<html>
	<script type="text/javascript" src="/JavaServerFaces/faces/javax.faces.resource/common.js?ln=js">
	</script>
	<link type = "text/css" rel = "stylesheet" href = "/helloworld/javax.faces.resource/styles.css.jsf?ln = css" /> 
   	<head>
		<title>First JSP</title>
	</head>
	<body>
		<div>
			<select name="j_idt6:j_idt8">
				<option value="1">Item 1</option>
				<option value="2">Item 2</option>
			</select>
			<select name="j_idt6:j_idt8">
				<option value="3">Item 3</option>
				<option value="4">Item 4</option>
			</select>
		</div>
		<%
			double x = Math.random();
		%>
		<div>
			<input type="radio" id="male" name="gender" value="male">
			<label for="male">Male</label><br>
			<input type="radio" id="female" name="gender" value="female">
			<label for="female">Female</label><br>
			<input type="radio" id="other" name="gender" value="other">
			<label for="other">Other</label>
		</div>
		<input
			type="hidden"
			id="custId"
			name="custId"
			value="3487">
		<input type="text" id="username" name="username">
		<input type="password" id="pass" name="password" minlength="8" required>
		<img src="http://www.tutorialspoint.com/images/jsf-mini-logo.png">
		<button type="button">Click Me!</button>
		<table id = "j_idt10:panel" border = "1" cellpadding = "10" cellspacing = "1">
			<thead>
				<tr><th colspan = "2" scope = "colgroup">Login</th></tr>
			</thead>
			<tbody>
				<tr>
					<td>Username</td>
					<td>xxxxxxxxxxxxx</td>
				</tr>
				<tr>
					<td>Password</td>
					<td>yyyyyyyyyyyyyy</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan = "2">
						<span style = "display:block; text-align:center">
							<input id = "j_idt10:submit" type = "submit" name = "j_idt10:submit" value = "Submit" />
						</span>
					</td>
				</tr>
			</tfoot>
		</table>
		<%
			double num = Math.random();
			if (num > 0.95) {
		%>
		<h2>You'll have a luck day!</h2><p>(<%= num %>)</p>
		<% } else { %>
		<h2>Well, life goes on ... </h2><p>(<%= num %>)</p>
		<% } %>
		<label for="email">
			Email address
		</label>
		<a href="<%= request.getRequestURI() %>">
			<h3>Try Again</h3>
		</a>
		<table border="1">
			<tr>
				<td>Festival Name:</td>
	    		<td>Location:</td>
        		<td>Start Date:</td>
	    		<td>End Date:</td>
		    	<td>URL:</td>
			</tr>
			<% for(int i = 0; i < allFestivals.size(); i+=1) { %>
				<tr>
					<td>${allFestivals.get(i).getFestivalName()}</td>
					<td>${allFestivals.get(i).getLocation()}</td>
					<td>${allFestivals.get(i).getStartDate()}</td>
					<% if (1 > 2) { %>
						<td>${allFestivals.get(i).getEndDate()}</td>
					<% } %>
					<td>${allFestivals.get(i).getURL()}</td>
				</tr>
			<% } %>
		</table>
	</body>
</html>