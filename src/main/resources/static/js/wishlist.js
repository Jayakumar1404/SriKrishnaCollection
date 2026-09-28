document.addEventListener("DOMContentLoaded", function () {

    /* =====================================================
       WISHLIST PAGE INITIALIZATION
    ===================================================== */

    const emptyWishlist =
        document.querySelector(".empty-wishlist");

    const wishlistCards =
        document.querySelectorAll(".wishlist-card");

    const removeButtons =
        document.querySelectorAll(".remove-wishlist");

    const navLinks =
        document.querySelectorAll(".nav-links a");



    /* =====================================================
       FADE-IN ANIMATION
    ===================================================== */

    document.body.classList.add("page-loaded");



    /* =====================================================
       WISHLIST CARD ANIMATION
    ===================================================== */

    wishlistCards.forEach(function (card, index) {

        card.style.opacity = "0";

        card.style.transform =
            "translateY(25px)";


        setTimeout(function () {

            card.style.transition =
                "all 0.5s ease";

            card.style.opacity = "1";

            card.style.transform =
                "translateY(0)";

        }, 100 + (index * 100));

    });



    /* =====================================================
       REMOVE WISHLIST ITEM
    ===================================================== */

    removeButtons.forEach(function (button) {

        button.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                const card =
                    button.closest(".wishlist-card");

                if (!card) {
                    return;
                }


                const confirmed =
                    confirm(
                        "Remove this product from your wishlist?"
                    );


                if (!confirmed) {
                    return;
                }


                card.style.transition =
                    "all 0.4s ease";

                card.style.opacity = "0";

                card.style.transform =
                    "scale(0.9)";


                setTimeout(function () {

                    card.remove();

                    checkWishlistEmpty();

                }, 400);

            }
        );

    });



    /* =====================================================
       CHECK EMPTY WISHLIST
    ===================================================== */

    function checkWishlistEmpty() {

        const remainingCards =
            document.querySelectorAll(
                ".wishlist-card"
            );


        if (
            remainingCards.length === 0
            &&
            emptyWishlist
        ) {

            emptyWishlist.style.display =
                "block";

        }

    }



    /* =====================================================
       NAVIGATION HOVER EFFECT
    ===================================================== */

    navLinks.forEach(function (link) {

        link.addEventListener(
            "mouseenter",
            function () {

                link.style.transform =
                    "translateY(-1px)";

            }
        );


        link.addEventListener(
            "mouseleave",
            function () {

                link.style.transform =
                    "translateY(0)";

            }
        );

    });



    /* =====================================================
       GOLD BUTTON EFFECT
    ===================================================== */

    const goldButtons =
        document.querySelectorAll(
            ".gold-button, .promo-button"
        );


    goldButtons.forEach(function (button) {

        button.addEventListener(
            "mouseenter",
            function () {

                button.style.transform =
                    "translateY(-3px)";

            }
        );


        button.addEventListener(
            "mouseleave",
            function () {

                button.style.transform =
                    "translateY(0)";

            }
        );

    });



    /* =====================================================
       HEART HOVER EFFECT
    ===================================================== */

    const heart =
        document.querySelector(".empty-heart");


    if (heart) {

        heart.addEventListener(
            "mouseenter",
            function () {

                heart.style.transform =
                    "scale(1.08)";

            }
        );


        heart.addEventListener(
            "mouseleave",
            function () {

                heart.style.transform =
                    "scale(1)";

            }
        );

    }



    /* =====================================================
       SCROLL REVEAL
    ===================================================== */

    const revealElements =
        document.querySelectorAll(
            ".wishlist-heading, .empty-wishlist, .wishlist-promo"
        );


    function revealOnScroll() {

        const windowHeight =
            window.innerHeight;


        revealElements.forEach(function (element) {

            const elementTop =
                element.getBoundingClientRect().top;


            if (
                elementTop <
                windowHeight - 80
            ) {

                element.classList.add(
                    "revealed"
                );

            }

        });

    }


    window.addEventListener(
        "scroll",
        revealOnScroll
    );


    revealOnScroll();



    /* =====================================================
       SMOOTH SCROLL
    ===================================================== */

    document.querySelectorAll(
        'a[href^="#"]'
    ).forEach(function (link) {

        link.addEventListener(
            "click",
            function (event) {

                const targetId =
                    link.getAttribute("href");


                if (
                    targetId === "#"
                    ||
                    targetId.length <= 1
                ) {

                    return;

                }


                const target =
                    document.querySelector(
                        targetId
                    );


                if (target) {

                    event.preventDefault();

                    target.scrollIntoView({
                        behavior: "smooth"
                    });

                }

            }
        );

    });



    /* =====================================================
       PAGE VISIBILITY
    ===================================================== */

    document.addEventListener(
        "visibilitychange",
        function () {

            if (
                document.visibilityState ===
                "visible"
            ) {

                document.body.classList.add(
                    "page-active"
                );

            }

        }
    );

});