document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // PASSWORD SHOW / HIDE
    // =====================================================

    const password =
        document.getElementById("password");

    const togglePassword =
        document.getElementById("togglePassword");


    if (password && togglePassword) {

        togglePassword.addEventListener(
            "click",
            function () {

                const icon =
                    togglePassword.querySelector("i");


                if (password.type === "password") {

                    password.type = "text";

                    icon.classList.remove(
                        "fa-eye"
                    );

                    icon.classList.add(
                        "fa-eye-slash"
                    );

                    togglePassword.setAttribute(
                        "aria-label",
                        "Hide password"
                    );

                } else {

                    password.type = "password";

                    icon.classList.remove(
                        "fa-eye-slash"
                    );

                    icon.classList.add(
                        "fa-eye"
                    );

                    togglePassword.setAttribute(
                        "aria-label",
                        "Show password"
                    );
                }

            }
        );
    }


    // =====================================================
    // LOGIN BUTTON LOADING
    // =====================================================

    const form =
        document.getElementById(
            "customerLoginForm"
        );

    const loginButton =
        document.getElementById(
            "loginButton"
        );

    const loginText =
        document.getElementById(
            "loginText"
        );

    const loginArrow =
        document.getElementById(
            "loginArrow"
        );


    if (form) {

        form.addEventListener(
            "submit",
            function () {

                if (!form.checkValidity()) {
                    return;
                }


                loginButton.disabled = true;


                loginText.innerText =
                    "SIGNING IN";


                loginArrow.classList.remove(
                    "fa-arrow-right"
                );


                loginArrow.classList.add(
                    "fa-spinner",
                    "fa-spin"
                );

            }
        );
    }


    // =====================================================
    // AUTO FOCUS EMAIL
    // =====================================================

    const email =
        document.getElementById("email");


    if (email) {

        setTimeout(
            function () {

                email.focus();

            },
            300
        );
    }

});