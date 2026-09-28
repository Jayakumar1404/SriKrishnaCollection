document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // PASSWORD TOGGLE
    // =====================================================

    function setupPasswordToggle(
        inputId,
        buttonId
    ) {

        const input =
            document.getElementById(inputId);

        const button =
            document.getElementById(buttonId);

        if (!input || !button) {
            return;
        }

        button.addEventListener(
            "click",
            function () {

                const icon =
                    button.querySelector("i");

                if (input.type === "password") {

                    input.type = "text";

                    icon.classList.remove(
                        "fa-eye"
                    );

                    icon.classList.add(
                        "fa-eye-slash"
                    );

                } else {

                    input.type = "password";

                    icon.classList.remove(
                        "fa-eye-slash"
                    );

                    icon.classList.add(
                        "fa-eye"
                    );

                }

            }
        );
    }


    setupPasswordToggle(
        "password",
        "togglePassword"
    );


    setupPasswordToggle(
        "confirmPassword",
        "toggleConfirmPassword"
    );


    // =====================================================
    // PASSWORD STRENGTH
    // =====================================================

    const password =
        document.getElementById("password");

    const strengthBar =
        document.getElementById("strengthBar");

    const strengthText =
        document.getElementById("strengthText");


    if (password) {

        password.addEventListener(
            "input",
            function () {

                const value =
                    password.value;

                let strength = 0;


                if (value.length >= 6) {
                    strength++;
                }

                if (/[A-Z]/.test(value)) {
                    strength++;
                }

                if (/[0-9]/.test(value)) {
                    strength++;
                }

                if (/[^A-Za-z0-9]/.test(value)) {
                    strength++;
                }


                const widths = [
                    "0%",
                    "25%",
                    "50%",
                    "75%",
                    "100%"
                ];

                strengthBar.style.width =
                    widths[strength];


                if (strength === 0) {

                    strengthText.innerText =
                        "Use at least 6 characters";

                } else if (strength === 1) {

                    strengthText.innerText =
                        "Weak password";

                } else if (strength === 2) {

                    strengthText.innerText =
                        "Fair password";

                } else if (strength === 3) {

                    strengthText.innerText =
                        "Good password";

                } else {

                    strengthText.innerText =
                        "Strong password";
                }

            }
        );
    }


    // =====================================================
    // CONFIRM PASSWORD
    // =====================================================

    const confirmPassword =
        document.getElementById(
            "confirmPassword"
        );

    const passwordMatch =
        document.getElementById(
            "passwordMatch"
        );


    function checkPasswords() {

        if (!password.value ||
            !confirmPassword.value) {

            passwordMatch.innerText = "";

            return;
        }


        if (
            password.value ===
            confirmPassword.value
        ) {

            passwordMatch.innerText =
                "Passwords match";

            passwordMatch.style.color =
                "#198754";

        } else {

            passwordMatch.innerText =
                "Passwords do not match";

            passwordMatch.style.color =
                "#dc3545";
        }
    }


    if (password && confirmPassword) {

        password.addEventListener(
            "input",
            checkPasswords
        );

        confirmPassword.addEventListener(
            "input",
            checkPasswords
        );
    }


    // =====================================================
    // MOBILE NUMBER
    // =====================================================

    const mobile =
        document.getElementById("mobile");


    if (mobile) {

        mobile.addEventListener(
            "input",
            function () {

                mobile.value =
                    mobile.value
                        .replace(/\D/g, "")
                        .slice(0, 10);

            }
        );
    }


    // =====================================================
    // FORM SUBMIT
    // =====================================================

    const form =
        document.getElementById(
            "customerRegisterForm"
        );

    const registerButton =
        document.getElementById(
            "registerButton"
        );

    const registerText =
        document.getElementById(
            "registerText"
        );

    const registerArrow =
        document.getElementById(
            "registerArrow"
        );


    if (form) {

       // =====================================================
// FORM SUBMIT
// =====================================================

const form =
    document.getElementById("customerRegisterForm");

const registerButton =
    document.getElementById("registerButton");

const registerText =
    document.getElementById("registerText");

const registerArrow =
    document.getElementById("registerArrow");

const terms =
    document.getElementById("terms");


if (form) {

    form.addEventListener(
        "submit",
        function (event) {

            // =============================================
            // TERMS & CONDITIONS CHECK
            // =============================================

            if (!terms.checked) {

                event.preventDefault();

                Swal.fire({
                    icon: "warning",
                    title: "Terms & Conditions Required",
                    text: "Please accept the Terms & Conditions and Privacy Policy before creating your account.",
                    confirmButtonText: "I Understand",
                    confirmButtonColor: "#b9972e"
                });

                terms.focus();

                return;
            }


            // =============================================
            // PASSWORD MATCH CHECK
            // =============================================

            if (
                password.value !==
                confirmPassword.value
            ) {

                event.preventDefault();

                Swal.fire({
                    icon: "error",
                    title: "Password Mismatch",
                    text: "Password and confirm password must match.",
                    confirmButtonColor: "#b9972e"
                });

                confirmPassword.focus();

                return;
            }


            // =============================================
            // HTML VALIDATION
            // =============================================

            if (!form.checkValidity()) {

                event.preventDefault();

                form.reportValidity();

                return;
            }


            // =============================================
            // LOADING STATE
            // =============================================

            registerButton.disabled = true;

            registerText.innerText =
                "CREATING ACCOUNT";

            registerArrow.classList.remove(
                "fa-arrow-right"
            );

            registerArrow.classList.add(
                "fa-spinner",
                "fa-spin"
            );

        }
    );
}
    }


    // =====================================================
    // AUTO FOCUS
    // =====================================================

    const firstName =
        document.getElementById(
            "firstName"
        );

    if (firstName) {

        setTimeout(
            function () {

                firstName.focus();

            },
            300
        );
    }

});