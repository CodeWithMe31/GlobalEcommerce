
const API_BASE = "http://localhost:8080"; // Gateway URL
let token = null;
let role = null;
let userEmail = null;

function showLoader(show = true) {
    const loader = document.getElementById("loader");
    if (loader) loader.classList.toggle("hidden", !show);
}

function showError(msg) {
    const errorBox = document.getElementById("errorBox");
    errorBox.textContent = msg;
    errorBox.classList.remove("hidden");
    setTimeout(() => errorBox.classList.add("hidden"), 3000);
}

function showPage(id) {
    document.querySelectorAll(".page").forEach(p => p.classList.remove("active"));
    const targetPage = document.getElementById(id);
    if (targetPage) targetPage.classList.add("active");
}

// 1. Updated Login: Real API call to Auth Service via Gateway
async function login() {
    const emailInput = document.getElementById("email").value;
    const passwordInput = document.getElementById("password").value;

    if (!emailInput || !passwordInput) {
        showError("Please enter both email and password");
        return;
    }

    showLoader(true);
    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: emailInput, password: passwordInput })
        });

        if (!response.ok) throw new Error("Invalid credentials");

        const data = await response.json();
        token = data.token; // Store JWT
        userEmail = emailInput;

        // Extract role from JWT payload
        const payload = JSON.parse(atob(token.split('.')[1]));
        role = payload.role;

        // Admin menu visibility logic
        const adminMenu = document.getElementById("adminMenu");
        if (role !== "ADMIN") {
            adminMenu.style.display = "none";
        } else {
            adminMenu.style.display = "block";
        }

        showPage("products");
        loadProducts();
    } catch (err) {
        showError(err.message);
    } finally {
        showLoader(false);
    }
}

// 2. Updated Load Products: Fetches from Product Service
async function loadProducts() {
    showLoader(true);
    const productGrid = document.getElementById("productGrid");
    try {
        const response = await fetch(`${API_BASE}/products`);
        const products = await response.json();

        productGrid.innerHTML = products.map(p => `
            <div class="form-card">
                <strong>${p.name}</strong><br/>
                <small>${p.category}</small><br/>
                $${p.price}
                <button onclick="addToCart(${p.id}, '${p.name}', ${p.price})">Add to Cart</button>
            </div>
        `).join('');
    } catch (err) {
        showError("Failed to load products");
    } finally {
        showLoader(false);
    }
}

// Helper to add items to Cart Service
async function addToCart(productId, productName, price) {
    try {
        const response = await fetch(`${API_BASE}/cart`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Required by Gateway
            },
            body: JSON.stringify({ productId, productName, price, quantity: 1 })
        });
        if (response.ok) alert("Added to cart!");
    } catch (err) {
        showError("Could not add to cart");
    }
}

// 3. Updated Load Orders: Fetches from Order Service
async function loadOrders() {
    showLoader(true);
    const orderList = document.getElementById("orderList");
    try {
        const response = await fetch(`${API_BASE}/orders`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const orders = await response.json();

        if (orders.length === 0) {
            orderList.innerHTML = "<p>No orders found.</p>";
            return;
        }

        orderList.innerHTML = orders.map(o => `
            <div class="form-card">
                <strong>Order #${o.id}</strong> - Status: ${o.status}<br/>
                Total: $${o.totalAmount.toFixed(2)}
            </div>
        `).join('');
    } catch (err) {
        showError("Failed to load orders");
    } finally {
        showLoader(false);
    }
}
