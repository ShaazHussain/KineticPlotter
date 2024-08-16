function showPlot(plotId) {
    // Hide all plots initially
    const plots = document.querySelectorAll('.plot-container');
    plots.forEach(plot => {
        plot.style.display = 'none';
    });

    // Show the selected plot
    document.getElementById(plotId).style.display = 'block';
}

// Optionally, show one plot by default
document.addEventListener('DOMContentLoaded', () => {
    showPlot('concentrationPlot'); // Show the concentration plot by default
});