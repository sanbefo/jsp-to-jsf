<html>
	<head>
		<title>First JSP</title>
	</head>
	<body>
		<%
			double num = Math.random();
			if (num > 0.95) {
		%>
		<h2>You'll have a luck day!</h2><p>(<%= num %>)</p>
		<% } else { %>
		<h2>Well, life goes on ... </h2><p>(<%= num %>)</p>
		<% } %>
		<img src = "http://www.tutorialspoint.com/images/jsf-mini-logo.png" />
		<select name = "j_idt6:j_idt8">
			<option value = "1">Item 1</option>
			<option value = "2">Item 2</option>
		</select>
		<label for="email">
			Email address
		</label>
		<a href="<%= request.getRequestURI() %>">
			<h3>Try Again</h3>
		</a>
	</body>
</html>