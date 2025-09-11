// Admin Dashboard JavaScript
let dashboardChart = null;

document.addEventListener('DOMContentLoaded', function() {
    loadDashboardData();
    setupDashboardEventListeners();
    initializeDashboard();
});

// Initialize dashboard components
function initializeDashboard() {
    // Set up auto-refresh for dashboard data
    setInterval(loadDashboardData, 60000); // Refresh every minute
    
    // Initialize any charts or interactive elements
    setupDashboardCharts();
}

// Load dashboard statistics and data
async function loadDashboardData() {
    try {
        AdminUtils.showLoading('dashboard-stats');
        
        // Load dashboard statistics
        const stats = await AdminUtils.makeApiCall('/admin/api/dashboard/stats');
        if (stats) {
            updateDashboardStats(stats);
        }
        
        // Load recent activities
        const activities = await AdminUtils.makeApiCall('/admin/api/dashboard/recent-activities');
        if (activities) {
            updateRecentActivities(activities);
        }
        
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to load dashboard data');
    } finally {
        AdminUtils.hideLoading('dashboard-stats');
    }
}

// Update dashboard statistics cards
function updateDashboardStats(stats) {
    // Update customer statistics
    updateStatCard('total-customers', stats.totalCustomers);
    updateStatCard('active-customers', stats.activeCustomers);
    updateStatCard('new-customers-today', stats.newCustomersToday);
    
    // Update loan statistics
    updateStatCard('pending-loans', stats.pendingLoanApplications);
    updateStatCard('approved-loans', stats.approvedLoans);
    updateStatCard('total-loan-amount', AdminUtils.formatCurrency(stats.totalLoanAmount || 0));
    updateStatCard('new-loans-today', stats.newLoansToday);
    
    // Update chat statistics
    updateStatCard('unread-messages', stats.unreadMessages);
    
    // Update notification badge
    updateNotificationBadge(stats.unreadMessages);
}

// Update individual stat card
function updateStatCard(cardId, value) {
    const element = document.getElementById(cardId);
    if (element) {
        element.textContent = value;
        
        // Add animation effect
        element.classList.add('updated');
        setTimeout(() => element.classList.remove('updated'), 1000);
    }
}

// Update notification badge
function updateNotificationBadge(count) {
    const badge = document.querySelector('.notification-badge');
    if (badge) {
        if (count > 0) {
            badge.textContent = count > 99 ? '99+' : count;
            badge.style.display = 'block';
        } else {
            badge.style.display = 'none';
        }
    }
}

// Update recent activities section
function updateRecentActivities(activities) {
    updateRecentCustomers(activities.recentCustomers);
    updateRecentLoans(activities.recentLoans);
    updateRecentMessages(activities.recentMessages);
}

// Update recent customers list
function updateRecentCustomers(customers) {
    const container = document.getElementById('recent-customers');
    if (!container || !customers) return;
    
    container.innerHTML = customers.map(customer => `
        <div class="activity-item">
            <div class="activity-icon">
                <i class="fas fa-user-plus"></i>
            </div>
            <div class="activity-content">
                <h4>${customer.firstName} ${customer.lastName}</h4>
                <p>New customer registered</p>
                <small class="text-muted">${AdminUtils.formatDateTime(customer.createdDate)}</small>
            </div>
            <div class="activity-actions">
                <a href="/admin/customers/${customer.customerId}" class="btn btn-sm btn-primary">View</a>
            </div>
        </div>
    `).join('');
}

// Update recent loan applications
function updateRecentLoans(loans) {
    const container = document.getElementById('recent-loans');
    if (!container || !loans) return;
    
    container.innerHTML = loans.map(loan => `
        <div class="activity-item">
            <div class="activity-icon">
                <i class="fas fa-file-invoice-dollar"></i>
            </div>
            <div class="activity-content">
                <h4>${loan.customerName}</h4>
                <p>${loan.loanType} - ${AdminUtils.formatCurrency(loan.loanAmount)}</p>
                <small class="text-muted">${AdminUtils.formatDateTime(loan.createdDate)}</small>
            </div>
            <div class="activity-actions">
                <span class="badge badge-${getStatusBadgeClass(loan.status)}">${loan.status}</span>
                <a href="/admin/loans/${loan.loanId}" class="btn btn-sm btn-primary">Review</a>
            </div>
        </div>
    `).join('');
}

// Update recent chat messages
function updateRecentMessages(messages) {
    const container = document.getElementById('recent-messages');
    if (!container || !messages) return;
    
    container.innerHTML = messages.map(message => `
        <div class="activity-item">
            <div class="activity-icon">
                <i class="fas fa-comment"></i>
            </div>
            <div class="activity-content">
                <h4>${message.customerName}</h4>
                <p>${message.message.substring(0, 50)}${message.message.length > 50 ? '...' : ''}</p>
                <small class="text-muted">${AdminUtils.formatDateTime(message.timestamp)}</small>
            </div>
            <div class="activity-actions">
                ${!message.isRead ? '<span class="badge badge-danger">New</span>' : ''}
                <a href="/admin/chat?customer=${message.customerId}" class="btn btn-sm btn-primary">Reply</a>
            </div>
        </div>
    `).join('');
}

// Get CSS class for status badge
function getStatusBadgeClass(status) {
    const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'UNDER_REVIEW': 'info'
    };
    return statusMap[status] || 'secondary';
}

// Set up dashboard event listeners
function setupDashboardEventListeners() {
    // Refresh button
    const refreshBtn = document.getElementById('refresh-dashboard');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', function() {
            loadDashboardData();
            AdminUtils.showAlert('info', 'Dashboard refreshed');
        });
    }
    
    // Quick action buttons
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('quick-action-btn')) {
            handleQuickAction(e.target);
        }
    });
}

// Handle quick actions from dashboard
function handleQuickAction(button) {
    const action = button.dataset.action;
    
    switch (action) {
        case 'view-pending-loans':
            window.location.href = '/admin/loans?status=pending';
            break;
        case 'view-new-customers':
            window.location.href = '/admin/customers?filter=recent';
            break;
        case 'view-unread-messages':
            window.location.href = '/admin/chat?filter=unread';
            break;
    }
}

// Setup dashboard charts (if needed)
function setupDashboardCharts() {
    // Placeholder for future chart implementations
    // Could add pie charts for loan statuses, line charts for customer growth, etc.
    console.log('Dashboard charts initialized');
}

// Refresh dashboard data function (called by main.js)
async function refreshDashboardData() {
    await loadDashboardData();
}
