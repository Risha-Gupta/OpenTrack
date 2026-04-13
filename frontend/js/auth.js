const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');

if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const errMsg = document.getElementById('error-msg');
        errMsg.textContent = '';
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        try {
            await api.login(username, password);
            window.location.href = `profile.html?user=${username}`;
        } catch (err) {
            errMsg.textContent = err.message || 'Login failed. Check your credentials.';
        }
    });
}

if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const errMsg = document.getElementById('error-msg');
        errMsg.textContent = '';
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const githubUsername = document.getElementById('github-username')?.value.trim();
        try {
            await api.register(username, email, password);
            await api.login(username, password);
            if (githubUsername) await api.linkGithub(githubUsername);
            window.location.href = `profile.html?user=${githubUsername || username}`;
        } catch (err) {
            errMsg.textContent = err.message || 'Registration failed. Try a different username or email.';
        }
    });
}

window.addEventListener('DOMContentLoaded', () => {
    const authLink = document.getElementById('auth-link');
    if (authLink && api.isLoggedIn()) {
        authLink.textContent = 'Logout';
        authLink.href = '#';
        authLink.addEventListener('click', () => { api.logout(); window.location.href = 'index.html'; });
    }
});