// Admin Service - Main JavaScript File
document.addEventListener('DOMContentLoaded', function() {
    
    // Initialize admin interface
    initializeAdminInterface();
    
    // Set up global event listeners
    setupGlobalEventListeners();
    
    // Check authentication status
    checkAuthenticationStatus();
});

// Initialize admin interface
function initializeAdminInterface() {
    // Create alert container for notifications
    createAlertContainer();
    
    // Initialize navigation highlighting
    highlightCurrentPage();
    
    // Set up logout functionality
    setupLogoutHandler();
}

// Create alert container for showing notifications
function createAlertContainer() {
    if (!document.getElementById('alert-container')) {
        const alertContainer = document.createElement('div');
        alertContainer.id = 'alert-container';
        alertContainer.className = 'alert-container';
        document.body.insertBefore(alertContainer, document.body.firstChild);
    }
}

// Highlight current page in navigation
function highlightCurrentPage() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.admin-nav a');
    
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

// Set up global event listeners
function setupGlobalEventListeners() {
    // Handle form submissions
    document.addEventListener('submit', handleFormSubmission);
    
    // Handle button clicks
    document.addEventListener('click', handleButtonClick);
    
    // Handle input changes for real-time validation
    document.addEventListener('input', handleInputValidation);
}

// Handle form submissions globally
function handleFormSubmission(event) {
    const form = event.target;
    
    if (form.classList.contains('ajax-form')) {
        event.preventDefault();
        submitAjaxForm(form);
    }
}

// Submit form via AJAX
async function submitAjaxForm(form) {
    if (typeof AdminUtils === 'undefined') {
        console.warn('AdminUtils not available, falling back to standard form submission');
        return;
    }
    
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    const action = form.getAttribute('action');
    const method = form.getAttribute('method') || 'POST';
    
    try {
        AdminUtils.showLoading('submit-btn');
        
        const response = await AdminUtils.makeApiCall(action, method, data);
        
        if (response && response.success) {
            AdminUtils.showAlert('success', response.message || 'Operation completed successfully');
            
            // Redirect if specified
            if (response.redirectUrl) {
                setTimeout(() => {
                    window.location.href = response.redirectUrl;
                }, 1000);
            }
        } else {
            AdminUtils.showAlert('danger', response?.message || 'Operation failed');
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'An error occurred while processing your request');
    } finally {
        AdminUtils.hideLoading('submit-btn', 'Submit');
    }
}

// Handle button clicks globally
function handleButtonClick(event) {
    const button = event.target;
    
    // Handle approve/reject buttons
    if (button.classList.contains('approve-btn')) {
        handleLoanApproval(button);
    } else if (button.classList.contains('reject-btn')) {
        handleLoanRejection(button);
    } else if (button.classList.contains('toggle-status-btn')) {
        handleStatusToggle(button);
    }
}

// Handle loan approval
async function handleLoanApproval(button) {
    if (typeof AdminUtils === 'undefined') {
        console.warn('AdminUtils not available for loan approval');
        return;
    }
    
    const loanId = button.dataset.loanId;
    const remarks = prompt('Enter approval remarks (optional):') || 'Approved by admin';
    
    if (AdminUtils.confirmAction('approve this loan application')) {
        const response = await AdminUtils.makeApiCall(
            `/admin/loans/api/approve/${loanId}`,
            'POST',
            { remarks: remarks }
        );
        
        if (response && response.success) {
            AdminUtils.showAlert('success', 'Loan approved successfully');
            location.reload(); // Refresh to show updated status
        }
    }
}

// Handle loan rejection
async function handleLoanRejection(button) {
    if (typeof AdminUtils === 'undefined') {
        console.warn('AdminUtils not available for loan rejection');
        return;
    }
    
    const loanId = button.dataset.loanId;
    const remarks = prompt('Enter rejection reason:');
    
    if (remarks && AdminUtils.confirmAction('reject this loan application')) {
        const response = await AdminUtils.makeApiCall(
            `/admin/loans/api/reject/${loanId}`,
            'POST',
            { remarks: remarks }
        );
        
        if (response && response.success) {
            AdminUtils.showAlert('success', 'Loan rejected');
            location.reload(); // Refresh to show updated status
        }
    }
}

// Handle status toggle (activate/deactivate customers)
async function handleStatusToggle(button) {
    if (typeof AdminUtils === 'undefined') {
        console.warn('AdminUtils not available for status toggle');
        return;
    }
    
    const customerId = button.dataset.customerId;
    const currentStatus = button.dataset.status;
    const action = currentStatus === 'true' ? 'deactivate' : 'activate';
    
    if (AdminUtils.confirmAction(`${action} this customer account`)) {
        const response = await AdminUtils.makeApiCall(
            `/admin/customers/api/${customerId}/toggle-status`,
            'POST'
        );
        
        if (response && response.success) {
            AdminUtils.showAlert('success', response.message);
            location.reload(); // Refresh to show updated status
        }
    }
}

// Handle input validation
function handleInputValidation(event) {
    const input = event.target;
    
    if (input.type === 'email') {
        validateEmailInput(input);
    } else if (input.classList.contains('required')) {
        validateRequiredInput(input);
    }
}

// Validate email input
function validateEmailInput(input) {
    if (typeof AdminUtils !== 'undefined') {
        const isValid = AdminUtils.isValidEmail(input.value);
        input.classList.toggle('invalid', !isValid && input.value.length > 0);
    }
}

// Validate required input
function validateRequiredInput(input) {
    const isValid = input.value.trim().length > 0;
    input.classList.toggle('invalid', !isValid);
}

// Set up logout handler
function setupLogoutHandler() {
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', async function(e) {
            e.preventDefault();
            
            if (typeof AdminUtils !== 'undefined') {
                if (AdminUtils.confirmAction('logout')) {
                    await AdminUtils.makeApiCall('/api/admin/logout', 'POST');
                    AdminUtils.redirectToLogin();
                }
            } else {
                // Fallback to standard logout
                if (confirm('Are you sure you want to logout?')) {
                    window.location.href = '/logout';
                }
            }
        });
    }
}

// Check authentication status
function checkAuthenticationStatus() {
    const currentPath = window.location.pathname;
    const isLoginPage = currentPath.includes('/login') || currentPath.includes('/register');
    const isPublicPage = currentPath === '/' || currentPath === '/index' || currentPath === '/home';
    
    // Only check authentication if AdminUtils is available
    if (typeof AdminUtils !== 'undefined' && !isLoginPage && !isPublicPage && !AdminUtils.isAuthenticated()) {
        console.log('[Auth Check] Not authenticated, redirecting to login');
        AdminUtils.redirectToLogin();
    }
}

// Auto-refresh dashboard data every 30 seconds
function startAutoRefresh() {
    setInterval(async function() {
        if (window.location.pathname.includes('/dashboard')) {
            await refreshDashboardData();
        }
    }, 30000);
}

// Initialize auto-refresh on dashboard
if (window.location.pathname.includes('/dashboard')) {
    startAutoRefresh();
}
