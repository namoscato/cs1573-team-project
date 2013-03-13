//compute_average:
//computes the average, given a generated text file with n rows and 2 columns

//grab the file
<input type="file" id="fileinput" />
<script type="text/javascript">
	function readFile(evt) {
		var f = evt.target.files[0];
		//if we failed to load the file at all, alert
		if (!f) {
			alert("Failed to load text file");
		}
		//check that we actually have a text file
		else if (!file.type.match('text.*')) {
			alert(f.name + " is not a valid text file.");
		}
		//proceed with reading!
		else {
			var r = new FileReader();

			//read into memory
			r.readAsText(f);
			
			//handle progress, success, errors
			r.onprogress = updateProgress;
			r.onload = loaded;
			r.onerror = errorHandler;
		}
	}

	function updateProgress(evt) {
		if (evt.lengthComputable) {
			var loaded = (evt.loaded/evt.total);
		}
	}

	function loaded(evt) {
		var fileString = evt.target.result;
		var lines = fileString.split("\n"); //split the file by lines

		//each line should contain the rating and the count of people who contributed to that rating.
		//next we want to compute the weighted mean:  the sum of the (rating * count)s divided by the sum of the counts.
		var numerator = 0;
		var denominator = 0;
		for (int i = 0; i < lines.length; i++) { //pass through the lines
			var arr = lines.split(" ");
			var rating = arr[0];
			var num = arr[1];

			numerator += rating * num;
			denominator += num;
		}
		var average = numerator / denominator;

	}

	function errorHandler (evt) {
		if (evt.target.error.name == "NotReadableError") {
			alert("File could not be read.");
		}
	}

</script>