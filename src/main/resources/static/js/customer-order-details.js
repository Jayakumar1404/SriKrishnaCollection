document.addEventListener("DOMContentLoaded", function () {

    /*
     * =====================================================
     * ORDER DETAILS PAGE
     * =====================================================
     */

    console.log("Customer Order Details loaded");


    /*
     * =====================================================
     * STATUS
     * =====================================================
     */

    const statusElement =
        document.querySelector("[data-order-status]");

    if (statusElement) {

        const status =
            statusElement
                .getAttribute("data-order-status")
                .toUpperCase();

        statusElement.classList.add(
            "status-" + status.toLowerCase()
        );
    }


    /*
     * =====================================================
     * IMAGE ERROR HANDLING
     * =====================================================
     */

    const productImages =
        document.querySelectorAll(".item-image img");

    productImages.forEach(function (image) {

        image.addEventListener("error", function () {

            this.style.display = "none";

            const parent =
                this.parentElement;

            const icon =
                document.createElement("i");

            icon.className =
                "fa-solid fa-image";

            parent.appendChild(icon);
        });

    });


    /*
     * =====================================================
     * BACK BUTTON
     * =====================================================
     */

    const backButton =
        document.querySelector(".back-button");

    if (backButton) {

        backButton.addEventListener(
            "click",
            function () {

                console.log(
                    "Returning to My Orders"
                );

            }
        );
    }


    /*
     * =====================================================
     * PRINT ORDER
     * =====================================================
     */

    const printButton =
        document.querySelector("#printOrder");

    if (printButton) {

        printButton.addEventListener(
            "click",
            function () {

                window.print();

            }
        );
    }

});
document.addEventListener("DOMContentLoaded", function () {

    const timeline =
        document.querySelector(".order-timeline");

    if (!timeline) {
        return;
    }

    const statusElement =
        document.querySelector("[data-order-status]");

    let status = "PLACED";

    if (statusElement) {
        status =
            statusElement
                .getAttribute("data-order-status")
                .toUpperCase();
    }

    const progressMap = {

        "PLACED": "0%",

        "CONFIRMED": "25%",

        "PACKED": "50%",

        "SHIPPED": "75%",

        "DELIVERED": "100%"

    };

    timeline.style.setProperty(
        "--progress",
        progressMap[status] || "0%"
    );


    /*
     * Mark current step
     */

    const steps =
        timeline.querySelectorAll(".timeline-step");

    const statusIndex = {

        "PLACED": 0,

        "CONFIRMED": 1,

        "PACKED": 2,

        "SHIPPED": 3,

        "DELIVERED": 4

    };

    const currentIndex =
        statusIndex[status];


    steps.forEach(function (step, index) {

        if (index === currentIndex) {

            step.classList.add("current");

        }

    });

});