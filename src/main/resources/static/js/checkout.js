/* =========================================================
   SRI KRISHNA COLLECTION
   CHECKOUT PAGE JAVASCRIPT
   ========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // ELEMENTS
    // =====================================================

    const checkoutForm =
        document.getElementById("checkoutForm");

    const placeOrderButton =
        document.getElementById("placeOrderButton");

    const placeOrderText =
        document.getElementById("placeOrderText");

    const placeOrderIcon =
        document.getElementById("placeOrderIcon");


    // =====================================================
    // CHECKOUT FORM
    // =====================================================

    if (checkoutForm) {

        checkoutForm.addEventListener(
            "submit",
            function (event) {

                // -----------------------------------------
                // GET VALUES
                // -----------------------------------------

                const shippingAddress =
                    checkoutForm
                        .querySelector(
                            '[name="shippingAddress"]'
                        );

                const city =
                    checkoutForm
                        .querySelector(
                            '[name="city"]'
                        );

                const state =
                    checkoutForm
                        .querySelector(
                            '[name="state"]'
                        );

                const pincode =
                    checkoutForm
                        .querySelector(
                            '[name="pincode"]'
                        );

                const phone =
                    checkoutForm
                        .querySelector(
                            '[name="phone"]'
                        );


                // -----------------------------------------
                // ADDRESS VALIDATION
                // -----------------------------------------

                if (
                    !shippingAddress ||
                    shippingAddress.value.trim() === ""
                ) {

                    event.preventDefault();

                    showCheckoutMessage(
                        "Please enter your delivery address.",
                        "error"
                    );

                    if (shippingAddress) {
                        shippingAddress.focus();
                    }

                    return;
                }


                // -----------------------------------------
                // CITY VALIDATION
                // -----------------------------------------

                if (
                    city &&
                    city.value.trim() === ""
                ) {

                    event.preventDefault();

                    showCheckoutMessage(
                        "Please enter your city.",
                        "error"
                    );

                    city.focus();

                    return;
                }


                // -----------------------------------------
                // STATE VALIDATION
                // -----------------------------------------

                if (
                    state &&
                    state.value.trim() === ""
                ) {

                    event.preventDefault();

                    showCheckoutMessage(
                        "Please enter your state.",
                        "error"
                    );

                    state.focus();

                    return;
                }


                // -----------------------------------------
                // PINCODE VALIDATION
                // -----------------------------------------

                if (
                    pincode &&
                    !/^[0-9]{6}$/.test(
                        pincode.value.trim()
                    )
                ) {

                    event.preventDefault();

                    showCheckoutMessage(
                        "Please enter a valid 6-digit pincode.",
                        "error"
                    );

                    pincode.focus();

                    return;
                }


                // -----------------------------------------
                // PHONE VALIDATION
                // -----------------------------------------

                if (
                    phone &&
                    !/^[0-9]{10}$/.test(
                        phone.value.trim()
                    )
                ) {

                    event.preventDefault();

                    showCheckoutMessage(
                        "Please enter a valid 10-digit phone number.",
                        "error"
                    );

                    phone.focus();

                    return;
                }


                // -----------------------------------------
                // PAYMENT METHOD
                // -----------------------------------------

                const paymentMethod =
                    checkoutForm.querySelector(
                        'input[name="paymentMethod"]:checked'
                    );


                if (!paymentMethod) {

                    event.preventDefault();

                    showCheckoutMessage(
                        "Please select a payment method.",
                        "error"
                    );

                    return;
                }


                // -----------------------------------------
                // LOADING STATE
                // -----------------------------------------

                if (placeOrderButton) {

                    placeOrderButton.classList.add(
                        "loading"
                    );

                    placeOrderButton.disabled = true;
                }


                if (placeOrderText) {

                    placeOrderText.textContent =
                        "PROCESSING ORDER...";
                }


                if (placeOrderIcon) {

                    placeOrderIcon.className =
                        "fa-solid fa-spinner";
                }

            }
        );
    }


    // =====================================================
    // PINCODE
    // =====================================================

    const pincodeInput =
        document.querySelector(
            '[name="pincode"]'
        );


    if (pincodeInput) {

        pincodeInput.addEventListener(
            "input",
            function () {

                this.value =
                    this.value
                        .replace(/\D/g, "")
                        .substring(0, 6);
            }
        );
    }


    // =====================================================
    // PHONE
    // =====================================================

    const phoneInput =
        document.querySelector(
            '[name="phone"]'
        );


    if (phoneInput) {

        phoneInput.addEventListener(
            "input",
            function () {

                this.value =
                    this.value
                        .replace(/\D/g, "")
                        .substring(0, 10);
            }
        );
    }


    // =====================================================
    // PAYMENT SELECTION
    // =====================================================

    const paymentOptions =
        document.querySelectorAll(
            ".payment-option"
        );


    paymentOptions.forEach(
        function (option) {

            option.addEventListener(
                "click",
                function () {

                    const radio =
                        this.querySelector(
                            'input[type="radio"]'
                        );

                    if (radio) {
                        radio.checked = true;
                    }


                    // Remove selected state

                    paymentOptions.forEach(
                        function (item) {

                            item.classList.remove(
                                "selected"
                            );

                        }
                    );


                    // Add selected state

                    this.classList.add(
                        "selected"
                    );
                }
            );

        }
    );


    // =====================================================
    // SHOW INITIAL SELECTED PAYMENT
    // =====================================================

    const selectedPayment =
        document.querySelector(
            '.payment-option input[type="radio"]:checked'
        );


    if (selectedPayment) {

        const selectedOption =
            selectedPayment.closest(
                ".payment-option"
            );

        if (selectedOption) {

            selectedOption.classList.add(
                "selected"
            );
        }
    }

});


// =========================================================
// CHECKOUT MESSAGE
// =========================================================

function showCheckoutMessage(
    message,
    type
) {

    // Remove existing message

    const existing =
        document.querySelector(
            ".checkout-js-message"
        );

    if (existing) {
        existing.remove();
    }


    // Create message

    const messageBox =
        document.createElement(
            "div"
        );


    messageBox.className =
        "checkout-js-message";


    if (type === "error") {

        messageBox.innerHTML =

            '<i class="fa-solid fa-circle-exclamation"></i>' +
            '<span>' +
            message +
            '</span>';

    } else {

        messageBox.innerHTML =

            '<i class="fa-solid fa-circle-check"></i>' +
            '<span>' +
            message +
            '</span>';
    }


    // Insert before form

    const form =
        document.getElementById(
            "checkoutForm"
        );


    if (form) {

        form.parentNode.insertBefore(
            messageBox,
            form
        );
    }


    // Scroll to message

    messageBox.scrollIntoView({
        behavior: "smooth",
        block: "center"
    });


    // Auto remove

    setTimeout(
        function () {

            if (messageBox) {
                messageBox.remove();
            }

        },
        4000
    );
}
