let currentPage = 0;
const PAGE_SIZE = 20;

async function loadLeaderboard(page = 0) {
    try {
        const data = await api.getLeaderboard(page, PAGE_SIZE);
        renderPodium(data.content.slice(0, 3));
        renderTable(data.content, page);
        renderPagination(data.totalPages, page);
        currentPage = page;
    } catch (err) {
        document.getElementById('leaderboard-body').innerHTML =
            `<tr><td colspan="7" style="text-align:center;color:#f85149">Failed to load: ${err.message}</td></tr>`;
    }
}

function renderPodium(top3) {
    const medals = ['🥇', '🥈', '🥉'];
    const classes = ['first', 'second', 'third'];
    document.getElementById('podium').innerHTML = top3.map((c, i) => `
        <div class="podium-item ${classes[i]}">
            <span class="podium-rank">${medals[i]}</span>
            <img src="${c.avatarUrl || `https://github.com/${c.githubUsername}.png?size=50`}"
                 alt="${c.githubUsername}" class="podium-avatar"
                 onerror="this.src='https://github.com/identicons/${c.githubUsername}.png'">
            <a href="profile.html?user=${c.githubUsername}" class="podium-name">${c.githubUsername}</a>
            <span class="podium-score">${c.totalScore} pts</span>
        </div>`).join('');
}

function renderTable(contributors, page) {
    document.getElementById('leaderboard-body').innerHTML = contributors.map((c, i) => {
        const rank = page * PAGE_SIZE + i + 1;
        return `<tr>
            <td><span class="rank-badge ${rank <= 3 ? 'top3' : ''}">#${rank}</span></td>
            <td><a href="profile.html?user=${c.githubUsername}" class="contributor-link">
                <img src="${c.avatarUrl || `https://github.com/${c.githubUsername}.png?size=24`}" alt="${c.githubUsername}">
                ${c.githubUsername}</a></td>
            <td><strong>${c.totalScore}</strong></td>
            <td>${c.commitCount}</td><td>${c.prCount}</td>
            <td>${c.issueCount}</td><td>${c.reviewCount}</td>
        </tr>`;
    }).join('');
}

function renderPagination(totalPages, current) {
    document.getElementById('pagination').innerHTML = Array.from({ length: totalPages }, (_, i) =>
        `<button class="page-btn ${i === current ? 'active' : ''}" onclick="loadLeaderboard(${i})">${i + 1}</button>`
    ).join('');
}

document.getElementById('search-input').addEventListener('input', function () {
    const query = this.value.toLowerCase();
    document.querySelectorAll('#leaderboard-body tr').forEach(row => {
        const name = row.querySelector('.contributor-link')?.textContent.toLowerCase() || '';
        row.style.display = name.includes(query) ? '' : 'none';
    });
});

loadLeaderboard();