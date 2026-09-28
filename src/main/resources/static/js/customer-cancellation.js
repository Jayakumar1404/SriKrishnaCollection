/* =========================================================
   SRI KRISHNA COLLECTION
   CUSTOMER CANCELLATION
   ========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    const form =
        document.getElementById("cancellationForm");

    const reason =
        document.getElementById("reason");

    const characterCount =
        document.getElementById("characterCount");

    const cancelButton =
        document.getElementById("cancelSubmitButton");

    const confirmationModal =
        document.getElementById("confirmationModal");

    const confirmYes =
        document.getElementById("confirmYes");

    const confirmNo =
        document.getElementById("confirmNo");


    /* =====================================================
       CHARACTER COUNT
       ===================================================== */

    if (reason && characterCount) {

        function updateCharacterCount() {

            const length =
                reason.value.length;

            characterCount.textContent =
                length + " / 500";

        }

        reason.addEventListener(
            "input",
            updateCharacterCount
        );

        updateCharacterCount();
    }


    /* =====================================================
       FORM VALIDATION
       ===================================================== */

    if (form) {

        form.addEventListener(
            "submit",
            function (event) {

                const reasonValue =
                    reason.value.trim();

                if (reasonValue.length < 5) {

                    event.preventDefault();

                    alert(
                        "Please provide a valid cancellation reason."
                    );

                    reason.focus();

                    return;
                }


                /*
                 * Don't immediately submit.
                 * Show confirmation first.
                 */

                event.preventDefault();

                if (confirmationModal) {

                    confirmationModal.classList.add(
                        "active"
                    );

                    document.body.style.overflow =
                        "hidden";
                }

            }
        );
    }


    /* =====================================================
       CONFIRM CANCELLATION
       ===================================================== */

    if (confirmYes) {

        confirmYes.addEventListener(
            "click",
            function () {

                if (cancelButton) {

                    cancelButton.disabled =
                        true;

                    cancelButton.innerHTML =
                        "Cancelling...";
                }

                document.body.style.overflow =
                    "";

                /*
                 * Actually submit the form.
                 */

                form.submit();

            }
        );
    }


    /* =====================================================
       CLOSE CONFIRMATION
       ===================================================== */

    if (confirmNo) {

        confirmNo.addEventListener(
            "click",
            function () {

                closeModal();

            }
        );
    }


    /* =====================================================
       CLICK OUTSIDE MODAL
       ===================================================== */

    if (confirmationModal) {

        confirmationModal.addEventListener(
            "click",
            function (event) {

                if (
                    event.target ===
                    confirmationModal
                ) {

                    closeModal();

                }

            }
        );
    }


    /* =====================================================
       ESC KEY
       ===================================================== */

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Escape"
                &&
                confirmationModal
                &&
                confirmationModal.classList.contains(
                    "active"
                )
            ) {

                closeModal();

            }

        }
    );


    /* =====================================================
       CLOSE MODAL FUNCTION
       ===================================================== */

    function closeModal() {

        if (confirmationModal) {

            confirmationModal.classList.remove(
                "active"
            );

            document.body.style.overflow =
                "";

        }

    }

});