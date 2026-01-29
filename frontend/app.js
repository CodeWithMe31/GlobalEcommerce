
let token = null;
let role = null;

function showLoader(show=true) {
  loader.classList.toggle("hidden", !show);
}

function showError(msg) {
  errorBox.textContent = msg;
  errorBox.classList.remove("hidden");
  setTimeout(() => errorBox.classList.add("hidden"), 3000);
}

function showPage(id) {
  document.querySelectorAll(".page").forEach(p => p.classList.remove("active"));
  document.getElementById(id).classList.add("active");
}

function login() {
  showLoader(true);

  // DEMO: simulate backend login
  setTimeout(() => {
    token = "demo-jwt";
    role = email.value === "admin@test.com" ? "ADMIN" : "USER";

    if (role !== "ADMIN") {
      document.getElementById("adminMenu").style.display = "none";
    }

    showLoader(false);
    showPage("products");
  }, 1000);
}

function loadProducts() {
  showLoader(true);
  setTimeout(() => {
    productGrid.innerHTML = "";
    for (let i = 1; i <= 6; i++) {
      productGrid.innerHTML += `
        <div class="form-card">
          <strong>Product ${i}</strong><br/>
          $${i*10}
        </div>`;
    }
    showLoader(false);
  }, 800);
}

function loadOrders() {
  showLoader(true);
  setTimeout(() => {
    orderList.innerHTML = "<div class='form-card'>Order #101 – SHIPPED</div>";
    showLoader(false);
  }, 800);
}
