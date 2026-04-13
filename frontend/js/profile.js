const urlParams = new URLSearchParams(window.location.search);
const username = urlParams.get('user');

async function loadProfile() {
    if (!username) {
        if (api.isLoggedIn()) {
            const me = await api.getMe();
            window.location.href = `profile.html?user=${me.githubUsername || me.username}`;
        } else {
            window.location.href = 'login.html';
        }
        return;
    }
    try {
        const [contributor, stats] = await Promise.all([
            api.getContributor(username),
            api.getContributorStats(username)
        ]);
        renderProfileHeader(contributor, stats);
        renderStats(stats);
        renderActivityChart(stats);
    } catch (err) {
        document.getElementById('profile-header').innerHTML =
            `<div style="color:#f85149;padding:2rem">User not found or not yet synced: ${username}</div>`;
    }
}

function renderProfileHeader(contributor, stats) {
    document.getElementById('profile-header').innerHTML = `
        <img src="${contributor.avatarUrl || `https://github.com/${username}.png?size=80`}"
             alt="${username}" class="profile-avatar"
             onerror="this.src='https://github.com/identicons/${username}.png'">
        <div class="profile-info">
            <h1>${contributor.displayName || username}</h1>
            <p>@${contributor.githubUsername}</p>
            <a href="https://github.com/${username}" target="_blank" style="font-size:0.8rem;color:var(--accent)">
                View on GitHub ↗</a>
        </div>
        <div class="profile-score">
            <span class="score-value">${stats.totalScore}</span>
            <span class="score-label">Total Score</span>
        </div>`;
}

function renderStats(stats) {
    const items = [
        { name: 'Commits', val: stats.commitCount, icon: '📝' },
        { name: 'Pull Requests', val: stats.prCount, icon: '🔀' },
        { name: 'Issues', val: stats.issueCount, icon: '🐛' },
        { name: 'Code Reviews', val: stats.reviewCount, icon: '👁️' },
        { name: 'Last Synced', val: stats.lastSynced ? new Date(stats.lastSynced).toLocaleDateString() : 'Never', icon: '🔄' }
    ];
    document.getElementById('stats-list').innerHTML = items.map(item =>
        `<div class="stat-item">
            <span class="stat-name">${item.icon} ${item.name}</span>
            <span class="stat-val">${item.val}</span>
        </div>`).join('');
}

function renderActivityChart(stats) {
    drawActivityChart('activityChart', {
        commits: stats.commitCount, prs: stats.prCount,
        issues: stats.issueCount, reviews: stats.reviewCount
    });
}

loadProfile();