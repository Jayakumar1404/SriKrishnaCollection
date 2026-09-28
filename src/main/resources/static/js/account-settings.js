document.addEventListener("DOMContentLoaded", function () {

    /* =====================================================
       ALERT CLOSE
       ===================================================== */

    const alertCloseButtons =
        document.querySelectorAll(".alert-close");

    alertCloseButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const alert =
                this.closest(".alert-box");

            if (alert) {
                alert.remove();
            }

        });

    });


    /* =====================================================
       AUTO HIDE ALERT
       ===================================================== */

    setTimeout(function () {

        const alerts =
            document.querySelectorAll(".alert-box");

        alerts.forEach(function (alert) {

            alert.classList.add("hide-alert");

            setTimeout(function () {

                alert.remove();

            }, 400);

        });

    }, 5000);


    /* =====================================================
       PROFILE IMAGE
       ===================================================== */

    const profileImage =
        document.getElementById("profileImage");

    const uploadButton =
        document.getElementById("uploadButton");

    const selectedFile =
        document.getElementById("selectedFile");

    const profilePreview =
        document.getElementById("profilePreview");

    const defaultPreview =
        document.getElementById("defaultPreview");


    if (profileImage) {

        profileImage.addEventListener(
            "change",
            function () {

                const file =
                    this.files[0];

                if (!file) {

                    uploadButton.disabled = true;

                    selectedFile.textContent = "";

                    return;
                }


                /* =========================================
                   FILE TYPE
                   ========================================= */

                const allowedTypes = [
                    "image/jpeg",
                    "image/png",
                    "image/webp"
                ];


                if (
                    !allowedTypes.includes(file.type)
                ) {

                    alert(
                        "Please select a JPG, PNG or WEBP image."
                    );

                    this.value = "";

                    uploadButton.disabled = true;

                    selectedFile.textContent = "";

                    return;
                }


                /* =========================================
                   FILE SIZE
                   ========================================= */

                if (
                    file.size > 5 * 1024 * 1024
                ) {

                    alert(
                        "Image size must be less than 5 MB."
                    );

                    this.value = "";

                    uploadButton.disabled = true;

                    selectedFile.textContent = "";

                    return;
                }


                /* =========================================
                   FILE NAME
                   ========================================= */

                selectedFile.innerHTML =
                    '<i class="fa-solid fa-file-image"></i> '
                    + escapeHtml(file.name);


                uploadButton.disabled = false;


                /* =========================================
                   LIVE PREVIEW
                   ========================================= */

                const reader =
                    new FileReader();

                reader.onload =
                    function (event) {

                        if (profilePreview) {

                            profilePreview.src =
                                event.target.result;

                            profilePreview.style.display =
                                "block";
                        }


                        if (defaultPreview) {

                            defaultPreview.style.display =
                                "none";
                        }

                    };

                reader.readAsDataURL(file);

            }
        );

    }


    /* =====================================================
       PASSWORD SHOW / HIDE
       ===================================================== */

    const passwordToggles =
        document.querySelectorAll(".password-toggle");


    passwordToggles.forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                const targetId =
                    this.getAttribute("data-target");

                const input =
                    document.getElementById(targetId);

                const icon =
                    this.querySelector("i");


                if (!input) {
                    return;
                }


                if (input.type === "password") {

                    input.type = "text";

                    icon.classList.remove(
                        "fa-eye"
                    );

                    icon.classList.add(
                        "fa-eye-slash"
                    );

                } else {

                    input.type = "password";

                    icon.classList.remove(
                        "fa-eye-slash"
                    );

                    icon.classList.add(
                        "fa-eye"
                    );

                }

            }
        );

    });


    /* =====================================================
       MOBILE NUMBER
       ===================================================== */

    const mobile =
        document.getElementById("mobile");


    if (mobile) {

        mobile.addEventListener(
            "input",
            function () {

                this.value =
                    this.value
                        .replace(/\D/g, "")
                        .substring(0, 10);

            }
        );

    }


    /* =====================================================
       PINCODE
       ===================================================== */

    const pincode =
        document.getElementById("pincode");


    if (pincode) {

        pincode.addEventListener(
            "input",
            function () {

                this.value =
                    this.value
                        .replace(/\D/g, "")
                        .substring(0, 6);

            }
        );

    }


    /* =====================================================
       NEW PASSWORD STRENGTH
       ===================================================== */

    const newPassword =
        document.getElementById("newPassword");

    const passwordStrength =
        document.getElementById("passwordStrength");


    if (newPassword) {

        newPassword.addEventListener(
            "input",
            function () {

                const password =
                    this.value;

                if (!passwordStrength) {
                    return;
                }


                if (password.length === 0) {

                    passwordStrength.innerHTML = "";

                    updatePasswordRules("");

                    return;
                }


                let score = 0;


                if (password.length >= 8) {
                    score++;
                }

                if (/[A-Z]/.test(password)) {
                    score++;
                }

                if (/[0-9]/.test(password)) {
                    score++;
                }

                if (/[!@#$%^&*]/.test(password)) {
                    score++;
                }


                let text = "";

                if (score <= 1) {

                    text =
                        '<span class="strength weak">'
                        + "Weak password"
                        + "</span>";

                } else if (score === 2) {

                    text =
                        '<span class="strength medium">'
                        + "Medium password"
                        + "</span>";

                } else {

                    text =
                        '<span class="strength strong">'
                        + "Strong password"
                        + "</span>";

                }


                passwordStrength.innerHTML =
                    text;


                updatePasswordRules(
                    password
                );


                checkPasswordMatch();

            }
        );

    }


    /* =====================================================
       CONFIRM PASSWORD
       ===================================================== */

    const confirmPassword =
        document.getElementById(
            "confirmPassword"
        );

    if (confirmPassword) {

        confirmPassword.addEventListener(
            "input",
            function () {

                checkPasswordMatch();

            }
        );

    }


    function checkPasswordMatch() {

        const match =
            document.getElementById(
                "passwordMatch"
            );


        if (!match ||
            !newPassword ||
            !confirmPassword) {

            return;
        }


        if (
            confirmPassword.value.length === 0
        ) {

            match.innerHTML = "";

            return;
        }


        if (
            newPassword.value ===
            confirmPassword.value
        ) {

            match.innerHTML =
                '<span class="match-success">'
                + '<i class="fa-solid fa-check"></i> '
                + "Passwords match"
                + "</span>";

        } else {

            match.innerHTML =
                '<span class="match-error">'
                + '<i class="fa-solid fa-xmark"></i> '
                + "Passwords do not match"
                + "</span>";

        }

    }


    /* =====================================================
       PASSWORD RULES
       ===================================================== */

    function updatePasswordRules(
        password
    ) {

        const length =
            document.getElementById(
                "ruleLength"
            );

        const upper =
            document.getElementById(
                "ruleUpper"
            );

        const number =
            document.getElementById(
                "ruleNumber"
            );


        if (length) {

            setRule(
                length,
                password.length >= 8
            );

        }


        if (upper) {

            setRule(
                upper,
                /[A-Z]/.test(password)
            );

        }


        if (number) {

            setRule(
                number,
                /[0-9]/.test(password)
            );

        }

    }


    function setRule(
        element,
        valid
    ) {

        const icon =
            element.querySelector("i");


        if (valid) {

            element.classList.add(
                "rule-valid"
            );

            if (icon) {

                icon.classList.remove(
                    "fa-circle"
                );

                icon.classList.add(
                    "fa-circle-check"
                );

            }

        } else {

            element.classList.remove(
                "rule-valid"
            );

            if (icon) {

                icon.classList.remove(
                    "fa-circle-check"
                );

                icon.classList.add(
                    "fa-circle"
                );

            }

        }

    }


    /* =====================================================
       PASSWORD FORM VALIDATION
       ===================================================== */

    const passwordForm =
        document.getElementById(
            "passwordForm"
        );


    if (passwordForm) {

        passwordForm.addEventListener(
            "submit",
            function (event) {

                const current =
                    document.getElementById(
                        "currentPassword"
                    );

                const newPass =
                    document.getElementById(
                        "newPassword"
                    );

                const confirm =
                    document.getElementById(
                        "confirmPassword"
                    );


                if (!current.value) {

                    event.preventDefault();

                    alert(
                        "Please enter your current password."
                    );

                    current.focus();

                    return;
                }


                if (newPass.value.length < 8) {

                    event.preventDefault();

                    alert(
                        "New password must contain at least 8 characters."
                    );

                    newPass.focus();

                    return;
                }


                if (
                    !/[A-Z]/.test(
                        newPass.value
                    )
                ) {

                    event.preventDefault();

                    alert(
                        "New password must contain at least one uppercase letter."
                    );

                    newPass.focus();

                    return;
                }


                if (
                    !/[0-9]/.test(
                        newPass.value
                    )
                ) {

                    event.preventDefault();

                    alert(
                        "New password must contain at least one number."
                    );

                    newPass.focus();

                    return;
                }


                if (
                    newPass.value !==
                    confirm.value
                ) {

                    event.preventDefault();

                    alert(
                        "New password and confirm password do not match."
                    );

                    confirm.focus();

                    return;
                }

            }
        );

    }


    /* =====================================================
       PROFILE FORM VALIDATION
       ===================================================== */

    const profileForm =
        document.getElementById(
            "profileForm"
        );


    if (profileForm) {

        profileForm.addEventListener(
            "submit",
            function (event) {

                const mobileValue =
                    mobile
                        ? mobile.value.trim()
                        : "";

                const pincodeValue =
                    pincode
                        ? pincode.value.trim()
                        : "";


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
                    pincodeValue &&
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

            }
        );

    }


    /* =====================================================
       SETTINGS MENU SCROLL
       ===================================================== */

    const menuItems =
        document.querySelectorAll(
            ".settings-menu-item"
        );


    menuItems.forEach(function (item) {

        item.addEventListener(
            "click",
            function () {

                menuItems.forEach(
                    function (menu) {

                        menu.classList.remove(
                            "active"
                        );

                    }
                );


                this.classList.add(
                    "active"
                );

            }
        );

    });


    /* =====================================================
       ESCAPE HTML
       ===================================================== */

    function escapeHtml(
        text
    ) {

        const div =
            document.createElement(
                "div"
            );

        div.textContent =
            text;

        return div.innerHTML;

    }

});