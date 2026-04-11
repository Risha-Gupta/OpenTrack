const API_BASE = 'http://localhost:8080/api/v1';

const api = {
    _token: () => localStorage.getItem('opentrack_token'),

    _headers(auth = false) {
        const h = { 'Content-Type': 'application/json' };
        if (auth) h['Authorization'] = `Bearer ${this._token()}`;
        return h;
    },

    async _fetch(path, options = {}) {
        const res = await fetch(`${API_BASE}${path}`, options);
        if (!res.ok) {
            const err = await res.json().catch(() => ({ message: res.statusText }));
            throw new Error(err.message || 'Request failed');
        }
        return res.json();
    },

    async login(username, password) {
        const data = await this._fetch('/auth/login', {
            method: 'POST', headers: this._headers(),
            body: JSON.stringify({ username, password })
        });
        if (data.token) localStorage.setItem('opentrack_token', data.token);
        return data;
    },

    async register(username, email, password) {
        return this._fetch('/auth/register', {
            method: 'POST', headers: this._headers(),
            body: JSON.stringify({ username, email, password })
        });
    },

    logout() { localStorage.removeItem('opentrack_token'); },
    isLoggedIn() { return !!this._token(); },

    async getLeaderboard(page = 0, size = 20) {
        return this._fetch(`/contributors?page=${page}&size=${size}`);
    },

    async getContributor(username) { return this._fetch(`/contributors/${username}`); },

    async getContributorStats(username) { return this._fetch(`/contributors/${username}/stats`); },

    async syncContributor(username) {
        return this._fetch(`/sync/contributor/${username}`, {
            method: 'POST', headers: this._headers(true)
        });
    },

    async getMonthlyReport(year, month) {
        return this._fetch(`/reports/monthly?year=${year}&month=${month}`,
            { headers: this._headers(true) });
    },

    async getWeeklyReport(username) {
        return this._fetch(`/reports/weekly/${username}`, { headers: this._headers(true) });
    },

    async getMe() { return this._fetch('/users/me', { headers: this._headers(true) }); },

    async linkGithub(githubUsername) {
        return this._fetch('/users/me/github', {
            method: 'PUT', headers: this._headers(true),
            body: JSON.stringify({ githubUsername })
        });
    }
};