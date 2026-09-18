/**
 * MythBreak — Shared Auth Utility
 * Handles JWT storage, session management, and role-based redirects.
 */
const Auth = (() => {
    const TOKEN_KEY = 'mb_token';
    const USER_KEY  = 'mb_user';

    function saveSession(data) {
        // Normalize role to uppercase string to guard against serialization variations
        const role = data.role ? String(data.role).toUpperCase() : '';
        localStorage.setItem(TOKEN_KEY, data.token);
        localStorage.setItem(USER_KEY, JSON.stringify({
            userId:    data.userId,
            email:     data.email,
            firstName: data.firstName,
            lastName:  data.lastName,
            role:      role
        }));
    }

    function getToken() { return localStorage.getItem(TOKEN_KEY); }

    function getUser() {
        const u = localStorage.getItem(USER_KEY);
        return u ? JSON.parse(u) : null;
    }

    function getRole() {
        const u = getUser();
        return u ? u.role : null;
    }

    function isLoggedIn() { return !!getToken(); }

    function logout() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        window.location.href = '/login.html';
    }

    function requireAuth() {
        if (!isLoggedIn()) { window.location.href = '/login.html'; }
    }

    function requireRole(role) {
        requireAuth();
        const r = getRole();
        if (!Array.isArray(role)) role = [role];
        if (!role.map(x => x.toUpperCase()).includes(r)) {
            window.location.href = '/login.html';
        }
    }

    function dashboardFor(role) {
        const normalised = role ? String(role).toUpperCase() : '';
        const map = {
            LEARNER:  '/dashboard-learner.html',
            EARNER:   '/dashboard-earner.html',
            EDUCATOR: '/dashboard-educator.html',
            COMPANY:  '/dashboard-company.html',
            ADMIN:    '/dashboard-learner.html'
        };
        return map[normalised] || '/login.html';
    }

    /** Authenticated API call helper */
    async function api(path, options = {}) {
        const token = getToken();
        const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
        if (token) headers['Authorization'] = 'Bearer ' + token;

        const res = await fetch(path, { ...options, headers });

        // If 401 (token expired / missing), force logout
        if (res.status === 401) { logout(); return; }

        const text = await res.text();
        let json;
        try { json = JSON.parse(text); } catch { json = { message: text }; }

        if (!res.ok) {
            // 403 means role mismatch — don't logout, just surface the error
            throw new Error(json.message || json.error || `Request failed (${res.status})`);
        }
        return json;
    }

    /** Render the navbar user info & logout button */
    function renderNavUser(containerId) {
        const u = getUser();
        if (!u) return;
        const el = document.getElementById(containerId);
        if (!el) return;
        el.innerHTML = `
            <span style="color:var(--text-secondary);font-size:0.85rem;">
                👋 ${u.firstName}
            </span>
            <button onclick="Auth.logout()" class="btn btn-secondary btn-sm">Logout</button>
        `;
    }

    return { saveSession, getToken, getUser, getRole, isLoggedIn, logout, requireAuth, requireRole, dashboardFor, api, renderNavUser };
})();
