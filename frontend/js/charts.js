function drawActivityChart(canvasId, data) {
    const ctx = document.getElementById(canvasId)?.getContext('2d');
    if (!ctx) return;
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Commits', 'Pull Requests', 'Issues', 'Reviews'],
            datasets: [{
                data: [data.commits, data.prs, data.issues, data.reviews],
                backgroundColor: ['#3fb950', '#58a6ff', '#f85149', '#d29922'],
                borderColor: '#161b22',
                borderWidth: 3,
                hoverOffset: 8
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom', labels: { color: '#c9d1d9', padding: 16, font: { size: 12 } } },
                tooltip: {
                    callbacks: {
                        label: (ctx) => ` ${ctx.label}: ${ctx.raw} (${
                            ((ctx.raw / ctx.dataset.data.reduce((a, b) => a + b, 0)) * 100).toFixed(1)}%)`
                    }
                }
            }
        }
    });
}

function drawContributionLineChart(canvasId, labels, dataPoints) {
    const ctx = document.getElementById(canvasId)?.getContext('2d');
    if (!ctx) return;
    new Chart(ctx, {
        type: 'line',
        data: {
            labels,
            datasets: [{
                label: 'Score', data: dataPoints, borderColor: '#58a6ff',
                backgroundColor: 'rgba(88, 166, 255, 0.1)', tension: 0.4,
                fill: true, pointBackgroundColor: '#58a6ff', pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: { ticks: { color: '#8b949e' }, grid: { color: '#21262d' } },
                y: { ticks: { color: '#8b949e' }, grid: { color: '#21262d' }, beginAtZero: true }
            },
            plugins: { legend: { labels: { color: '#c9d1d9' } } }
        }
    });
}