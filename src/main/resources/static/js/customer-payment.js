document.addEventListener("DOMContentLoaded", function () {

    console.log("=================================");
    console.log("Sri Krishna Payment JS Loaded");
    console.log("=================================");


    const payButton =
        document.getElementById("payNow");

    const errorBox =
        document.getElementById("paymentError");


    // =====================================================
    // CHECK BUTTON
    // =====================================================

    if (!payButton) {

        console.error(
            "ERROR: #payNow button not found."
        );

        return;
    }


    console.log(
        "Pay button found successfully."
    );


    // =====================================================
    // CHECK RAZORPAY SCRIPT
    // =====================================================

    if (typeof Razorpay === "undefined") {

        console.error(
            "ERROR: Razorpay Checkout JS is not loaded."
        );

        showError(
            "Razorpay could not be loaded. Please refresh the page."
        );

        return;
    }


    console.log(
        "Razorpay Checkout JS loaded successfully."
    );


    // =====================================================
    // BUTTON CLICK
    // =====================================================

    payButton.addEventListener(
        "click",
        function () {

            console.log(
                "Continue to Razorpay clicked."
            );

            createRazorpayOrder();

        }
    );


    // =====================================================
    // CREATE ORDER
    // =====================================================

    async function createRazorpayOrder() {

        try {


            // -------------------------------------------------
            // BUTTON LOADING
            // -------------------------------------------------

            payButton.disabled = true;

            payButton.innerHTML =
                '<i class="fa-solid fa-spinner fa-spin"></i> Preparing Payment...';


            clearError();


            console.log(
                "Calling /Krishna/payment/create-order..."
            );


            // -------------------------------------------------
            // BACKEND REQUEST
            // -------------------------------------------------

            const response =
                await fetch(
                    "/Krishna/payment/create-order",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json",

                            "Accept":
                                "application/json"
                        },

                        credentials: "same-origin"
                    }
                );


            console.log(
                "Backend status:",
                response.status
            );


            // -------------------------------------------------
            // READ RESPONSE
            // -------------------------------------------------

            const text =
                await response.text();


            console.log(
                "Backend response:",
                text
            );


            let data;


            try {

                data =
                    JSON.parse(text);

            } catch (e) {

                throw new Error(
                    "Server returned an invalid response."
                );
            }


            // -------------------------------------------------
            // BACKEND ERROR
            // -------------------------------------------------

            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Unable to create Razorpay order."
                );
            }


            if (!data.success) {

                throw new Error(
                    data.message ||
                    "Unable to create Razorpay order."
                );
            }


            // -------------------------------------------------
            // CHECK ORDER ID
            // -------------------------------------------------

            if (!data.orderId) {

                throw new Error(
                    "Razorpay Order ID was not returned."
                );
            }


            if (!data.keyId) {

                throw new Error(
                    "Razorpay Key ID was not returned."
                );
            }


            if (!data.amount) {

                throw new Error(
                    "Payment amount was not returned."
                );
            }


            console.log(
                "Razorpay Order ID:",
                data.orderId
            );


            console.log(
                "Amount:",
                data.amount
            );


            console.log(
                "Key ID:",
                data.keyId
            );


            // -------------------------------------------------
            // OPEN RAZORPAY
            // -------------------------------------------------

            openRazorpay(data);


        } catch (error) {


            console.error(
                "PAYMENT ERROR:",
                error
            );


            showError(
                error.message
            );


            resetButton();
        }
    }


    // =====================================================
    // OPEN RAZORPAY
    // =====================================================

    function openRazorpay(data) {


        console.log(
            "Opening Razorpay Checkout..."
        );


        const options = {

            key:
                data.keyId,

            amount:
                data.amount,

            currency:
                data.currency || "INR",

            name:
                "Sri Krishna Collection",

            description:
                "Sri Krishna Collection Order",

            order_id:
                data.orderId,


            // -------------------------------------------------
            // PREFILL
            // -------------------------------------------------

            prefill: {

                name:
                    "Sri Krishna Collection",

                email:
                    "",

                contact:
                    ""
            },


            // -------------------------------------------------
            // THEME
            // -------------------------------------------------

            theme: {

                color:
                    "#176b68"
            },


            // -------------------------------------------------
            // PAYMENT HANDLER
            // -------------------------------------------------

            handler:
                function (response) {

                    console.log(
                        "Razorpay payment response:",
                        response
                    );


                    verifyPayment(
                        response
                    );
                },


            // -------------------------------------------------
            // CLOSE
            // -------------------------------------------------

            modal: {

                ondismiss:
                    function () {

                        console.log(
                            "Razorpay window closed."
                        );

                        resetButton();
                    }
            }

        };


        try {


            const razorpay =
                new Razorpay(
                    options
                );


            // =================================================
            // PAYMENT FAILED
            // =================================================

            razorpay.on(
                "payment.failed",
                function (response) {


                    console.error(
                        "Razorpay payment failed:",
                        response
                    );


                    let message =
                        "Payment failed.";


                    if (
                        response &&
                        response.error &&
                        response.error.description
                    ) {

                        message =
                            response.error.description;
                    }


                    showError(
                        message
                    );


                    resetButton();
                }
            );


            // =================================================
            // OPEN
            // =================================================

            razorpay.open();


            console.log(
                "Razorpay.open() executed."
            );


        } catch (error) {


            console.error(
                "Unable to open Razorpay:",
                error
            );


            showError(
                error.message
            );


            resetButton();
        }
    }


    // =====================================================
    // VERIFY PAYMENT
    // =====================================================

    async function verifyPayment(
        razorpayResponse
    ) {


        try {


            payButton.disabled =
                true;


            payButton.innerHTML =
                '<i class="fa-solid fa-spinner fa-spin"></i> Verifying Payment...';


            console.log(
                "Verifying payment..."
            );


            const response =
                await fetch(
                    "/Krishna/payment/verify",
                    {

                        method: "POST",

                        headers: {

                            "Content-Type":
                                "application/json",

                            "Accept":
                                "application/json"
                        },

                        credentials:
                            "same-origin",

                        body:
                            JSON.stringify({

                                razorpay_payment_id:
                                    razorpayResponse
                                        .razorpay_payment_id,

                                razorpay_order_id:
                                    razorpayResponse
                                        .razorpay_order_id,

                                razorpay_signature:
                                    razorpayResponse
                                        .razorpay_signature
                            })
                    }
                );


            const text =
                await response.text();


            console.log(
                "Verification response:",
                text
            );


            let data;


            try {

                data =
                    JSON.parse(text);

            } catch (e) {

                throw new Error(
                    "Invalid verification response."
                );
            }


            if (!response.ok ||
                !data.success) {

                throw new Error(
                    data.message ||
                    "Payment verification failed."
                );
            }


            console.log(
                "Payment verified successfully."
            );


            // =================================================
            // SUCCESS
            // =================================================

            window.location.href =
                "/Krishna/order-success";


        } catch (error) {


            console.error(
                "Verification error:",
                error
            );


            showError(
                error.message
            );


            resetButton();
        }
    }


    // =====================================================
    // ERROR
    // =====================================================

    function showError(message) {

        if (!errorBox) {
            return;
        }


        errorBox.textContent =
            message;


        errorBox.style.display =
            "block";
    }


    // =====================================================
    // CLEAR ERROR
    // =====================================================

    function clearError() {

        if (!errorBox) {
            return;
        }


        errorBox.textContent =
            "";

        errorBox.style.display =
            "none";
    }


    // =====================================================
    // RESET BUTTON
    // =====================================================

    function resetButton() {

        payButton.disabled =
            false;


        payButton.innerHTML =
            '<i class="fa-solid fa-lock"></i> Continue to Razorpay';
    }

});