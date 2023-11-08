document.addEventListener('DOMContentLoaded', function() {
	const loginTab = document.getElementById('login-tab');
	const signupTab = document.getElementById('signup-tab');

	// Switch between login and signup forms
	loginTab.addEventListener('click', function() {
		window.location.href = "index.jsp";
	});

	signupTab.addEventListener('click', function() {
		window.location.href = "signup.jsp";
	});


});
