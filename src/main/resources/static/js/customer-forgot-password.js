document.addEventListener("DOMContentLoaded", function () {

    const form =
        document.getElementById("forgotPasswordForm");

    const email =
        document.getElementById("email");

    const button =
        document.getElementById("recoveryButton");

    const text =
        document.getElementById("recoveryText");

    const arrow =
        document.getElementById("recoveryArrow");


    // Focus email

    if (email) {

        setTimeout(function () {

            email.focus();

        }, 300);
    }


    // Submit loading

    if (form) {

        form.addEventListener(
            "submit",
            function () {

                if (!form.checkValidity()) {
                    return;
                }

                button.disabled = true;

                text.innerText =
                    "SENDING OTP";

                arrow.classList.remove(
                    "fa-arrow-right"
                );

                arrow.classList.add(
                    "fa-spinner",
                    "fa-spin"
                );

            }
        );
    }

});