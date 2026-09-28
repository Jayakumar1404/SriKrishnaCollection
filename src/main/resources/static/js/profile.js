document.addEventListener(
    "DOMContentLoaded",
    function () {

        const form =
            document.getElementById(
                "profileForm"
            );

        if (!form) {
            return;
        }

        const firstName =
            document.getElementById(
                "firstName"
            );

        const lastName =
            document.getElementById(
                "lastName"
            );

        const mobile =
            document.getElementById(
                "mobile"
            );

        const pincode =
            document.getElementById(
                "pincode"
            );

        const saveButton =
            document.getElementById(
                "saveProfileButton"
            );


        // =================================================
        // MOBILE
        // =================================================

        if (mobile) {

            mobile.addEventListener(
                "input",
                function () {

                    this.value =
                        this.value
                            .replace(
                                /\D/g,
                                ""
                            )
                            .slice(
                                0,
                                10
                            );

                }
            );
        }


        // =================================================
        // PINCODE
        // =================================================

        if (pincode) {

            pincode.addEventListener(
                "input",
                function () {

                    this.value =
                        this.value
                            .replace(
                                /\D/g,
                                ""
                            )
                            .slice(
                                0,
                                6
                            );

                }
            );
        }


        // =================================================
        // NAME
        // =================================================

        function cleanName(input) {

            if (!input) {
                return;
            }

            input.addEventListener(
                "input",
                function () {

                    this.value =
                        this.value.replace(
                            /[^a-zA-Z\s.'-]/g,
                            ""
                        );

                }
            );
        }

        cleanName(firstName);

        cleanName(lastName);


        // =================================================
        // FORM VALIDATION
        // =================================================

        form.addEventListener(
            "submit",
            function (event) {

                const firstNameValue =
                    firstName.value.trim();

                const lastNameValue =
                    lastName.value.trim();

                const mobileValue =
                    mobile.value.trim();

                const pincodeValue =
                    pincode.value.trim();


                if (
                    firstNameValue.length < 2
                ) {

                    event.preventDefault();

                    alert(
                        "Please enter a valid first name."
                    );

                    firstName.focus();

                    return;
                }


                if (
                    lastNameValue.length < 1
                ) {

                    event.preventDefault();

                    alert(
                        "Please enter your last name."
                    );

                    lastName.focus();

                    return;
                }


                if (
                    !/^\d{10}$/.test(
                        mobileValue
                    )
                ) {

                    event.preventDefault();

                    alert(
                        "Mobile number must contain exactly 10 digits."
                    );

                    mobile.focus();

                    return;
                }


                if (
                    !/^\d{6}$/.test(
                        pincodeValue
                    )
                ) {

                    event.preventDefault();

                    alert(
                        "Pincode must contain exactly 6 digits."
                    );

                    pincode.focus();

                    return;
                }


                // =========================================
                // LOADING STATE
                // =========================================

                if (saveButton) {

                    saveButton.disabled =
                        true;

                    saveButton.innerHTML =
                        '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';
                }

            }
        );

    }
);