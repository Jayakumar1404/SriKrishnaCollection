/**
 * SRI KRISHNA COLLECTION — main.js
 * Vanilla ES6. No build step required.
 * Sections: Preloader, Particles/Feathers, Navbar, Search, Sliders,
 *           Cart/Wishlist state, Quick View, Countdown, Reveal-on-scroll,
 *           Theme toggle, Back-to-top, Newsletter, Toasts.
 * ------------------------------------------------------------------------
 * NOTE: All product/cart data below is DEMO data for the static frontend.
 * When the Spring Boot REST API is wired up, replace the DEMO_PRODUCTS
 * array and the localStorage-backed cart/wishlist with fetch() calls to
 * /api/products, /api/cart, /api/wishlist (see comments marked API-HOOK).
//  *

'use strict';

/* ============================== DEMO DATA ============================== */
const DEMO_PRODUCTS = [
  { id: 1, name: 'Kanjivaram Silk Saree', cat: 'Sarees', price: 8999, old: 12999, img: 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?q=80&w=800', badge: 'New', rating: 5 },
  { id: 2, name: 'Banarasi Silk Saree', cat: 'Sarees', price: 7499, old: 9999, img: 'https://images.unsplash.com/photo-1610189844772-2a5eef0e5f18?q=80&w=800', badge: 'Sale', rating: 4 },
  { id: 3, name: 'Anarkali Chudidhar Set', cat: 'Chudidhar', price: 3499, old: 4999, img: 'https://images.unsplash.com/photo-1583391733956-6c78276477e2?q=80&w=800', badge: 'Trending', rating: 5 },
  { id: 4, name: 'Bridal Lehenga Choli', cat: 'Lehenga', price: 15999, old: 21999, img: 'https://images.unsplash.com/photo-1610652492500-ded49ceeb378?q=80&w=800', badge: 'Best Seller', rating: 5 },
  { id: 5, name: 'Designer Cotton Kurti', cat: 'Kurti', price: 1299, old: 1799, img: 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?q=80&w=800', badge: 'New', rating: 4 },
  { id: 6, name: 'Printed Kurta Set', cat: 'Kurta Set', price: 2199, old: 2899, img: 'https://images.unsplash.com/photo-1622470953794-aa9c70b0fb9d?q=80&w=800', badge: 'Sale', rating: 4 },
  { id: 7, name: 'Peacock Motif Jhumka', cat: 'Jewellery', price: 899, old: 1299, img: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?q=80&w=800', badge: 'Trending', rating: 5 },
  { id: 8, name: 'Embroidered Handbag', cat: 'Handbags', price: 1599, old: 2199, img: 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?q=80&w=800', badge: 'New', rating: 4 },
];

/* ============================== PRELOADER ============================== */
window.addEventListener('load', () => {
  const pre = document.getElementById('preloader');
  setTimeout(() => pre && pre.classList.add('loaded'), 500);
});

/* ========================== GOLD PARTICLE FIELD ========================= */
function spawnParticles(container, count = 26) {
  if (!container) return;
  for (let i = 0; i < count; i++) {
    const p = document.createElement('span');
    p.className = 'particle';
    const size = 2 + Math.random() * 4;
    p.style.width = p.style.height = `${size}px`;
    p.style.left = `${Math.random() * 100}%`;
    p.style.animationDuration = `${8 + Math.random() * 10}s`;
    p.style.animationDelay = `${Math.random() * 10}s`;
    container.appendChild(p);
  }
}
document.querySelectorAll('.particle-field').forEach(el => spawnParticles(el));

/* ============================ PEACOCK FEATHERS =========================== */
function buildFeatherCrown() {
  const crown = document.getElementById('featherCrown');
  if (!crown) return;
  const total = 9;
  const colors = ['#0B8C7D', '#045D56', '#1E3FA0', '#0B1F4E'];
  for (let i = 0; i < total; i++) {
    const angle = -48 + (96 / (total - 1)) * i; // spread -48deg..+48deg
    const f = document.createElement('div');
    f.className = 'feather';
    f.style.setProperty('--rot', `${angle}deg`);
    f.style.left = '50%';
    f.style.animationDelay = `${i * 0.15}s`;
    const c1 = colors[i % colors.length];
    f.innerHTML = `
      <svg viewBox="0 0 70 260" xmlns="http://www.w3.org/2000/svg">
        <path d="M35 260 C15 190 10 120 35 10 C60 120 55 190 35 260 Z" fill="${c1}" opacity="0.55"/>
        <circle cx="35" cy="42" r="20" fill="#D4AF37" opacity="0.85"/>
        <circle cx="35" cy="42" r="11" fill="#0B1F4E" opacity="0.9"/>
        <circle cx="35" cy="42" r="4" fill="#F4E4A6"/>
        <line x1="35" y1="260" x2="35" y2="20" stroke="#D4AF37" stroke-width="1.4" opacity="0.6"/>
      </svg>`;
    crown.appendChild(f);
  }
}
buildFeatherCrown();

/* ================================ NAVBAR ================================= */
const navbar = document.getElementById('mainNavbar');
window.addEventListener('scroll', () => {
  if (!navbar) return;
  navbar.classList.toggle('scrolled', window.scrollY > 40);
  updateBackToTopProgress();
});

/* mobile toggler feedback handled by Bootstrap collapse; nothing extra needed */

/* =============================== SEARCH =================================== */
const searchOverlay = document.getElementById('searchOverlay');
document.querySelectorAll('[data-open-search]').forEach(btn =>
  btn.addEventListener('click', () => {
    searchOverlay.classList.add('active');
    setTimeout(() => document.getElementById('searchInput')?.focus(), 300);
  })
);
document.querySelector('.search-close')?.addEventListener('click', () => searchOverlay.classList.remove('active'));
document.addEventListener('keydown', e => { if (e.key === 'Escape') searchOverlay?.classList.remove('active'); });

document.querySelectorAll('.search-suggestions span').forEach(tag =>
  tag.addEventListener('click', () => {
    document.getElementById('searchInput').value = tag.textContent.trim();
  })
);

/* Voice search (Web Speech API, graceful fallback) */
const voiceBtn = document.getElementById('voiceSearchBtn');
if (voiceBtn) {
  voiceBtn.addEventListener('click', () => {
    const SR = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SR) {
      toast('info', 'Voice search needs a supported browser (Chrome/Edge).');
      return;
    }
    const rec = new SR();
    rec.lang = 'en-IN';
    voiceBtn.classList.add('text-danger');
    rec.start();
    rec.onresult = (e) => {
      document.getElementById('searchInput').value = e.results[0][0].transcript;
      voiceBtn.classList.remove('text-danger');
    };
    rec.onerror = () => voiceBtn.classList.remove('text-danger');
  });
}

/* ============================ CART / WISHLIST ============================= */
/* API-HOOK: replace localStorage with authenticated REST calls once backend is live */
const Store = {
  get(key) { return JSON.parse(localStorage.getItem(key) || '[]'); },
  set(key, val) { localStorage.setItem(key, JSON.stringify(val)); },
};

function addToCart(id) {
  const product = DEMO_PRODUCTS.find(p => p.id === id);
  if (!product) return;
  const cart = Store.get('skc_cart');
  const existing = cart.find(i => i.id === id);
  if (existing) existing.qty += 1;
  else cart.push({ ...product, qty: 1 });
  Store.set('skc_cart', cart);
  renderCounts();
  renderCartDrawer();
  toast('success', `${product.name} added to cart`);
}

function toggleWishlist(id) {
  const product = DEMO_PRODUCTS.find(p => p.id === id);
  let wishlist = Store.get('skc_wishlist');
  if (wishlist.find(i => i.id === id)) {
    wishlist = wishlist.filter(i => i.id !== id);
    toast('info', `${product.name} removed from wishlist`);
  } else {
    wishlist.push(product);
    toast('success', `${product.name} added to wishlist`);
  }
  Store.set('skc_wishlist', wishlist);
  renderCounts();
  renderWishlistDrawer();
  document.querySelectorAll(`.btn-wishlist[data-id="${id}"]`).forEach(b => b.classList.toggle('active'));
}

function renderCounts() {
  const cartCount = Store.get('skc_cart').reduce((s, i) => s + i.qty, 0);
  const wishCount = Store.get('skc_wishlist').length;
  document.querySelectorAll('.cart-count').forEach(el => el.textContent = cartCount);
  document.querySelectorAll('.wishlist-count').forEach(el => el.textContent = wishCount);
}

function renderCartDrawer() {
  const wrap = document.getElementById('cartItems');
  if (!wrap) return;
  const cart = Store.get('skc_cart');
  wrap.innerHTML = cart.length ? cart.map(i => `
    <div class="mini-item">
      <img src="${i.img}" alt="${i.name}">
      <div class="flex-grow-1">
        <h6 class="mb-1" style="font-size:.88rem">${i.name}</h6>
        <div class="d-flex justify-content-between align-items-center">
          <span class="price-now">₹${(i.price * i.qty).toLocaleString('en-IN')}</span>
          <span class="small text-muted">Qty: ${i.qty}</span>
        </div>
      </div>
      <button class="btn btn-sm text-danger" onclick="removeFromCart(${i.id})"><i class="fa-solid fa-xmark"></i></button>
    </div>`).join('') : `<p class="text-center text-muted mt-4">Your cart is empty.</p>`;
  const total = cart.reduce((s, i) => s + i.price * i.qty, 0);
  const totalEl = document.getElementById('cartTotal');
  if (totalEl) totalEl.textContent = `₹${total.toLocaleString('en-IN')}`;
}

function removeFromCart(id) {
  Store.set('skc_cart', Store.get('skc_cart').filter(i => i.id !== id));
  renderCounts(); renderCartDrawer();
}

function renderWishlistDrawer() {
  const wrap = document.getElementById('wishlistItems');
  if (!wrap) return;
  const list = Store.get('skc_wishlist');
  wrap.innerHTML = list.length ? list.map(i => `
    <div class="mini-item">
      <img src="${i.img}" alt="${i.name}">
      <div class="flex-grow-1">
        <h6 class="mb-1" style="font-size:.88rem">${i.name}</h6>
        <span class="price-now">₹${i.price.toLocaleString('en-IN')}</span>
      </div>
      <button class="btn btn-sm" onclick="addToCart(${i.id})" title="Move to cart"><i class="fa-solid fa-cart-plus"></i></button>
    </div>`).join('') : `<p class="text-center text-muted mt-4">No items saved yet.</p>`;
}

window.addToCart = addToCart;
window.toggleWishlist = toggleWishlist;
window.removeFromCart = removeFromCart;

/* ============================ PRODUCT RENDERING ============================ */
function starIcons(rating) {
  return Array.from({ length: 5 }, (_, i) => `<i class="fa-${i < rating ? 'solid' : 'regular'} fa-star"></i>`).join('');
}

function productCardHTML(p) {
  const wished = Store.get('skc_wishlist').some(i => i.id === p.id);
  return `
  <div class="col-6 col-md-4 col-lg-3 reveal">
    <div class="product-card">
      <div class="product-media">
        ${p.badge ? `<span class="product-badge ${p.badge === 'Sale' ? 'sale' : ''}">${p.badge}</span>` : ''}
        <div class="product-actions">
          <button class="btn-wishlist ${wished ? 'active' : ''}" data-id="${p.id}" onclick="toggleWishlist(${p.id})" title="Wishlist"><i class="fa-solid fa-heart"></i></button>
          <button data-bs-toggle="modal" data-bs-target="#quickViewModal" onclick="openQuickView(${p.id})" title="Quick View"><i class="fa-solid fa-eye"></i></button>
          <button onclick="addCompare(${p.id})" title="Compare"><i class="fa-solid fa-arrows-left-right"></i></button>
        </div>
        <img src="${p.img}" alt="${p.name}" loading="lazy">
        <div class="quick-add" onclick="addToCart(${p.id})"><i class="fa-solid fa-bag-shopping me-1"></i> Add to Cart</div>
      </div>
      <div class="product-info">
        <span class="cat">${p.cat}</span>
        <h6>${p.name}</h6>
        <div class="stars mb-1">${starIcons(p.rating)}</div>
        <div class="price-row">
          <span class="price-now">₹${p.price.toLocaleString('en-IN')}</span>
          ${p.old ? `<span class="price-old">₹${p.old.toLocaleString('en-IN')}</span>` : ''}
        </div>
      </div>
    </div>
  </div>`;
}

function renderProductGrid(targetId, products) {
  const el = document.getElementById(targetId);
  if (!el) return;
  el.innerHTML = products.map(productCardHTML).join('');
  revealOnScroll();
}

/* Skeleton -> real content simulate network latency for realism */
window.addEventListener('DOMContentLoaded', () => {
  setTimeout(() => {
    renderProductGrid('newArrivalsGrid', DEMO_PRODUCTS.slice(0, 4));
    renderProductGrid('trendingGrid', [...DEMO_PRODUCTS].reverse().slice(0, 4));
    renderProductGrid('bestSellerGrid', DEMO_PRODUCTS.slice(2, 6));
    renderProductGrid('featuredGrid', DEMO_PRODUCTS);
    renderCounts();
    renderCartDrawer();
    renderWishlistDrawer();
  }, 900);
});

/* ============================== QUICK VIEW ============================== */
function openQuickView(id) {
  const p = DEMO_PRODUCTS.find(i => i.id === id);
  if (!p) return;
  document.getElementById('qvImage').src = p.img;
  document.getElementById('qvName').textContent = p.name;
  document.getElementById('qvCat').textContent = p.cat;
  document.getElementById('qvPrice').textContent = `₹${p.price.toLocaleString('en-IN')}`;
  document.getElementById('qvOld').textContent = p.old ? `₹${p.old.toLocaleString('en-IN')}` : '';
  document.getElementById('qvStars').innerHTML = starIcons(p.rating);
  document.getElementById('qvAddBtn').setAttribute('onclick', `addToCart(${p.id})`);
}
window.openQuickView = openQuickView;

function addCompare(id) {
  const list = Store.get('skc_compare');
  if (list.find(i => i === id)) { toast('info', 'Already added to compare'); return; }
  if (list.length >= 4) { toast('warning', 'You can compare up to 4 products'); return; }
  list.push(id);
  Store.set('skc_compare', list);
  toast('success', 'Added to comparison');
}
window.addCompare = addCompare;

/* ============================== COUNTDOWN ================================= */
function startCountdown(targetSelector, hours = 26) {
  const el = document.querySelector(targetSelector);
  if (!el) return;
  const end = Date.now() + hours * 3600 * 1000;
  const box = { h: el.querySelector('.cd-h'), m: el.querySelector('.cd-m'), s: el.querySelector('.cd-s') };
  setInterval(() => {
    const diff = Math.max(0, end - Date.now());
    const h = Math.floor(diff / 3600000);
    const m = Math.floor((diff % 3600000) / 60000);
    const s = Math.floor((diff % 60000) / 1000);
    if (box.h) box.h.textContent = String(h).padStart(2, '0');
    if (box.m) box.m.textContent = String(m).padStart(2, '0');
    if (box.s) box.s.textContent = String(s).padStart(2, '0');
  }, 1000);
}
startCountdown('#dealCountdown');

/* ============================ REVEAL ON SCROLL ============================ */
function revealOnScroll() {
  const els = document.querySelectorAll('.reveal:not(.in)');
  const io = new IntersectionObserver((entries) => {
    entries.forEach(e => { if (e.isIntersecting) { e.target.classList.add('in'); io.unobserve(e.target); } });
  }, { threshold: 0.15 });
  els.forEach(el => io.observe(el));
}
revealOnScroll();

/* ================================ THEME =================================== */
const themeToggle = document.getElementById('themeToggle');
function applyTheme(mode) {
  document.documentElement.setAttribute('data-theme', mode);
  localStorage.setItem('skc_theme', mode);
  if (themeToggle) themeToggle.innerHTML = mode === 'dark' ? '<i class="fa-solid fa-sun"></i>' : '<i class="fa-solid fa-moon"></i>';
}
applyTheme(localStorage.getItem('skc_theme') || 'light');
themeToggle?.addEventListener('click', () => {
  applyTheme(document.documentElement.getAttribute('data-theme') === 'dark' ? 'light' : 'dark');
});

/* ============================== BACK TO TOP ================================ */
const backTop = document.getElementById('backToTop');
const backTopBar = document.getElementById('backToTopBar');
function updateBackToTopProgress() {
  if (!backTop) return;
  const scrolled = window.scrollY;
  const height = document.documentElement.scrollHeight - window.innerHeight;
  const pct = height > 0 ? scrolled / height : 0;
  backTop.classList.toggle('show', scrolled > 400);
  if (backTopBar) {
    const circumference = 2 * Math.PI * 20;
    backTopBar.style.strokeDasharray = circumference;
    backTopBar.style.strokeDashoffset = circumference * (1 - pct);
  }
}
backTop?.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));

/* ================================ RIPPLE =================================== */
document.addEventListener('click', (e) => {
  const btn = e.target.closest('.ripple');
  if (!btn) return;
  const circle = document.createElement('span');
  const rect = btn.getBoundingClientRect();
  const size = Math.max(rect.width, rect.height);
  circle.className = 'ripple-circle';
  circle.style.width = circle.style.height = `${size}px`;
  circle.style.left = `${e.clientX - rect.left - size / 2}px`;
  circle.style.top = `${e.clientY - rect.top - size / 2}px`;
  btn.appendChild(circle);
  setTimeout(() => circle.remove(), 650);
});

/* ============================== NEWSLETTER ================================= */
document.getElementById('newsletterForm')?.addEventListener('submit', (e) => {
  e.preventDefault();
  const input = e.target.querySelector('input');
  if (!input.value || !input.value.includes('@')) {
    toast('error', 'Please enter a valid email address');
    return;
  }
  toast('success', 'Welcome to the family! Check your inbox for 10% off.');
  input.value = '';
});

/* ============================ CONTACT / CHAT STUB =========================== */
document.getElementById('chatFab')?.addEventListener('click', () => {
  toast('info', 'Live chat widget goes here — connect Tawk.to / Freshchat / custom WebSocket.');
});

/* ================================= TOASTS =================================== */
function toast(icon, title) {
  if (window.Swal) {
    Swal.fire({
      toast: true, position: 'top-end', icon, title,
      showConfirmButton: false, timer: 2600, timerProgressBar: true,
      background: 'var(--surface)', color: 'var(--ink)',
    });
  } else {
    console.log(`[${icon}] ${title}`);
  }
}
window.toast = toast;

/* ============================ CHECKOUT / BUY NOW STUB ======================= */
function buyNow(id) {
  addToCart(id);
  toast('info', 'Redirecting to checkout…');
  /* API-HOOK: window.location.href = '/checkout.html'; */
}
window.buyNow = buyNow;
