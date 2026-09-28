/*=========================================
    Sri Krishna Collection
    Login Page JavaScript
=========================================*/

document.addEventListener("DOMContentLoaded", function () {

    // ==========================
    // Password Show / Hide
    // ==========================

    const password = document.getElementById("password");
    const togglePassword = document.getElementById("togglePassword");

    if (togglePassword && password) {

        togglePassword.addEventListener("click", function () {

            const icon = this.querySelector("i");

            if (password.type === "password") {

                password.type = "text";

                icon.classList.remove("fa-eye");
                icon.classList.add("fa-eye-slash");

            } else {

                password.type = "password";

                icon.classList.remove("fa-eye-slash");
                icon.classList.add("fa-eye");

            }

        });

    }

    // ==========================
    // Login Form Validation
    // ==========================

    const loginForm = document.querySelector("form");

    if (loginForm) {

        loginForm.addEventListener("submit", function (e) {

            e.preventDefault();

            const email = document.querySelector("input[type='email']").value.trim();
            const pass = password.value.trim();

            if (email === "" || pass === "") {

                alert("Please fill all fields.");

                return;

            }

            if (!validateEmail(email)) {

                alert("Please enter a valid Email Address.");

                return;

            }

            if (pass.length < 6) {

                alert("Password must contain at least 6 characters.");

                return;

            }

            loginSuccess();

        });

    }

    // ==========================
    // Email Validation
    // ==========================

    function validateEmail(email) {

        const regex =
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        return regex.test(email);

    }

    // ==========================
    // Login Success Animation
    // ==========================

    function loginSuccess() {

        const button = document.querySelector(".login-btn");

        button.disabled = true;

        button.innerHTML =
            `<span class="spinner-border spinner-border-sm"></span>
             Logging in...`;

        setTimeout(function () {

            alert("Login Successful!");

            // Redirect

            window.location.href = "/Krishna/welcome";

        }, 2000);

    }

    // ==========================
    // Input Focus Effect
    // ==========================

    document.querySelectorAll(".form-control").forEach(function (input) {

        input.addEventListener("focus", function () {

            this.parentElement.style.boxShadow =
                "0 0 15px rgba(212,175,55,0.5)";

        });

        input.addEventListener("blur", function () {

            this.parentElement.style.boxShadow = "none";

        });

    });

});