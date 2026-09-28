document.addEventListener("DOMContentLoaded", function () {

    const password =
        document.getElementById("password");

    const confirmPassword =
        document.getElementById("confirmPassword");

    const togglePassword =
        document.getElementById("togglePassword");

    const toggleConfirm =
        document.getElementById("toggleConfirm");

    const strengthBar =
        document.getElementById("strengthBar");

    const strengthText =
        document.getElementById("strengthText");

    const matchMessage =
        document.getElementById("matchMessage");

    const form =
        document.getElementById("resetPasswordForm");

    const resetButton =
        document.getElementById("resetButton");

    const resetText =
        document.getElementById("resetText");

    const resetArrow =
        document.getElementById("resetArrow");


    /* =====================================================
       PASSWORD VISIBILITY
    ===================================================== */

    function toggleVisibility(input, button) {

        button.addEventListener("click", function () {

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

        });

    }


    toggleVisibility(
        password,
        togglePassword
    );

    toggleVisibility(
        confirmPassword,
        toggleConfirm
    );


    /* =====================================================
       PASSWORD STRENGTH
    ===================================================== */
    function checkStrength(value) {

    let score = 0;

    if (value.length >= 8) {
        score++;
    }

    if (/[A-Z]/.test(value)) {
        score++;
    }

    if (/[0-9]/.test(value)) {
        score++;
    }

    if (/[^A-Za-z0-9]/.test(value)) {
        score++;
    }


    const percentage = score * 25;

    // Update track width
    strengthBar.style.width =
        percentage + "%";


    // ==========================================
    // PASSWORD STRENGTH COLOR
    // ==========================================

    if (value.length === 0) {

        strengthBar.style.background =
            "#d9534f";

        strengthText.innerText =
            "Enter password";

        strengthText.style.color =
            "#999";

    }

    else if (score === 1) {

        // Weak
        strengthBar.style.background =
            "#d9534f";

        strengthText.innerText =
            "Weak";

        strengthText.style.color =
            "#d9534f";

    }

    else if (score === 2) {

        // Fair
        strengthBar.style.background =
            "#e6a23c";

        strengthText.innerText =
            "Fair";

        strengthText.style.color =
            "#e6a23c";

    }

    else if (score === 3) {

        // Good
        strengthBar.style.background =
            "#c9a227";

        strengthText.innerText =
            "Good";

        strengthText.style.color =
            "#9a7714";

    }

    else if (score === 4) {

        // Strong
        strengthBar.style.background =
            "#198754";

        strengthText.innerText =
            "Strong";

        strengthText.style.color =
            "#198754";
    }


    updateRequirements(value);
}


    /* =====================================================
       REQUIREMENTS
    ===================================================== */

    function updateRequirements(value) {

        const length =
            document.getElementById(
                "lengthRequirement"
            );

        const uppercase =
            document.getElementById(
                "uppercaseRequirement"
            );

        const number =
            document.getElementById(
                "numberRequirement"
            );

        const special =
            document.getElementById(
                "specialRequirement"
            );


        setRequirement(
            length,
            value.length >= 8
        );

        setRequirement(
            uppercase,
            /[A-Z]/.test(value)
        );

        setRequirement(
            number,
            /[0-9]/.test(value)
        );

        setRequirement(
            special,
            /[^A-Za-z0-9]/.test(value)
        );
    }


    function setRequirement(
        element,
        valid
    ) {

        if (!element) {
            return;
        }


        const icon =
            element.querySelector("i");


        if (valid) {

            element.classList.add(
                "valid"
            );

            icon.classList.remove(
                "fa-circle"
            );

            icon.classList.add(
                "fa-check"
            );

        } else {

            element.classList.remove(
                "valid"
            );

            icon.classList.remove(
                "fa-check"
            );

            icon.classList.add(
                "fa-circle"
            );
        }

    }


    password.addEventListener(
        "input",
        function () {

            checkStrength(
                password.value
            );

            checkMatch();

        }
    );


    /* =====================================================
       CONFIRM PASSWORD
    ===================================================== */

    confirmPassword.addEventListener(
        "input",
        checkMatch
    );


    function checkMatch() {

        if (!confirmPassword.value) {

            matchMessage.innerText = "";

            return;
        }


        if (
            password.value ===
            confirmPassword.value
        ) {

            matchMessage.innerText =
                "✓ Passwords match";

            matchMessage.style.color =
                "#8a741e";

        } else {

            matchMessage.innerText =
                "Passwords do not match";

            matchMessage.style.color =
                "#d9534f";
        }

    }


    /* =====================================================
       FORM SUBMIT
    ===================================================== */

    form.addEventListener(
        "submit",
        function (event) {

            const value =
                password.value;


            const strongPassword =
                value.length >= 8 &&
                /[A-Z]/.test(value) &&
                /[0-9]/.test(value) &&
                /[^A-Za-z0-9]/.test(value);


            if (!strongPassword) {

                event.preventDefault();

                Swal.fire({
                    icon: "warning",
                    title: "Password Too Weak",
                    text:
                        "Use at least 8 characters, one uppercase letter, one number and one special character.",
                    confirmButtonColor: "#b9972e"
                });

                return;
            }


            if (
                password.value !==
                confirmPassword.value
            ) {

                event.preventDefault();

                Swal.fire({
                    icon: "error",
                    title: "Passwords Don't Match",
                    text:
                        "Please make sure both passwords are the same.",
                    confirmButtonColor: "#b9972e"
                });

                return;
            }


            /* Loading */

            resetButton.disabled = true;

            resetText.innerText =
                "UPDATING PASSWORD";

            resetArrow.classList.remove(
                "fa-arrow-right"
            );

            resetArrow.classList.add(
                "fa-spinner",
                "fa-spin"
            );

        }
    );


    /* =====================================================
       INITIAL FOCUS
    ===================================================== */

    if (password) {

        setTimeout(function () {

            password.focus();

        }, 300);
    }

});