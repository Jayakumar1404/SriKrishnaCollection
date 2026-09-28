document.addEventListener("DOMContentLoaded", function () {

    const boxes =
        document.querySelectorAll(".otp-box");

    const hiddenOtp =
        document.getElementById("otp");

    const form =
        document.getElementById("otpForm");

    const verifyButton =
        document.getElementById("verifyButton");

    const verifyText =
        document.getElementById("verifyText");

    const verifyArrow =
        document.getElementById("verifyArrow");

    const resendButton =
        document.getElementById("resendButton");

    const timerElement =
        document.getElementById("timer");


    // =====================================================
    // OTP BOX INPUT
    // =====================================================

    boxes.forEach(function (box, index) {

        box.addEventListener("input", function () {

            box.value =
                box.value.replace(/\D/g, "").slice(0, 1);

            if (box.value && index < boxes.length - 1) {

                boxes[index + 1].focus();

            }

            updateHiddenOtp();

        });


        // Backspace

        box.addEventListener("keydown", function (event) {

            if (
                event.key === "Backspace" &&
                !box.value &&
                index > 0
            ) {

                boxes[index - 1].focus();

            }

        });


        // Arrow navigation

        box.addEventListener("keydown", function (event) {

            if (
                event.key === "ArrowLeft" &&
                index > 0
            ) {

                boxes[index - 1].focus();

            }

            if (
                event.key === "ArrowRight" &&
                index < boxes.length - 1
            ) {

                boxes[index + 1].focus();

            }

        });

    });


    // =====================================================
    // PASTE OTP
    // =====================================================

    if (boxes.length > 0) {

        boxes[0].addEventListener(
            "paste",
            function (event) {

                event.preventDefault();

                const pasted =
                    event.clipboardData
                        .getData("text")
                        .replace(/\D/g, "")
                        .slice(0, 6);


                pasted.split("").forEach(
                    function (digit, index) {

                        if (boxes[index]) {
                            boxes[index].value = digit;
                        }

                    }
                );


                updateHiddenOtp();


                if (pasted.length === 6) {

                    boxes[5].focus();

                }

            }
        );
    }


    // =====================================================
    // UPDATE HIDDEN OTP
    // =====================================================

    function updateHiddenOtp() {

        let otp = "";

        boxes.forEach(function (box) {

            otp += box.value;

        });

        hiddenOtp.value = otp;
    }


    // =====================================================
    // FORM SUBMIT
    // =====================================================

    if (form) {

        form.addEventListener(
            "submit",
            function (event) {

                updateHiddenOtp();

                if (hiddenOtp.value.length !== 6) {

                    event.preventDefault();

                    Swal.fire({
                        icon: "warning",
                        title: "Enter Complete OTP",
                        text: "Please enter the 6-digit verification code.",
                        confirmButtonColor: "#b9972e"
                    });

                    return;
                }


                verifyButton.disabled = true;

                verifyText.innerText =
                    "VERIFYING";

                verifyArrow.classList.remove(
                    "fa-arrow-right"
                );

                verifyArrow.classList.add(
                    "fa-spinner",
                    "fa-spin"
                );

            }
        );
    }


    // =====================================================
    // OTP TIMER
    // =====================================================

    let remainingSeconds = 120;


    function updateTimer() {

        const minutes =
            Math.floor(remainingSeconds / 60);

        const seconds =
            remainingSeconds % 60;


        timerElement.innerText =
            String(minutes).padStart(2, "0") +
            ":" +
            String(seconds).padStart(2, "0");


        if (remainingSeconds <= 0) {

            clearInterval(timer);

            resendButton.disabled = false;

            timerElement.innerText =
                "EXPIRED";

            return;
        }


        remainingSeconds--;

    }


    updateTimer();


    const timer =
        setInterval(updateTimer, 1000);


    // =====================================================
    // RESEND
    // =====================================================

    resendButton.addEventListener(
        "click",
        function () {

            /*
             * IMPORTANT:
             * This button is only enabled after the
             * frontend timer expires.
             *
             * Connect it to your existing resend-OTP
             * endpoint if your backend already has one.
             */

            Swal.fire({
                icon: "info",
                title: "Resend OTP",
                text: "Please use your existing resend OTP process.",
                confirmButtonColor: "#b9972e"
            });

        }
    );


    // =====================================================
    // AUTO FOCUS
    // =====================================================

    if (boxes.length > 0) {

        setTimeout(function () {

            boxes[0].focus();

        }, 300);

    }

});