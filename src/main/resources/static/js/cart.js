document.addEventListener("DOMContentLoaded", function () {

    const quantityControls =
        document.querySelectorAll(".quantity-control");


    quantityControls.forEach(function (control) {

        const minusButton =
            control.querySelector(".minus-btn");

        const plusButton =
            control.querySelector(".plus-btn");

        const input =
            control.querySelector(".quantity-input");

        const form =
            control.closest(".quantity-form");

        const cartItem =
            control.closest(".cart-item");


        if (!input || !form || !cartItem) {
            return;
        }


        // =====================================================
        // PRICE
        // =====================================================

        const unitPriceElement =
            cartItem.querySelector(".unit-price span");

        const itemTotalElement =
            cartItem.querySelector(".item-total span");


        const unitPrice =
            parseFloat(
                unitPriceElement.textContent
                    .replace(/,/g, "")
                    .trim()
            ) || 0;


        // =====================================================
        // UPDATE SCREEN
        // =====================================================

        function updateScreen() {

            let quantity =
                parseInt(input.value) || 1;


            const max =
                parseInt(
                    input.getAttribute("max")
                );


            if (quantity < 1) {

                quantity = 1;

            }


            if (!isNaN(max)
                    && quantity > max) {

                quantity = max;

            }


            input.value =
                quantity;


            // Calculate item total

            const itemTotal =
                unitPrice * quantity;


            itemTotalElement.textContent =
                itemTotal.toLocaleString(
                    "en-IN",
                    {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    }
                );


            // Update entire cart summary

            updateCartSummary();
        }


        // =====================================================
        // SAVE TO DATABASE
        // =====================================================

        async function saveQuantity() {

            const formData =
                new FormData(form);


            try {

                const response =
                    await fetch(
                        form.action,
                        {
                            method: "POST",
                            body: formData
                        }
                    );


                if (!response.ok) {

                    throw new Error(
                        "Unable to update cart."
                    );

                }


                console.log(
                    "Cart quantity saved successfully."
                );


            } catch (error) {

                console.error(error);

                alert(
                    "Unable to update cart. Please try again."
                );

            }

        }


        // =====================================================
        // PLUS
        // =====================================================

        plusButton.addEventListener(
            "click",
            async function () {

                let value =
                    parseInt(input.value) || 1;


                const max =
                    parseInt(
                        input.getAttribute("max")
                    );


                if (!isNaN(max)
                        && value >= max) {

                    input.value =
                        max;

                    updateScreen();

                    return;
                }


                input.value =
                    value + 1;


                // Instant UI update

                updateScreen();


                // Save database

                await saveQuantity();

            }
        );


        // =====================================================
        // MINUS
        // =====================================================

        minusButton.addEventListener(
            "click",
            async function () {

                let value =
                    parseInt(input.value) || 1;


                if (value <= 1) {

                    input.value = 1;

                    updateScreen();

                    return;
                }


                input.value =
                    value - 1;


                // Instant UI update

                updateScreen();


                // Save database

                await saveQuantity();

            }
        );


        // =====================================================
        // MANUAL INPUT
        // =====================================================

        input.addEventListener(
            "change",
            async function () {

                updateScreen();

                await saveQuantity();

            }
        );


        // Initial calculation

        updateScreen();

    });


    // =========================================================
    // CART SUMMARY
    // =========================================================

    function updateCartSummary() {

        let subtotal = 0;

        let totalItems = 0;


        const cartItems =
            document.querySelectorAll(
                ".cart-item"
            );


        cartItems.forEach(function (item) {

            const quantityInput =
                item.querySelector(
                    ".quantity-input"
                );


            const unitPriceElement =
                item.querySelector(
                    ".unit-price span"
                );


            if (!quantityInput
                    || !unitPriceElement) {

                return;
            }


            const quantity =
                parseInt(
                    quantityInput.value
                ) || 1;


            const price =
                parseFloat(
                    unitPriceElement.textContent
                        .replace(/,/g, "")
                        .trim()
                ) || 0;


            subtotal +=
                price * quantity;


            totalItems +=
                quantity;

        });


        // =====================================================
        // ITEM COUNT
        // =====================================================

        const itemCount =
            document.querySelector(
                ".item-count strong"
            );


        if (itemCount) {

            itemCount.textContent =
                totalItems;

        }


        // =====================================================
        // SUBTOTAL
        // =====================================================

        document
            .querySelectorAll(".summary-line")
            .forEach(function (line) {

                const label =
                    line.querySelector("span");


                if (!label) {
                    return;
                }


                if (
                    label.textContent
                        .trim()
                        .toLowerCase()
                        === "subtotal"
                ) {

                    const amount =
                        line.querySelector(
                            "strong"
                        );


                    if (amount) {

                        amount.textContent =
                            "₹ " +
                            subtotal.toLocaleString(
                                "en-IN",
                                {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                }
                            );

                    }

                }

            });


        // =====================================================
        // TOTAL
        // =====================================================

        const finalTotal =
            document.querySelector(
                ".summary-total strong"
            );


        if (finalTotal) {

            finalTotal.textContent =
                "₹ " +
                subtotal.toLocaleString(
                    "en-IN",
                    {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    }
                );

        }

    }

});