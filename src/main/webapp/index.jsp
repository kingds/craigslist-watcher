<!DOCTYPE html>
<html lang="en">
<HEAD>
<title>Craigslist Watcher</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<style>
h2 {
	text-align: center;
}

#input-div {
	position: absolute;
	margin: auto;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	width: 600px;
	height: 300px;
}

#search-input {
	width: 600px;
	margin-top: 15px;
}

#submit-button {
	margin-top: 10px;
	width: 100px;
	margin-left: 250px;
}
</style>
</HEAD>

<body>
	<div id="input-div">
		<h2>Craigslist Watcher</h2>
		<form action="NewSearch" method="get" class="form-group">
		  <input type="text" id="search-input" class="form-control" name="searchUrl" placeholder="Paste the Craigslist search URL that you want to follow">
		  <input type="submit" id="submit-button" class="btn btn-info form-control" value="Submit">
		</form>
	</div>
</body>


