/* ==========================================
   SRI KRISHNA COLLECTION
   ADMIN DASHBOARD JS
========================================== */


document.addEventListener(
    "DOMContentLoaded",
    function () {


        /* ======================================
           SIDEBAR
        ====================================== */

        const menu =
            document.getElementById(
                "sidebarMenu"
            );


        const upButton =
            document.getElementById(
                "menuScrollUp"
            );


        const downButton =
            document.getElementById(
                "menuScrollDown"
            );


        const sidebar =
            document.querySelector(
                ".sidebar"
            );


        const overlay =
            document.getElementById(
                "sidebarOverlay"
            );


        const mobileMenuButton =
            document.getElementById(
                "mobileMenuButton"
            );



        /* ======================================
           SCROLL UP
        ====================================== */

        if (upButton && menu) {

            upButton.addEventListener(
                "click",
                function () {

                    menu.scrollBy({

                        top: -180,

                        behavior: "smooth"

                    });

                }
            );

        }



        /* ======================================
           SCROLL DOWN
        ====================================== */

        if (downButton && menu) {

            downButton.addEventListener(
                "click",
                function () {

                    menu.scrollBy({

                        top: 180,

                        behavior: "smooth"

                    });

                }
            );

        }



        /* ======================================
           SCROLL BUTTON STATE
        ====================================== */

        function updateScrollButtons() {

            if (!menu) {
                return;
            }


            const atTop =
                menu.scrollTop <= 5;


            const atBottom =
                menu.scrollTop +
                menu.clientHeight >=
                menu.scrollHeight - 5;


            if (upButton) {

                upButton.classList.toggle(
                    "disabled",
                    atTop
                );

            }


            if (downButton) {

                downButton.classList.toggle(
                    "disabled",
                    atBottom
                );

            }

        }


        if (menu) {

            menu.addEventListener(
                "scroll",
                updateScrollButtons
            );


            updateScrollButtons();

        }



        /* ======================================
           ACTIVE MENU
        ====================================== */

        const currentPath =
            window.location.pathname;


        document
            .querySelectorAll(
                ".sidebar-menu a"
            )
            .forEach(
                function (link) {

                    const linkPath =
                        new URL(
                            link.href,
                            window.location.origin
                        ).pathname;


                    if (
                        linkPath ===
                        currentPath
                    ) {

                        link.classList.add(
                            "active"
                        );

                    }

                }
            );



        /* ======================================
           MOBILE SIDEBAR
        ====================================== */

        function openMobileSidebar() {

            if (sidebar) {

                sidebar.classList.add(
                    "mobile-open"
                );

            }


            if (overlay) {

                overlay.classList.add(
                    "active"
                );

            }

        }


        function closeMobileSidebar() {

            if (sidebar) {

                sidebar.classList.remove(
                    "mobile-open"
                );

            }


            if (overlay) {

                overlay.classList.remove(
                    "active"
                );
            }

            if (mobileMenuButton) {
                mobileMenuButton.setAttribute("aria-expanded", "false");

            }

        }


        if (mobileMenuButton) {
            mobileMenuButton.addEventListener("click", function (event) {
                event.preventDefault();
                openMobileSidebar();
                mobileMenuButton.setAttribute("aria-expanded", "true");
            });
        }

        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape") closeMobileSidebar();
        });


        if (overlay) {

            overlay.addEventListener(
                "click",
                closeMobileSidebar
            );

        }


        document
            .querySelectorAll(
                ".sidebar-menu a"
            )
            .forEach(
                function (link) {

                    link.addEventListener(
                        "click",
                        function () {

                            closeMobileSidebar();

                        }
                    );

                }
            );

    });



/* ==========================================
   BACK BUTTON
========================================== */

function goBack() {


    if (window.history.length > 1) {

        window.history.back();

    } else {

        window.location.href =
            "/admin/dashboard";

    }

}