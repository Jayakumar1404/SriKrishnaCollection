/* =========================================================
   SRI KRISHNA COLLECTION
   CUSTOMER HOME JAVASCRIPT
   UI / INTERACTION ONLY

   IMPORTANT:
   This script does NOT modify any href or endpoint.
========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    /* =====================================================
       NAVBAR SHADOW
    ===================================================== */

    const navbar = document.querySelector(".main-navbar");

    if (navbar) {

        function updateNavbar() {

            if (window.scrollY > 80) {
                navbar.classList.add("shadow");
            } else {
                navbar.classList.remove("shadow");
            }

        }

        window.addEventListener(
            "scroll",
            updateNavbar,
            { passive: true }
        );

        updateNavbar();
    }


    /* =====================================================
       PRODUCT SEARCH
    ===================================================== */

    const searchBox =
        document.querySelector(".search-box input");

    const products =
        document.querySelectorAll(".product-card");

    if (searchBox && products.length > 0) {

        searchBox.addEventListener("input", function () {

            const value =
                this.value
                    .trim()
                    .toLowerCase();

            products.forEach(function (product) {

                const text =
                    product.innerText.toLowerCase();

                const wrapper =
                    product.closest(
                        ".col-12, .col-sm-6, .col-md-6, .col-lg-3, .col-xl-3"
                    );

                const target =
                    wrapper || product;

                if (
                    value === "" ||
                    text.includes(value)
                ) {

                    target.style.display = "";

                    product.classList.remove(
                        "search-hidden"
                    );

                } else {

                    target.style.display = "none";

                    product.classList.add(
                        "search-hidden"
                    );
                }

            });

        });

    }


    /* =====================================================
       WISHLIST BUTTON
    ===================================================== */

    document
        .querySelectorAll(".wishlist-btn")
        .forEach(function (button) {

            button.addEventListener(
                "click",
                function (event) {

                    event.preventDefault();

                    const icon =
                        this.querySelector("i");

                    if (!icon) {
                        return;
                    }

                    const active =
                        this.classList.contains(
                            "wishlist-active"
                        );

                    if (!active) {

                        icon.classList.remove(
                            "fa-regular"
                        );

                        icon.classList.add(
                            "fa-solid"
                        );

                        this.classList.add(
                            "wishlist-active"
                        );

                        showToast(
                            "Added to Wishlist ❤️"
                        );

                    } else {

                        icon.classList.remove(
                            "fa-solid"
                        );

                        icon.classList.add(
                            "fa-regular"
                        );

                        this.classList.remove(
                            "wishlist-active"
                        );

                        showToast(
                            "Removed from Wishlist"
                        );
                    }

                }
            );

        });


    /* =====================================================
       NEWSLETTER
    ===================================================== */

    const newsletterButton =
        document.querySelector(
            ".newsletter-form button"
        );

    const newsletterInput =
        document.querySelector(
            ".newsletter-form input"
        );

    if (
        newsletterButton &&
        newsletterInput
    ) {

        newsletterButton.addEventListener(
            "click",
            function () {

                const email =
                    newsletterInput.value.trim();

                if (email === "") {

                    showAlert(
                        "warning",
                        "Email Required",
                        "Please enter your email."
                    );

                    newsletterInput.focus();

                    return;
                }

                const emailPattern =
                    /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

                if (!emailPattern.test(email)) {

                    showAlert(
                        "warning",
                        "Invalid Email",
                        "Please enter a valid email address."
                    );

                    newsletterInput.focus();

                    return;
                }

                showAlert(
                    "success",
                    "Subscribed",
                    "Thank you for subscribing!"
                );

                newsletterInput.value = "";

            }
        );

    }


    /* =====================================================
       SCROLL TO TOP
    ===================================================== */

    let topButton =
        document.getElementById("scrollTop");

    /*
       Your HTML already contains #scrollTop.
       Therefore don't create another one.
    */

    if (topButton) {

        function updateScrollButton() {

            if (window.scrollY > 400) {

                topButton.classList.add(
                    "show"
                );

            } else {

                topButton.classList.remove(
                    "show"
                );
            }

        }

        window.addEventListener(
            "scroll",
            updateScrollButton,
            { passive: true }
        );

        updateScrollButton();


        topButton.addEventListener(
            "click",
            function () {

                window.scrollTo({
                    top: 0,
                    behavior: "smooth"
                });

            }
        );

    }


    /* =====================================================
       CARD HOVER / TOUCH FEEDBACK
    ===================================================== */

    document
        .querySelectorAll(
            ".category-card, .product-card, .why-card"
        )
        .forEach(function (card) {

            card.addEventListener(
                "mouseenter",
                function () {

                    this.classList.add(
                        "is-hovered"
                    );

                }
            );

            card.addEventListener(
                "mouseleave",
                function () {

                    this.classList.remove(
                        "is-hovered"
                    );

                }
            );

        });


    /* =====================================================
       MOBILE NAVBAR
       Bootstrap handles the actual collapse.
       This only closes it after selecting a link.
    ===================================================== */

    const navLinks =
        document.querySelectorAll(
            "#mainNav .nav-link"
        );

    const navCollapse =
        document.getElementById(
            "mainNav"
        );

    navLinks.forEach(function (link) {

        link.addEventListener(
            "click",
            function () {

                if (
                    window.innerWidth < 992 &&
                    navCollapse &&
                    navCollapse.classList.contains(
                        "show"
                    )
                ) {

                    const toggler =
                        document.querySelector(
                            ".navbar-toggler"
                        );

                    if (toggler) {
                        toggler.click();
                    }

                }

            }
        );

    });


    /* =====================================================
       ACTIVE NAV LINK
       VISUAL ONLY
       DOES NOT CHANGE HREF
    ===================================================== */

    const currentPath =
        window.location.pathname;

    document
        .querySelectorAll(
            ".main-navbar .nav-link"
        )
        .forEach(function (link) {

            const href =
                link.getAttribute("href");

            if (
                href &&
                href !== "#" &&
                currentPath.includes(
                    href
                )
            ) {

                link.classList.add(
                    "active"
                );

            }

        });


    /* =====================================================
       HELPERS
    ===================================================== */

    function showToast(message) {

        if (
            typeof Swal !== "undefined"
        ) {

            Swal.fire({

                toast: true,

                position: "top-end",

                showConfirmButton: false,

                timer: 2000,

                timerProgressBar: true,

                icon: "success",

                title: message

            });

        } else {

            console.log(message);

        }

    }


    function showAlert(
        icon,
        title,
        text
    ) {

        if (
            typeof Swal !== "undefined"
        ) {

            Swal.fire({

                icon: icon,

                title: title,

                text: text,

                confirmButtonColor:
                    "#063b2d"

            });

        } else {

            alert(
                title + "\n\n" + text
            );

        }

    }

});
/* =========================================================
   NEW ARRIVALS - INFINITE RIGHT TO LEFT SLIDER
========================================================= */
document.addEventListener("DOMContentLoaded", function () {

    const track = document.querySelector(".arrival-track");

    if (!track) {
        return;
    }

    const originalItems = Array.from(
        track.querySelectorAll(".arrival-item")
    );

    if (originalItems.length < 2) {
        return;
    }

    /*
     * Create exactly ONE duplicate set.
     *
     * Original:
     * A B C D
     *
     * Clone:
     * A B C D
     *
     * Result:
     * A B C D A B C D
     */

    originalItems.forEach(function (item) {

        const clone = item.cloneNode(true);

        clone.classList.add("arrival-clone");

        clone.setAttribute(
            "aria-hidden",
            "true"
        );

        track.appendChild(clone);

    });

});