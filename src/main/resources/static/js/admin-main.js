toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": false,
    "progressBar": false,
    "positionClass": "toast-top-center",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "2000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}

const btnLogout = document.getElementById("btn-logout");
btnLogout.addEventListener("click", async (e) => {
    try {
        await axios.post("/api/admin/logout")

        toastr.success("Đăng xuất thành công");
        setTimeout(() => {
            window.location.href = "/admin/login"
        }, 1500)
    } catch (e) {
        toastr.error("Đăng xuất thất bại");
        console.log(e)
    }
})