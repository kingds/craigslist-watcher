<!DOCTYPE html>
<html lang="en">
<HEAD>
<title>Craigslist Watcher</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<style>
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

#email-input {
	width: 600px;
}

#submit-button {
	margin-top: 10px;
	width: 100px;
	margin-left: 250px;
}

input, div, p {
	margin-top: 10px;
}
</style>
</HEAD>

<body>
	<div id="input-div">
		<form method="post">
			<input type="text" id="email-input" class="form-control" name="email"
				placeholder="Email address"> <input type="text"
				id="name-input" class="form-control" name="name"
				placeholder="Name for the search">
			<p>How often would you like us to email you with new search
				results?</p>
			<div class="radio">
				<label><input type="radio" name="frequency" value="minute">Every
					minute</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="frequency"
					value="ten_minutes">Every ten minutes</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="frequency"
					value="thirty_minutes">Every thirty minutes</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="frequency" value="hour">Every
					hour</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="frequency" value="day">Once
					a day</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="frequency" value="week">Once
					a week</label>
			</div>
			<input type="submit" id="submit-button"
				class="btn btn-info form-control" value="Submit">
		</form>
	</div>
</body>